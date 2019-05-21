package org.wystriframework.crudgen.annotation;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.function.Consumer;

import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.wystriframework.core.definition.IFieldDelegate;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.util.ReflectionUtils;
import org.wystriframework.core.wicket.WystriConfiguration;

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

    private void processRequired(IFieldView<F> view, final AnnotatedRecord<E> arecord, final Field field) {
        if (field.required() != Bool.UNDEFINED) {
            view.setRequired(field.required().isTrue());
        } else {
            processPredicate(arecord, field.requiredIf(), view::setRequired);
        }
    }

    @SuppressWarnings("unchecked")
    private void processVisible(IFieldView<F> view, final AnnotatedRecord<E> arecord, final Field field) {
        processPredicate(arecord, field.visibleIf(), view::setVisible);
    }

    @SuppressWarnings("unchecked")
    private void processEnabled(IFieldView<F> view, final AnnotatedRecord<E> arecord, final Field field) {
        processPredicate(arecord, field.enabledIf(), view::setEnabled);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void processPredicate(final AnnotatedRecord<E> arecord, Class<? extends SerializablePredicate> predicateClass, Consumer<Boolean> callback) {
        if (!Modifier.isAbstract(predicateClass.getModifiers())) {
            Type[] argTypes = ReflectionUtils.getGenericTypesForInterface(predicateClass, SerializablePredicate.class);
            if (argTypes[0] == AnnotatedRecord.class) {
                SerializablePredicate<AnnotatedRecord<E>> predicate = WystriConfiguration.get().getBeanLookup().byType(predicateClass);
                callback.accept(predicate.test(arecord));
            } else {
                SerializablePredicate<E> predicate = WystriConfiguration.get().getBeanLookup().byType(predicateClass);
                callback.accept(predicate.test(arecord.getObject()));
            }
        }
    }
}
