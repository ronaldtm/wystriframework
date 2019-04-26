package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IRecord extends Serializable {

    IEntity getEntity();
    <T> T getValue(IField<T> field);
    <T> void setValue(IField<T> field, T value);

}
