package org.wystriframework.form.formbuilder;

import static org.wystriframework.ui.util.IBehaviorShortcutsMixin.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.StringValue;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.wystriframework.core.definition.IEntity;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldLayout.Cell;
import org.wystriframework.core.definition.IFieldLayout.Row;
import org.wystriframework.core.definition.IFieldLayout.Section;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IFileRef;
import org.wystriframework.core.definition.IOptionsProvider;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.core.wicket.WystriWicketUtils;
import org.wystriframework.form.formbuilder.appenders.BooleanFieldAppender;
import org.wystriframework.form.formbuilder.appenders.CheckboxFieldAppender;
import org.wystriframework.form.formbuilder.appenders.DropDownFieldAppender;
import org.wystriframework.form.formbuilder.appenders.FileUploadFieldAppender;
import org.wystriframework.form.formbuilder.appenders.IntegerFieldAppender;
import org.wystriframework.form.formbuilder.appenders.StringFieldAppender;
import org.wystriframework.ui.bootstrap.BSFormGroup;
import org.wystriframework.ui.bootstrap.BSFormRowLayout;
import org.wystriframework.ui.bootstrap.BSFormSectionListView;
import org.wystriframework.ui.bootstrap.IBSFormGroupLayout;

import com.google.common.collect.ImmutableMap;

public class EntityFormBuilder implements Serializable {

    private static final SerializableSupplier<IFieldComponentAppender<?>> DEFAULT_APPENDER = StringFieldAppender::new;

    @SuppressWarnings("unchecked")
    public <E, F> Component build(String id, IModel<? extends IRecord<E>> recordModel) {
        final RecordModel<? extends IRecord<E>, E> record = new RecordModel<>(recordModel);
        final IEntity<?> entity = record.getObject().getEntity();

        final BSFormSectionListView sectionsView = new BSFormSectionListView(id);
        for (Section section : entity.getLayout().getSections()) {
            final BSFormRowLayout layout = new BSFormRowLayout("layout", record);
            sectionsView.appendSection(section::getTitle, layout);
            for (Row row : section.getRows()) {
                final List<Cell> cells = row.getCells();
                final IBSFormGroupLayout formRow = layout.newFormRow();
                Map<String, StringValue> params = new HashMap<>();
                for (Cell cell : cells) {
                    final IField<E, ?> field = (IField<E, ?>) cell.getField();
                    final String spec = cell.getSpec();

                    final SerializableConsumer<BSFormGroup> groupConfigurer = g -> g.add($b.attrAppend("class", " " + spec));
                    final FieldComponentContext<E, ?> ctx = new FieldComponentContext<>(formRow, record, field, params, groupConfigurer);

                    appendField(ctx);
                }
            }
        }
        return sectionsView;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <E, F> void appendField(FieldComponentContext<E, F> ctx) {
        final IFieldView<E, F> fieldView = getAppender(ctx.getField(), DEFAULT_APPENDER).append(ctx);
        WystriWicketUtils.setFieldView(ctx.getFormGroup(), fieldView);
    }

    @SuppressWarnings("unchecked")
    protected <E, F> IFieldComponentAppender<F> getAppender(IField<E, F> field, SerializableSupplier<IFieldComponentAppender<?>> defaultAppender) {
        final IFieldComponentAppender<F> appender = field.getMetadata().get(IFieldComponentAppender.class);
        if (appender != null)
            return appender;

        return findAppender(field)
            .orElseGet(() -> (IFieldComponentAppender<F>) defaultAppender.get());
    }

    @SuppressWarnings("rawtypes")
    private static final ConcurrentMap<Class<?>, SerializableSupplier<IFieldComponentAppender>> appenders = new ConcurrentHashMap<>(ImmutableMap.<Class<?>, SerializableSupplier<IFieldComponentAppender>> builder()
        .put(String.class, StringFieldAppender::new)
        .put(int.class, IntegerFieldAppender::new)
        .put(Integer.class, IntegerFieldAppender::new)
        .put(boolean.class, CheckboxFieldAppender::new)
        .put(Boolean.class, BooleanFieldAppender::new)
        .put(IFileRef.class, FileUploadFieldAppender::new)
        .build());

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <E, F> Optional<IFieldComponentAppender<F>> findAppender(IField<E, F> field) {

        Optional<? extends IOptionsProvider<E, F>> op = field.getOptionsProvider();

        if (op.isPresent()) {
            return Optional.of(new DropDownFieldAppender<>());
        }

        return Optional.ofNullable(appenders.get(field.getType()))
            .map(it -> it.get())
            .map(it -> WystriConfiguration.get().getBeanLookup().inject(it));
    }
}
