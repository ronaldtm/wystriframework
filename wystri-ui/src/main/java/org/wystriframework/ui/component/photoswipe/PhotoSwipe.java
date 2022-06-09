package org.wystriframework.ui.component.photoswipe;

import static com.google.common.collect.Collections2.*;
import static org.wystriframework.ui.util.IBehaviorShortcutsMixin.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

//import javax.json.Json;
//import javax.json.JsonArrayBuilder;
//import javax.json.JsonObjectBuilder;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.wystriframework.ui.component.jquery.JQuery;

import com.github.openjson.JSONArray;
import com.github.openjson.JSONObject;

public class PhotoSwipe extends Panel {

    public PhotoSwipe(String id) {
        super(id);

        add($b.attrAppend("class", "pswp"));
        add($b.attrReplace("tabindex", "-1"));
        add($b.attrReplace("role", "dialog"));
        add($b.attrReplace("aria-hidden", "true"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));

        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(PhotoSwipe.class, "res/photoswipe.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(PhotoSwipe.class, "res/photoswipe-ui-default.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(PhotoSwipe.class, "res/jquery-photoswipe.js")));
        response.render(CssHeaderItem.forReference(new PackageResourceReference(PhotoSwipe.class, "res/photoswipe.css")));
        response.render(CssHeaderItem.forReference(new PackageResourceReference(PhotoSwipe.class, "res/default-skin/default-skin.css")));
    }

    public static String getJavascriptFunction(Component comp, PSItem... items) {
        return getJavascriptFunction(comp, Arrays.asList(items));
    }
    public static String getJavascriptFunction(Component comp, Collection<PSItem> items) {
        return findPhotoSwipe(comp)
            .map(ps -> ""
                + "(function(evt) {"
                + "  " + JQuery.$(ps) + ".photoswipe({"
                + "  items:" + toString(items)
                + "  });"
                + "  evt.preventDefault();"
                + "})")
            .orElse("(function() {})");
    }

    public static boolean hasSupport(Component comp) {
        return findPhotoSwipe(comp).isPresent();
    }
    private static Optional<PhotoSwipe> findPhotoSwipe(Component comp) {
        return Optional.ofNullable(comp.visitParents(MarkupContainer.class, (container, visit) -> container.stream()
            .filter(it -> it instanceof PhotoSwipe)
            .map(it -> (PhotoSwipe) it)
            .findFirst()
            .ifPresent(it -> visit.stop(it))));
    }

    private static String toString(Collection<PSItem> items) {
        Collection<JSONObject> objs = transform(items, it -> it.get());
        return new JSONArray(objs).toString();
    }

    public static PSItem html(String html) {
        return () -> new JSONObject().put("html", html);
    }
    public static PSItem iframe(Url url) {
        String surl = url.toString();
        return () -> new JSONObject().put("html", "<iframe src='" + surl + "' style='position:relative;width:100%;height:100%;margin-top:44px;'></iframe>");
    }
    public static PSItem pdf(Url url) {
        String surl = url.toString();
        return () -> new JSONObject().put("html", "<iframe src='" + surl + "' style='position:relative;width:100%;height:100%;margin-top:44px;'></iframe>");
    }
    public static PSItem img(Url url) {
        String surl = url.toString();
        return () -> new JSONObject().put("src", surl);
    }
    public static PSItem img(Url url, int w, int h) {
        String surl = url.toString();
        return () -> new JSONObject().put("src", surl).put("w", w).put("h", h);
    }

    public static interface PSItem extends SerializableSupplier<JSONObject> {}
}
