package org.wystriframework.core.wicket.bootstrap;

import java.io.Serializable;

public interface IBSFormLayoutConfig extends Serializable {
    BSSize getComponentsSize();
    String format(Object value);
}