package org.wystriframework.ui.util;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.danekja.java.util.function.serializable.SerializableFunction;

public abstract class FeedbackMessageUtils {
    private FeedbackMessageUtils() {}

    public static Behavior keepMessage(int level, SerializableFunction<Component, String> msg) {
        return new Behavior() {
            @Override
            public void onConfigure(Component component) {
                component.getFeedbackMessages().add(component, msg.apply(component), level);
            }
        };
    }
}
