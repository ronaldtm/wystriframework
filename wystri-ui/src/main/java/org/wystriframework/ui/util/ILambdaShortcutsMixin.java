package org.wystriframework.ui.util;

import static java.util.stream.Collectors.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.danekja.java.util.function.serializable.SerializableFunction;

public interface ILambdaShortcutsMixin {

    static ILambdaShortcutsMixin $L = LambdaShortcutsImpl.$L;

    default <T> SerializableFunction<T, String> simNao(SerializableFunction<T, Boolean> boolFunc) {
        return simNao(boolFunc, "Sim", "Não", "");
    }
    default <T> SerializableFunction<T, String> simNao(SerializableFunction<T, Boolean> boolFunc, String sim, String nao, String nil) {
        return it -> {
            Boolean bool = boolFunc.apply(it);
            return (bool == null) ? nil
                : (bool) ? sim : nao;
        };
    }

    default <T, U> String join(Collection<String> list, CharSequence separator) {
        return (list == null) ? null : list.stream().collect(joining(separator));
    }
    default <T, U> String join(Collection<String> list, CharSequence separator, CharSequence prefix, CharSequence suffix) {
        return (list == null) ? null : list.stream().collect(joining(separator, prefix, suffix));
    }
    default <T, U> Set<U> toSet(Stream<T> stream, Function<T, U> function) {
        return stream.map(function).collect(Collectors.toSet());
    }
    default <T, U> Set<U> toSet(Collection<T> original, Function<T, U> function) {
        return toSet(original.stream(), function);
    }
    default <T, U> List<U> toList(Stream<T> stream, Function<T, U> function) {
        return stream.map(function).collect(Collectors.toList());
    }
    default <T, U> List<U> toList(Collection<T> original, Function<T, U> function) {
        return toList(original.stream(), function);
    }
    default <T> List<T> filter(List<T> original, Predicate<T> filter) {
        return filter(original.stream(), filter);
    }
    default <T> List<T> filter(Stream<T> original, Predicate<T> filter) {
        return original.filter(filter).collect(Collectors.toList());
    }
    default <T> SerializableConsumer<T> noopConsumer() {
        return it -> {};
    }
    default <T, U> SerializableBiConsumer<T, U> noopBiConsumer() {
        return (a, b) -> {};
    }
    default <T> SerializableFunction<T, T> identity() {
        return SerializableFunction.identity();
    }
}

enum LambdaShortcutsImpl implements ILambdaShortcutsMixin {
    $L;
}