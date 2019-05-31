package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IRecord extends Serializable {

    Object getTargetObject();
    IEntity getEntity();
    <F> F getValue(IField<F> field);
    <F> void setValue(IField<F> field, F value);

}
