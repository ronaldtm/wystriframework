package org.wystriframework.annotation.impl;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Objects;

import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.wystriframework.annotation.Field;
import org.wystriframework.core.definition.IConverter;
import org.wystriframework.core.definition.IFieldDelegate;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.wicket.Wystri;
import org.wystriframework.core.wicket.WystriConfiguration;

public class AnnotatedFieldDelegate<E, F> implements IFieldDelegate<E, F> {

    @Override
    @SuppressWarnings("unchecked")
    public void onAfterProcessed(IFieldView<E, F> view, IRecord<E> record) {

        final AnnotatedRecord<E> arecord = (AnnotatedRecord<E>) record;
        final AnnotatedField<E, F> afield = (AnnotatedField<E, F>) view.getField();
        final Field field = afield.getFieldAnnotation(Field.class);

        processValueChanged(view, arecord, field);
        processRequired(view, arecord, field);
        processVisible(view, arecord, field);
        processEnabled(view, arecord, field);
    }

    @SuppressWarnings("unchecked")
    protected void processValueChanged(IFieldView<E, F> view, final AnnotatedRecord<E> arecord, final Field field) {
        if (!Objects.equals(view.getSnapshotValue(), view.getValue()))
            view.markDirty();
    }

    @SuppressWarnings("unchecked")
    protected void processRequired(IFieldView<E, F> view, final AnnotatedRecord<E> arecord, final Field field) {
        final AnnotatedField<E, F> afield = (AnnotatedField<E, F>) view.getField();
        view.setRequired(afield.isRequired(arecord));
    }

    @SuppressWarnings("unchecked")
    protected void processVisible(IFieldView<E, F> view, final AnnotatedRecord<E> arecord, final Field field) {
        final boolean visible = processPredicate(arecord, field.visibleIf(), true);
        if (!visible && !Field.KEEP_VALUE.equals(field.invisibleDefaultValue()))
            view.setValue(converter(view, field.invisibleDefaultValue()));
        view.setVisible(visible);
    }

    @SuppressWarnings("unchecked")
    protected void processEnabled(IFieldView<E, F> view, final AnnotatedRecord<E> arecord, final Field field) {
        final boolean enabled = processPredicate(arecord, field.enabledIf(), true);
        if (!enabled && !Field.KEEP_VALUE.equals(field.disabledDefaultValue()))
            view.setValue(converter(view, field.disabledDefaultValue()));
        view.setEnabled(enabled);
    }

    private static <E, F> F converter(IFieldView<E, F> view, String value) {
        final IConverter<? extends F> converter = Wystri.get().getConverter(view.getType());
        return (converter != null) ? converter.stringToObject(value) : null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected static <E> boolean processPredicate(final AnnotatedRecord<E> arecord, Class<? extends SerializablePredicate> predicateClass, boolean defaultValue) {
        if (!Modifier.isAbstract(predicateClass.getModifiers())) {
            final SerializablePredicate predicate = WystriConfiguration.get().getBeanLookup().byType(predicateClass);
            final Type[] argTypes = Utils.getGenericTypesForInterface(predicateClass, SerializablePredicate.class);
            if (argTypes[0] == AnnotatedRecord.class) {
                return predicate.test(arecord);
            } else {
                return predicate.test(arecord.getTargetObject());
            }
        }
        return defaultValue;
    }
}
