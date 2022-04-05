package org.wystriframework.form.formbuilder;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.IEvent;

@SuppressWarnings("serial")
public class EntityFormProcessorBehavior extends Behavior {

    @Override
    public void onEvent(Component component, IEvent<?> event) {
        Object payload = event.getPayload();
        if (payload instanceof EntityFormProcessor) {
            ((EntityFormProcessor) payload).process((MarkupContainer) component);
        }
    }

    @Override
    public void onConfigure(Component component) {
        new EntityFormProcessor().process((MarkupContainer) component);
    }
}
