package org.wystriframework.core.util;

public interface UncheckedAppendable extends Appendable {
    @Override
    UncheckedAppendable append(char s);
    @Override
    UncheckedAppendable append(CharSequence s);
    @Override
    UncheckedAppendable append(CharSequence s, int start, int end);
}