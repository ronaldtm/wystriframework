package org.wystriframework.crudgen.view.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.crudgen.view.wicket.theme.CrudgenTheme;

public class CrudgenPanel<R extends IRecord> extends Panel {

    private static final String INPUT_FRAGMENT_ID = "input";

    public CrudgenPanel(String id, IModel<R> model) {
        super(id, model);
    }

    public void rebuild(R record) {

    }

    @SuppressWarnings("unchecked")
    public IModel<R> getModel() {
        return (IModel<R>) getDefaultModel();
    }

    @SuppressWarnings("unchecked")
    public R getModelObject() {
        return (R) getDefaultModelObject();
    }

    public <T, F extends IField<T>> Component inputField(String id, IModel<F> model, IComponentCreator<? extends Component> creator) {
        final Fragment fragment = new Fragment(id, INPUT_FRAGMENT_ID, this, model);
        final Component input = creator.create("input");
        return fragment
            .add(new Label("label", model.map(it -> it.getName())))
            .add(input)
            .add(CrudgenTheme.get().fieldFeedback(id, fragment, input));
    }

    public <T, F extends IField<T>> Component checkField(String id, IModel<F> model, IComponentCreator<? extends Component> creator) {
        final Fragment fragment = new Fragment(id, INPUT_FRAGMENT_ID, this, model);
        final Component input = creator.create("check");
        return fragment
            .add(new Label("label", model.map(it -> it.getName())))
            .add(input)
            .add(CrudgenTheme.get().fieldFeedback(id, fragment, input));
    }
}
