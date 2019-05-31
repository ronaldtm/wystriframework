package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IField<T> extends Serializable {

    IEntity getEntity();
    String getName();
    Class<T> getType();
    boolean isRequired(IRecord record);
    boolean isEnabled(IRecord record);
    boolean isVisible(IRecord record);
    List<IConstraint<? super T>> getConstraints(IRecord record);
    FieldMetadata getMetadata();
    IFieldDelegate<T> getDelegate();
    String requiredError();

    default String getLabel() {
        return getName();
    };

    @SuppressWarnings("unchecked")
    default <C extends IConstraint<? super T>> Optional<C> getConstraint(IRecord record, Class<C> type) {
        final Optional<C> exact = getConstraints(record).stream()
            .filter(it -> type == it.getClass())
            .map(it -> (C) it)
            .findFirst();

        if (exact.isPresent())
            return exact;

        final Optional<C> subclass = getConstraints(record).stream()
            .filter(it -> type.isAssignableFrom(it.getClass()))
            .map(it -> (C) it)
            .findFirst();

        return subclass;
    }
}
