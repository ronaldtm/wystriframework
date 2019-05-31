package org.wystriframework.crudgen.annotation.impl;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.wystriframework.core.definition.IFieldDelegate;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.util.ReflectionUtils;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.crudgen.annotation.Field;

public class AnnotatedFieldDelegate<E, F> implements IFieldDelegate<F> {

    @Override
    @SuppressWarnings("unchecked")
    public void onAfterProcessed(IFieldView<F> view, IRecord record) {

        final AnnotatedRecord<E> arecord = (AnnotatedRecord<E>) record;
        final AnnotatedField<E, F> afield = (AnnotatedField<E, F>) view.getField();
        final Field field = afield.getFieldAnnotation(Field.class);

        processRequired(view, arecord, field);
        processVisible(view, arecord, field);
        processEnabled(view, arecord, field);
    }

    @SuppressWarnings("unchecked")
    protected void processRequired(IFieldView<F> view, final AnnotatedRecord<E> arecord, final Field field) {
        AnnotatedField<E, F> afield = (AnnotatedField<E, F>) view.getField();
        view.setRequired(afield.isRequired(arecord));
    }

    @SuppressWarnings("unchecked")
    protected static <E, F> void processVisible(IFieldView<F> view, final AnnotatedRecord<E> arecord, final Field field) {
        view.setVisible(processPredicate(arecord, field.visibleIf(), true));
    }

    @SuppressWarnings("unchecked")
    protected static <E, F> void processEnabled(IFieldView<F> view, final AnnotatedRecord<E> arecord, final Field field) {
        view.setEnabled(processPredicate(arecord, field.enabledIf(), true));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected static <E> boolean processPredicate(final AnnotatedRecord<E> arecord, Class<? extends SerializablePredicate> predicateClass, boolean defaultValue) {
        if (!Modifier.isAbstract(predicateClass.getModifiers())) {
            Type[] argTypes = ReflectionUtils.getGenericTypesForInterface(predicateClass, SerializablePredicate.class);
            if (argTypes[0] == AnnotatedRecord.class) {
                SerializablePredicate<AnnotatedRecord<E>> predicate = WystriConfiguration.get().getBeanLookup().byType(predicateClass);
                return predicate.test(arecord);
            } else {
                SerializablePredicate<E> predicate = WystriConfiguration.get().getBeanLookup().byType(predicateClass);
                return predicate.test(arecord.getTargetObject());
            }
        }
        return defaultValue;
    }
}
