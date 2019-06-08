package org.wystriframework.core.formbuilder;

import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.wystriframework.core.definition.IRecord;

public class EntityPanel<R extends IRecord<E>, E> extends FormComponentPanel<R> {

    private EntityFormBuilder entityFormBuilder = new EntityFormBuilder();

    public EntityPanel(String id, IModel<R> model) {
        super(id, model);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        rebuild();
    }

    public void rebuild() {
        addOrReplace(entityFormBuilder.build("entity", getModel()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public IModel<R> getModel() {
        return (IModel<R>) getDefaultModel();
    }

    @Override
    @SuppressWarnings("unchecked")
    public R getModelObject() {
        return (R) getDefaultModelObject();
    }

}
