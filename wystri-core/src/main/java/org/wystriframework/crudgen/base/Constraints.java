package org.wystriframework.crudgen.base;

import org.wystriframework.core.definition.constraints.MaxLengthConstraint;

public class Constraints {

    public static MaxLengthConstraint maxLength(int len) {
        return new MaxLengthConstraint(len);
    }
}
