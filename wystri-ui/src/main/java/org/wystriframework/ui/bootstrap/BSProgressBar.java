package org.wystriframework.ui.bootstrap;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.request.resource.ResourceReference;
import org.wystriframework.ui.util.WicketComponentUtils;

public class BSProgressBar extends UploadProgressBar {

    public BSProgressBar(String id, Form<?> form) {
        super(id, form);
    }

    public BSProgressBar(String id, Form<?> form, FileUploadField uploadField) {
        super(id, form, uploadField);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Component status = get("status");
        remove("status");
        queue(status);
        setRenderBodyOnly(false);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(WicketComponentUtils.jsRefHeader(BSProgressBar.class, BSProgressBar.class.getSimpleName() + ".js"));
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        WicketComponentUtils.appendCssClasses(tag, "progress");
        WicketComponentUtils.appendDistinctValueToAttribute(tag, "styles", ';', "display:none");
    }

    @Override
    protected ResourceReference getCss() {
        return null;
    }
}
