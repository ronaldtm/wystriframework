package org.wystriframework.ui.component.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.IEvent;
import org.danekja.java.util.function.serializable.SerializablePredicate;

public class RefreshOnAjaxBehavior extends Behavior {

    private SerializablePredicate<Component> active;

    public RefreshOnAjaxBehavior() {
        this(c -> true);
    }
    public RefreshOnAjaxBehavior(SerializablePredicate<Component> active) {
        this.active = active;
    }

    @Override
    public void bind(Component component) {
        component
            .setOutputMarkupId(true)
            .setOutputMarkupPlaceholderTag(true);
    }
    @Override
    public void onEvent(Component component, IEvent<?> event) {
        if (event.getPayload() instanceof AjaxRequestTarget) {
            if ((component.getParent() != null) && component.getParent().isVisibleInHierarchy())
                ((AjaxRequestTarget) event.getPayload()).add(component);
        }
    }
    @Override
    public boolean isEnabled(Component component) {
        return active.test(component);
    }
}