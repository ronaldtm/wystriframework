package org.wystriframework.ui.bootstrap;

public enum BSResponsiveBreakpoint {
    NONE, xs, sm, md, lg;

    @Override
    public String toString() {
        return (this == NONE) ? "" : name();
    }
}
