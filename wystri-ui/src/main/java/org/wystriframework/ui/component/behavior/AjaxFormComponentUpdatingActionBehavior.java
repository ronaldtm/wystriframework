package org.wystriframework.ui.component.behavior;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.danekja.java.util.function.serializable.SerializableConsumer;

public class AjaxFormComponentUpdatingActionBehavior extends AjaxFormComponentUpdatingBehavior {

    private SerializableConsumer<AjaxRequestTarget> action = t -> {};

    public AjaxFormComponentUpdatingActionBehavior(String event) {
        super(event);
    }

    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        action.accept(target);
    }

    public AjaxFormComponentUpdatingActionBehavior andThen(SerializableConsumer<AjaxRequestTarget> action) {
        this.action = this.action.andThen(action);
        return this;
    }
}
