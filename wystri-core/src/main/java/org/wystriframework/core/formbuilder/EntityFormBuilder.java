package org.wystriframework.core.formbuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.formbuilder.appenders.BooleanFieldAppender;
import org.wystriframework.core.formbuilder.appenders.IntegerFieldAppender;
import org.wystriframework.core.formbuilder.appenders.StringFieldAppender;
import org.wystriframework.core.wicket.bootstrap.BSFormRowLayout;
import org.wystriframework.core.wicket.bootstrap.IBSFormGroupLayout;

import com.google.common.collect.ImmutableMap;

public class EntityFormBuilder implements Serializable {

    private static final SerializableSupplier<IFieldComponentAppender<?>> DEFAULT_APPENDER = StringFieldAppender::new;

    @SuppressWarnings("unchecked")
    public Component build(String id, IModel<? extends IRecord> recordModel) {
        final RecordModel<? extends IRecord> record = new RecordModel<>(recordModel);

        final BSFormRowLayout layout = new BSFormRowLayout(id, record);
        layout.add(new EntityFormProcessorBehavior());

        List<IFieldView<?>> list = new ArrayList<>();
        for (final Iterator<? extends IField<?>> it = record.getObject().getEntity().fields().iterator(); it.hasNext();) {
            final IField<?> field = it.next();
            final IFieldView<?> view = appendField(layout, record, field);
            list.add(view);
        }

        return layout;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <F> IFieldView<F> appendField(final IBSFormGroupLayout layout, final RecordModel<? extends IRecord> record, IField<F> field) {
        return getAppender(field)
            .append(layout, record, field);
    }

    @SuppressWarnings("unchecked")
    private <F> IFieldComponentAppender<F> getAppender(IField<F> field) {
        Class<?> fieldType = field.getType();

        ConcurrentMap<Class<?>, SerializableSupplier<IFieldComponentAppender<?>>> appenders = getAppendersMap();

        return (IFieldComponentAppender<F>) appenders.getOrDefault(fieldType, DEFAULT_APPENDER).get();
    }

    protected ConcurrentMap<Class<?>, SerializableSupplier<IFieldComponentAppender<?>>> getAppendersMap() {
        ConcurrentMap<Class<?>, SerializableSupplier<IFieldComponentAppender<?>>> appenders = new ConcurrentHashMap<>(ImmutableMap.<Class<?>, SerializableSupplier<IFieldComponentAppender<?>>> builder()
            .put(String.class, DEFAULT_APPENDER)
            .put(int.class, IntegerFieldAppender::new)
            .put(Integer.class, IntegerFieldAppender::new)
            .put(boolean.class, BooleanFieldAppender::new)
            .put(Boolean.class, BooleanFieldAppender::new)
            .build());
        return appenders;
    }
}
