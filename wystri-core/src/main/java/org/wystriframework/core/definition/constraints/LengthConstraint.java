package org.wystriframework.core.definition.constraints;

import org.wystriframework.core.definition.IConstrainable;

public class LengthConstraint implements ISimpleConstraint<String> {

    private int min = 0;
    private int max = Integer.MAX_VALUE;

    @Override
    public String checkSimple(IConstrainable<String> c) {
        final String value = c.getValue();

        if ((min > 0) && (value.length() < min))
            return "LengthConstraint.min";

        if ((max > 0) && (value.length() > max))
            return "LengthConstraint.max";

        return null;
    }

    //@formatter:off
    public int getMin() { return min; }
    public int getMax() { return max; }
    public LengthConstraint setMin(int min) { this.min = min; return this; }
    public LengthConstraint setMax(int max) { this.max = max; return this; }
    //@formatter:on
}
