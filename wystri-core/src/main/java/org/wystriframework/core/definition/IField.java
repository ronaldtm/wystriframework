package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IField<E, T> extends Serializable {

    IEntity<E> getEntity();
    String getName();
    Class<T> getType();
    boolean isRequired(IRecord<E> record);
    boolean isEnabled(IRecord<E> record);
    boolean isVisible(IRecord<E> record);
    List<IConstraint<? super T>> getConstraints(IRecord<E> record);
    FieldMetadata getMetadata();
    IFieldDelegate<E, T> getDelegate();
    String requiredError(IRecord<E> record);
    
    Optional<? extends IOptionsProvider<E, T>> getOptionsProvider();

    default String getLabel() {
        return getName();
    };


    @SuppressWarnings("unchecked")
    default <C extends IConstraint<? super T>> Optional<C> getConstraint(IRecord<E> record, Class<C> type) {
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
