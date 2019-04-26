package org.wystriframework.core.wicket.util;

import static java.util.stream.Collectors.*;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.danekja.java.misc.serializable.SerializableRunnable;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.wicketstuff.lazymodel.LazyModel;
import org.wystriframework.core.wicket.component.model.NullModel;

public interface IModelShortcutsMixin {
    static IModelShortcutsMixin $m = ModelShortcutsImpl.$m;

    default <T> List<IModel<T>> listOfModels(List<T> list, Function<T, ? extends IModel<T>> function) {
        return list.stream().map(it -> function.apply(it)).collect(toList());
    }

    default <T extends Serializable> List<IModel<T>> listOfModels(List<T> list) {
        return listOfModels(list, Model::of);
    }

    default <ROOT, T> LazyModel<T> lazy(IModel<ROOT> root, Class<ROOT> rootType, SerializableFunction<ROOT, T> func) {
        return LazyModel.model(func.apply(LazyModel.from(root, rootType)));
    }

    default <ROOT, T> LazyModel<T> lazy(IModel<ROOT> root, SerializableFunction<ROOT, T> func) {
        return LazyModel.model(func.apply(LazyModel.from(root)));
    }

    default <ROOT, T> LazyModel<T> lazyFrom(ROOT root, SerializableFunction<ROOT, T> func) {
        return LazyModel.model(func.apply(LazyModel.from(root)));
    }

    default <T> IModel<T> ofNull(Class<T> type) {
        return new NullModel<>(type);
    }

    default <T> LoadableDetachableModel<T> loadable(SerializableSupplier<T> getter) {
        return LoadableDetachableModel.of(getter);
    }

    default <T, U> LoadableDetachableModel<U> loadable(IModel<T> root, SerializableFunction<T, U> map) {
        return LoadableDetachableModel.of(() -> map.apply(root.getObject()));
    }

    default IModel<Boolean> not(IModel<Boolean> model) {
        return model.map(it -> !Boolean.TRUE.equals(model.getObject()));
    }

    default IModel<Boolean> or(IModel<Boolean> a, IModel<Boolean> b) {
        return () -> Boolean.TRUE.equals(a.getObject()) || Boolean.TRUE.equals(b.getObject());
    }

    default IModel<Boolean> and(IModel<Boolean> a, IModel<Boolean> b) {
        return () -> Boolean.TRUE.equals(a.getObject()) && Boolean.TRUE.equals(b.getObject());
    }

    default <T> IModel<T> getSet(SerializableSupplier<T> getter, SerializableConsumer<T> setter) {
        return getSet(getter, setter, () -> {});
    }
    default <T> IModel<T> getSet(SerializableSupplier<T> getter, SerializableConsumer<T> setter, SerializableRunnable detach) {
        return new IModel<T>() {
            @Override
            public T getObject() {
                return getter.get();
            }
            @Override
            public void setObject(T object) {
                setter.accept(object);
            }
            @Override
            public void detach() {
                detach.run();
            }
        };
    }
}

enum ModelShortcutsImpl implements IModelShortcutsMixin {
    $m;
}