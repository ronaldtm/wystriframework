package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.List;

public interface IField<T> extends Serializable {

    IEntity getEntity();
    String getName();
    FieldType getType();
    List<IConstraint<T>> getConstraints();
    FieldMetadata getMetadata();

}
