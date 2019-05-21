package org.wystriframework.crudgen.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.core.util.lang.PropertyResolver;
import org.wystriframework.core.definition.FieldMetadata;
import org.wystriframework.core.definition.IConstraint;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldDelegate;

public class AnnotatedField<E, F> implements IField<F> {

    private final AnnotatedEntity<E>           entity;
    private final String                       name;
    private final FieldMetadata                metadata    = new FieldMetadata();
    private final List<IConstraint<? super F>> constraints = new ArrayList<>();

    private final IFieldDelegate<F>            delegate;

    public static <E> AnnotatedField<E, ?> newSimpleField(AnnotatedEntity<E> entity, java.lang.reflect.Field field) {
        return new AnnotatedField<>(entity, field).withDefaultConstraints();
    }

    public AnnotatedField(AnnotatedEntity<E> entity, java.lang.reflect.Field field) {
        this.entity = entity;
        this.name = field.getName();
        this.delegate = new AnnotatedFieldDelegate<>();
    }

    @Override
    public List<IConstraint<? super F>> getConstraints() {
        return constraints;
    }

    @Override
    public FieldMetadata getMetadata() {
        return this.metadata;
    }

    @Override
    public IFieldDelegate<F> getDelegate() {
        return this.delegate;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public AnnotatedField<E, F> withDefaultConstraints() {
        //        Field field = getFieldAnnotation();
        //        if (field != null) {
        //            final IBeanLookup beanLookup = WystriConfiguration.get().getBeanLookup();
        //
        //            final Class<? extends SerializablePredicate> requiredIfClass = field.requiredIf();
        //            if (!Modifier.isAbstract(requiredIfClass.getModifiers())) {
        //                SerializablePredicate requiredIf = beanLookup.byType(requiredIfClass);
        //                requiredIf.test(null);
        //            }

        //        setConstraints(resolveConstraints()); //
        //        }
        return this;
    }

    public <A extends Annotation> A getFieldAnnotation(Class<A> annotationClass) {
        return getDeclaredField().getAnnotation(annotationClass);
    }

    public void addConstraint(IConstraint<F> constraint) {
        this.constraints.add(constraint);
    }
    public void setConstraints(Collection<? extends IConstraint<F>> constraints) {
        this.constraints.clear();
        this.constraints.addAll(constraints);
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
