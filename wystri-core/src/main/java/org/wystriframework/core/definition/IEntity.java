package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.List;

public interface IEntity extends Serializable {

    String getName();
    List<? extends IField<?>> getFields();

}
