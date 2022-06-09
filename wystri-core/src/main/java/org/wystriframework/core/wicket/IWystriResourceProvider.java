package org.wystriframework.core.wicket;

import org.apache.wicket.request.resource.ResourceReference;

public interface IWystriResourceProvider {

    ResourceReference[] getCSS();
    ResourceReference[] getJS();
}
