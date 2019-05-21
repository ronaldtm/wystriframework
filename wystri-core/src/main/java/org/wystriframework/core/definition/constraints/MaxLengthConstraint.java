package org.wystriframework.core.definition.constraints;

import org.wystriframework.core.definition.IConstrainable;

public class MaxLengthConstraint implements ISimpleConstraint<String> {

    private final int maxLength;

    public MaxLengthConstraint(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String checkSimple(IConstrainable<String> c) {
        return ((maxLength > 0) && (c.getValue().length() > maxLength)) ? "field.maxLength" : null;
    }

    public int getMaxLength() {
        return maxLength;
    }
}
