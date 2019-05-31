package org.wystriframework.core.definition.constraints;

import org.wystriframework.core.definition.IConstrainable;

import com.google.common.collect.Range;

public class RangeConstraint<T extends Comparable<T>> implements ISimpleConstraint<T> {

    private Range<T> range;

    public RangeConstraint<T> setRange(Range<T> range) {
        this.range = range;
        return this;
    }

    @Override
    public String checkSimple(IConstrainable<T> c) {
        return range.contains(c.getValue()) ? null : "field.range";
    }
}
