package org.wystriframework.core.wicket;

import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.UrlResourceReference;

public class WystriResourceProvider implements IWystriResourceProvider {

    private ResourceReference bootstrapJS  = new UrlResourceReference(Url.parse("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"));
    private ResourceReference bootstrapCSS = new UrlResourceReference(Url.parse("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"));

    @Override
    public ResourceReference[] getCSS() {
        return new ResourceReference[] {
            getBootstrapCSS()
        };
    }

    @Override
    public ResourceReference[] getJS() {
        return new ResourceReference[] {
            getBootstrapJS()
        };
    }

    public ResourceReference getBootstrapCSS() {
        return bootstrapCSS;
    }

    public ResourceReference getBootstrapJS() {
        return bootstrapJS;
    }

}
