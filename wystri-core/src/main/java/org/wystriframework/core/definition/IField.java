package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IField<T> extends Serializable {

    IEntity getEntity();
    String getName();
    Class<? extends T> getType();
    List<IConstraint<? super T>> getConstraints();
    FieldMetadata getMetadata();
    IFieldDelegate<T> getDelegate();

    @SuppressWarnings("unchecked")
    default <C extends IConstraint<? super T>> Optional<C> getConstraint(Class<C> type) {
        final Optional<C> exact = getConstraints().stream()
            .filter(it -> type == it.getClass())
            .map(it -> (C) it)
            .findFirst();

        if (exact.isPresent())
            return exact;

        final Optional<C> subclass = getConstraints().stream()
            .filter(it -> type.isAssignableFrom(it.getClass()))
            .map(it -> (C) it)
            .findFirst();

        return subclass;
    }
}
