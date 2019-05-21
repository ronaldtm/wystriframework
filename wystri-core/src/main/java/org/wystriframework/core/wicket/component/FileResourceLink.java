package org.wystriframework.core.wicket.component;

import java.io.IOException;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.util.io.IOUtils;
import org.wystriframework.core.definition.IFileRef;
import org.wystriframework.core.wicket.util.WicketComponentUtils;

public class FileResourceLink<T extends IFileRef> extends ResourceLink<T> {

    public FileResourceLink(String id, IModel<T> model) {
        super(id, new IResource() {
            @Override
            public void respond(Attributes attr) {
                try {
                    IOUtils.copy(model.getObject().openStream(), attr.getResponse().getOutputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        });
        this.setModel(model);
    }

    @Override
    public boolean isEnabledInHierarchy() {
        return (getModelObject() != null) && (getModelObject().isValid());
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);

        if (!isEnabledInHierarchy())
            WicketComponentUtils.appendCssClasses(tag, "disabled");
    }
}