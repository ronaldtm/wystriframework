package org.wystriframework.ui.component.photoswipe;

import static org.apache.commons.lang3.StringUtils.*;
import static org.wystriframework.ui.component.jquery.JQuery.*;

import org.apache.wicket.IRequestListener;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wystriframework.core.definition.IFileRef;
import org.wystriframework.ui.component.FileResourceLink;
import org.wystriframework.ui.component.photoswipe.PhotoSwipe.PSItem;

public class PhotoSwipeFileResourceLink<T extends IFileRef> extends FileResourceLink<T> implements IRequestListener {

    public PhotoSwipeFileResourceLink(String id, IModel<T> resource) {
        super(id, resource);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        if (getModelObject().isValid() && PhotoSwipe.hasSupport(this)) {

            final PSItem item = toItem();
            if (item != null) {
                $(this)
                    .on("click", ""
                        + "\n function(evt) {"
                        + "\n   if ($(this).attr('href')) {"
                        + "\n     (" + PhotoSwipe.getJavascriptFunction(this, item) + ")();"
                        + "\n     evt.preventDefault();"
                        + "\n   }"
                        + "\n }")
                    .renderOnDomReady(response);
            }
        }
    }

    private PSItem toItem() {
        final Url url = Url.parse(this.urlForListener(new PageParameters()).toString());

        final IFileRef res = getModelObject();

        final String mimeType = defaultString(res.getMimeType()).toLowerCase();

        if (mimeType.startsWith("image/"))
            return PhotoSwipe.img(url);

        else if (mimeType.startsWith("application/pdf"))
            return PhotoSwipe.pdf(url);

        else
            return null;
    }
}