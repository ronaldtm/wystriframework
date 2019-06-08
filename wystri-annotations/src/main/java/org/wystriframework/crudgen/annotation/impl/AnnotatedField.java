package org.wystriframework.crudgen.annotation.impl;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.*;
import static org.wystriframework.core.util.ReflectionUtils.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.wystriframework.core.definition.FieldMetadata;
import org.wystriframework.core.definition.IConstrainable;
import org.wystriframework.core.definition.IConstraint;
import org.wystriframework.core.definition.IConverter;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldDelegate;
import org.wystriframework.core.definition.IOptionsProvider;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.formbuilder.IFieldComponentAppender;
import org.wystriframework.core.util.IBeanLookup;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.crudgen.annotation.Constraint;
import org.wystriframework.crudgen.annotation.ConstraintFor;
import org.wystriframework.crudgen.annotation.CustomView;
import org.wystriframework.crudgen.annotation.Field;
import org.wystriframework.crudgen.annotation.Selection;
import org.wystriframework.crudgen.annotation.Selection.Option;

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
    public String requiredError(IRecord record) {
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
    public List<IConstraint<? super F>> getConstraints(IRecord record) {
        List<IConstraint<? super F>> constraints = new ArrayList<>();
        constraints.addAll(resolveMethodConstraints(record));
        constraints.addAll(resolveAnnotationConstraints());
        return constraints;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<IConstraint<? super F>> resolveAnnotationConstraints() {
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<IConstraint<? super F>> resolveMethodConstraints(IRecord record) {
        final List<IConstraint<? super F>> constraints = new ArrayList<>();
        final List<Pair<String, Class<?>[]>> constraintMethodSpecs = getAnnotatedMethods(entity.getObjectClass(), ConstraintFor.class).stream()
            .map(it -> Pair.of(it.getName(), it.getParameterTypes()))
            .collect(toList());
        for (Pair<String, Class<?>[]> methodSpec : constraintMethodSpecs) {
            constraints.add(new MethodConstraint(this, record, methodSpec));
        }
        return constraints;
    }

    @Override
    public FieldMetadata getMetadata() {
        final FieldMetadata metadata = new FieldMetadata();

        final CustomView view = getFieldAnnotation(CustomView.class);
        if (view != null) {
            if (!Modifier.isAbstract(view.appender().getModifiers()))
                metadata.put(IFieldComponentAppender.class, lookup().newInstance(view.appender(), view.appenderArgs()));
        }

        return metadata;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<? extends IOptionsProvider<F>> getOptionsProvider() {

        Selection selection = getFieldAnnotation(Selection.class);
        if (selection != null) {
            if (!Modifier.isAbstract(selection.provider().getModifiers())) {
                return (Optional<? extends IOptionsProvider<F>>) Optional.of(lookup().newInstance(selection.provider(), new String[0]));

            } else if (selection.options().length > 0) {
                final Class<F> type = this.getType();
                final IConverter<F> converter = WystriConfiguration.get().getConverter(type);

                List<Triple<String, F, String>> opcoes = new ArrayList<>();
                for (Option option : selection.options()) {
                    final String label = option.value();
                    final String id = (isNotBlank(option.id())) ? option.id() : label;
                    final F value = (F) converter.stringToObject(id);

                    opcoes.add(Triple.of(id, value, label));
                }
                return (Optional<? extends IOptionsProvider<F>>) Optional.of(new IOptionsProvider<F>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List<F> getOptions(IRecord record) {
                        return opcoes.stream()
                            .map(it -> it.getMiddle())
                            .collect(toList());
                    }
                    @Override
                    public String objectToId(F object) {
                        return opcoes.stream()
                            .filter(it -> Objects.equals(it.getMiddle(), object))
                            .findFirst()
                            .map(it -> it.getLeft())
                            .orElse(null);
                    }
                    @Override
                    public F idToObject(String id, List<? extends F> options) {
                        return opcoes.stream()
                            .filter(it -> Objects.equals(it.getLeft(), id))
                            .findFirst()
                            .map(it -> it.getMiddle())
                            .orElse(null);
                    }
                    @Override
                    public String objectToDisplay(F object, List<? extends F> options) {
                        return opcoes.stream()
                            .filter(it -> Objects.equals(it.getMiddle(), object))
                            .findFirst()
                            .map(it -> it.getRight())
                            .orElse(null);
                    }
                });

            } else if (selection.value().length > 0) {
                final Class<F> type = this.getType();
                final IConverter<F> converter = WystriConfiguration.get().getConverter(type);

                List<String> opcoes = new ArrayList<>(asList(selection.value()));
                return (Optional<? extends IOptionsProvider<F>>) Optional.of(new IOptionsProvider<F>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List<F> getOptions(IRecord record) {
                        return opcoes.stream()
                            .map(it -> converter.stringToObject(it))
                            .collect(toList());
                    }
                    @Override
                    public String objectToId(F object) {
                        return converter.objectToString(object);
                    }
                    @Override
                    public F idToObject(String id, List<? extends F> options) {
                        return converter.stringToObject(id);
                    }
                    @Override
                    public String objectToDisplay(F object, List<? extends F> options) {
                        return Objects.toString(object, "");
                    }
                });
            }
        }

        return Optional.empty();
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
                return predicate.test(arecord.getTargetObject());
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
    public Class<F> getType() {
        return (Class<F>) getDeclaredField().getType();
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

    protected static final class MethodConstraint<F> implements IConstraint<F> {
        private final AnnotatedField<?, F>     field;
        private final IRecord                  record;
        private final Pair<String, Class<?>[]> methodSpec;
        protected MethodConstraint(AnnotatedField<?, F> field, IRecord record, Pair<String, Class<?>[]> methodSpec) {
            this.field = field;
            this.record = record;
            this.methodSpec = methodSpec;
        }
        @Override
        public void check(IConstrainable<F> c) {
            final Class<?>[] argTypes = methodSpec.getValue();
            Method method;
            try {
                method = field.getEntity().getObjectClass().getMethod(methodSpec.getKey(), argTypes);
            } catch (NoSuchMethodException | SecurityException ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
            final ConstraintFor constraintFor = method.getAnnotation(ConstraintFor.class);
            final String targetFieldName = constraintFor.value();

            if (targetFieldName.equals(field.getName())) {
                Object[] args = new Object[argTypes.length];
                for (int i = 0; i < args.length; i++) {
                    if (IConstrainable.class.isAssignableFrom(argTypes[i])) {
                        args[i] = c;
                    } else {
                        throw new IllegalArgumentException("Unsupported argument type in constraint method: " + argTypes[i]);
                    }
                }
                Object result;
                try {
                    result = (Modifier.isStatic(method.getModifiers()))
                        ? method.invoke(null, args)
                        : method.invoke(record.getTargetObject(), args);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    throw new RuntimeException(ex.getMessage(), ex);
                }
                if (result instanceof String)
                    c.error((String) result);
            }
        }
    }

}
