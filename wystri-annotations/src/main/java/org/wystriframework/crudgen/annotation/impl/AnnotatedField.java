package org.wystriframework.crudgen.annotation.impl;

import static java.util.Arrays.*;
import static org.apache.commons.lang3.StringUtils.*;
import static org.wystriframework.core.util.ReflectionUtils.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.wystriframework.core.definition.FieldMetadata;
import org.wystriframework.core.definition.IConstraint;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldDelegate;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.util.IBeanLookup;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.crudgen.annotation.Constraint;
import org.wystriframework.crudgen.annotation.CustomView;
import org.wystriframework.crudgen.annotation.Field;

public class AnnotatedField<E, F> implements IField<F> {

    private final AnnotatedEntity<E> entity;
    private final String             name;

    private final IFieldDelegate<F>  delegate;

    public static <E> AnnotatedField<E, ?> newSimpleField(AnnotatedEntity<E> entity, java.lang.reflect.Field field) {
        return new AnnotatedField<>(entity, field);
    }

    public AnnotatedField(AnnotatedEntity<E> entity, java.lang.reflect.Field field) {
        this.entity = entity;
        this.name = field.getName();
        this.delegate = new AnnotatedFieldDelegate<>();
    }

    @Override
    public String getLabel() {
        final String label = getFieldAnnotation(Field.class).label();
        return (isNotBlank(label)) ? label : getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isRequired(IRecord record) {
        final Field field = getFieldAnnotation(Field.class);
        if (field.required() != Bool.UNDEFINED) {
            return field.required().isTrue();
        } else {
            return testPredicate((AnnotatedRecord<E>) record, field.requiredIf(), false);
        }
    }

    @Override
    public String requiredError() {
        final Field field = getFieldAnnotation(Field.class);
        return field.requiredError();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isEnabled(IRecord record) {
        return testPredicate((AnnotatedRecord<E>) record, getFieldAnnotation(Field.class).enabledIf(), true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isVisible(IRecord record) {
        return testPredicate((AnnotatedRecord<E>) record, getFieldAnnotation(Field.class).visibleIf(), true);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<IConstraint<? super F>> getConstraints() {
        List<IConstraint<? super F>> constraints = new ArrayList<>();
        List<Annotation> annotations = getAnnotatedAnnotations(getDeclaredField(), Constraint.class);
        for (Annotation annotation : annotations) {
            Constraint constraint = getMetaAnnotation(annotation, Constraint.class);

            Class<? extends IConstraint> implType = constraint.type();
            IConstraint<? super F> impl = WystriConfiguration.get().getBeanLookup().byType(implType);

            List<Method> annotationProperties = asList(annotation.getClass().getDeclaredMethods());
            for (Method annotationProperty : annotationProperties) {
                final String setterName = "set" + StringUtils.capitalize(annotationProperty.getName());
                final Class<?>[] argTypes = new Class<?>[] { annotationProperty.getReturnType() };

                try {
                    Method setter;
                    try {
                        setter = impl.getClass().getDeclaredMethod(setterName, argTypes);
                    } catch (NoSuchMethodException nsmex) {
                        continue;
                    }
                    if (setter != null) {
                        Object value = annotationProperty.invoke(annotation);
                        setter.setAccessible(true);
                        setter.invoke(impl, value);
                    }
                } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    throw new IllegalArgumentException(ex.getMessage(), ex);
                }
            }
            constraints.add(impl);
        }
        return constraints;
    }

    @Override
    public FieldMetadata getMetadata() {
        CustomView view = getFieldAnnotation(CustomView.class);
        if (!Modifier.isAbstract(view.appender().getModifiers())) {
            lookup().newInstance(view.appender(), view.appenderArgs());
        }
        return new FieldMetadata();
    }

    @Override
    public IFieldDelegate<F> getDelegate() {
        return this.delegate;
    }

    public <A extends Annotation> A getFieldAnnotation(Class<A> annotationClass) {
        return resolveAnnotation(getDeclaredField(), annotationClass);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <E> boolean testPredicate(final AnnotatedRecord<E> arecord, Class<? extends SerializablePredicate> predicateClass, boolean defaultValue) {
        if (!Modifier.isAbstract(predicateClass.getModifiers())) {
            Type[] argTypes = getGenericTypesForInterface(predicateClass, SerializablePredicate.class);
            if (argTypes[0] == AnnotatedRecord.class) {
                SerializablePredicate<AnnotatedRecord<E>> predicate = lookup().byType(predicateClass);
                return predicate.test(arecord);
            } else {
                SerializablePredicate<E> predicate = lookup().byType(predicateClass);
                return predicate.test(arecord.getObject());
            }
        }
        return defaultValue;
    }

    private static IBeanLookup lookup() {
        return WystriConfiguration.get().getBeanLookup();
    }

    //@formatter:off
    @Override public AnnotatedEntity<E> getEntity()  { return entity  ; }
    @Override public String             getName()    { return name    ; }
    //@formatter:on

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends F> getType() {
        return (Class<? extends F>) getDeclaredField().getType();
    }

    private java.lang.reflect.Field getDeclaredField() {
        try {
            return getEntity().getObjectClass().getDeclaredField(getName());
        } catch (NoSuchFieldException | SecurityException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    public F getValue(E object) {
        return (F) PropertyResolver.getValue(getName(), object);
    }

    public void setValue(E object, F value) {
        PropertyResolver.setValue(getName(), object, value, null);
    }
}
