package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IConstraint<T> extends Serializable {

    void check(IConstrainable<T> c);
}
