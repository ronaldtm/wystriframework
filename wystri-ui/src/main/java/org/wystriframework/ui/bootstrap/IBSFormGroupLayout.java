package org.wystriframework.ui.bootstrap;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.danekja.java.util.function.serializable.SerializableBiFunction;
import org.danekja.java.util.function.serializable.SerializableConsumer;

public interface IBSFormGroupLayout {

    BSFormLayoutConfig getLayoutConfig();
    
    BSFormGroup newFormGroup();

    IBSFormGroupLayout appendFormGroup(SerializableConsumer<BSFormGroup> callback);

    SerializableBiFunction<String, MarkupContainer, Component> getFeedbackComponentFactory();

}