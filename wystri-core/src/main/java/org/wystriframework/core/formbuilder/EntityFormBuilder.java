package org.wystriframework.core.formbuilder;

import static org.wystriframework.core.wicket.util.IBehaviorShortcutsMixin.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.StringValue;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.wystriframework.core.definition.IEntity;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldLayout.Cell;
import org.wystriframework.core.definition.IFieldLayout.Row;
import org.wystriframework.core.definition.IFieldLayout.Section;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IFileRef;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.formbuilder.appenders.BooleanFieldAppender;
import org.wystriframework.core.formbuilder.appenders.FileUploadFieldAppender;
import org.wystriframework.core.formbuilder.appenders.IntegerFieldAppender;
import org.wystriframework.core.formbuilder.appenders.StringFieldAppender;
import org.wystriframework.core.wicket.bootstrap.BSFormRowLayout;
import org.wystriframework.core.wicket.bootstrap.BSFormSectionListView;
import org.wystriframework.core.wicket.bootstrap.IBSFormGroupLayout;

import com.google.common.collect.ImmutableMap;

public class EntityFormBuilder implements Serializable {

    private static final SerializableSupplier<IFieldComponentAppender<?>> DEFAULT_APPENDER = StringFieldAppender::new;

    @SuppressWarnings("unchecked")
    public Component build(String id, IModel<? extends IRecord> recordModel) {
        final RecordModel<? extends IRecord> record = new RecordModel<>(recordModel);
        final IEntity entity = record.getObject().getEntity();

        final BSFormSectionListView sectionsView = new BSFormSectionListView(id);
        for (Section section : entity.getLayout().getSections()) {
            final BSFormRowLayout layout = new BSFormRowLayout("layout", record);
            sectionsView.appendSection(section::getTitle, layout);
            for (Row row : section.getRows()) {
                final List<Cell> cells = row.getCells();
                final IBSFormGroupLayout formRow = layout.newFormRow();
                Map<String, StringValue> params = new HashMap<>();
                for (Cell cell : cells) {
                    final String spec = cell.getSpec();
                    appendField(new FieldComponentContext<>(formRow, record, cell.getField(), params, g -> g.add($b.attrAppend("class", " " + spec))));
                }
            }
        }
        return sectionsView;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <F> IFieldView<F> appendField(FieldComponentContext<F> ctx) {
        return getAppender(ctx.getField())
            .append(ctx);
    }

    @SuppressWarnings("unchecked")
    private <F> IFieldComponentAppender<F> getAppender(IField<F> field) {
        Class<?> fieldType = field.getType();

        ConcurrentMap<Class<?>, SerializableSupplier<IFieldComponentAppender<?>>> appenders = getAppendersMap();

        return (IFieldComponentAppender<F>) appenders.getOrDefault(fieldType, DEFAULT_APPENDER).get();
    }

    protected static ConcurrentMap<Class<?>, SerializableSupplier<IFieldComponentAppender<?>>> getAppendersMap() {
        ConcurrentMap<Class<?>, SerializableSupplier<IFieldComponentAppender<?>>> appenders = new ConcurrentHashMap<>(ImmutableMap.<Class<?>, SerializableSupplier<IFieldComponentAppender<?>>> builder()
            .put(String.class, DEFAULT_APPENDER)
            .put(int.class, IntegerFieldAppender::new)
            .put(Integer.class, IntegerFieldAppender::new)
            .put(boolean.class, BooleanFieldAppender::new)
            .put(Boolean.class, BooleanFieldAppender::new)
            .put(IFileRef.class, FileUploadFieldAppender::new)
            .build());
        return appenders;
    }
}
