package org.wystriframework.annotation;

import java.io.Serializable;

import org.wystriframework.core.definition.IConstraint;

public interface IConstraintsProvider extends Serializable {

    static class NoopConstraintProvider implements IConstraintsProvider {
        @Override
        @SuppressWarnings("unchecked")
        public <T> IConstraint<? super T>[] getConstraints(Class<T> type) {
            return new IConstraint[0];
        }
    }

    <T> IConstraint<? super T>[] getConstraints(Class<T> type);
}
