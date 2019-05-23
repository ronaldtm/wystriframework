package org.wystriframework.core.formbuilder;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.util.visit.IVisit;
import org.wystriframework.core.definition.IFieldDelegate;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IRecord;

public class EntityFormProcessor {

    private static final MetaDataKey<IFieldView<?>> FIELD_VIEW = new MetaDataKey<IFieldView<?>>() {};

    public static IFieldView<?> getView(Component comp) {
        return comp.getMetaData(FIELD_VIEW);
    }

    public static void associateView(Component comp, IFieldView<?> fieldView) {
        comp.setMetaData(FIELD_VIEW, fieldView);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void process(MarkupContainer rootContainer) {
        MarkupContainer recordContainer = rootContainer.visitChildren(MarkupContainer.class, (MarkupContainer comp, IVisit<MarkupContainer> visit) -> {
            if (comp.getDefaultModelObject() instanceof IRecord)
                visit.stop(comp);
        });

        final IRecord record = (IRecord) recordContainer.getDefaultModelObject();

        recordContainer.visitChildren((Component comp, IVisit<Void> visit) -> {
            IFieldView<?> view = EntityFormProcessor.getView(comp);
            if (view != null) {
                IFieldDelegate delegate = view.getField().getDelegate();
                delegate.onAfterProcessed(view, record);
            }
        });
    }
    
    public void bubble(Component source) {
        source.send(source, Broadcast.BUBBLE, new EntityFormProcessor());
    }
}
