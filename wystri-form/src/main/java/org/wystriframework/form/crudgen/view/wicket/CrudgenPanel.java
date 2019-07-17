package org.wystriframework.form.crudgen.view.wicket;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.form.formbuilder.EntityFormBuilder;
import org.wystriframework.ui.component.photoswipe.PhotoSwipe;

public class CrudgenPanel<R extends IRecord<E>, E> extends Panel {

    private EntityFormBuilder entityFormBuilder = new EntityFormBuilder();

    public CrudgenPanel(String id, IModel<R> model) {
        super(id, model);

        add(new PhotoSwipe("photoSwipe"));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        rebuild();
    }

    public void rebuild() {
        addOrReplace(entityFormBuilder.build("entity", getModel()));
    }

    @SuppressWarnings("unchecked")
    public IModel<R> getModel() {
        return (IModel<R>) getDefaultModel();
    }

    @SuppressWarnings("unchecked")
    public R getModelObject() {
        return (R) getDefaultModelObject();
    }

}
