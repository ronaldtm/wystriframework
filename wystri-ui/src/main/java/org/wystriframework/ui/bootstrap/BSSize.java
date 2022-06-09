package org.wystriframework.ui.bootstrap;

public enum BSSize {
    md {
        @Override
        public String prefixWith(String prefix) {
            return prefix;
        }
        @Override
        public CharSequence insertBetween(String prefix, String suffix) {
            return prefix + '-' + suffix;
        }
    },
    sm, lg;

    public CharSequence prefixWith(String prefix) {
        return prefix + '-' + name();
    }
    public CharSequence insertBetween(String prefix, String suffix) {
        return prefix + '-' + name() + '-' + suffix;
    }
}