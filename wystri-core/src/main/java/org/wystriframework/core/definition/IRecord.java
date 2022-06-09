package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IRecord<E> extends Serializable {

    E getTargetObject();
    IEntity<E> getEntity();
    <F> F getValue(IField<E, F> field);
    <F> void setValue(IField<E, F> field, F value);

}
