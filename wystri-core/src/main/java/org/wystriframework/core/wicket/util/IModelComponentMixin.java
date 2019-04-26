package org.wystriframework.core.wicket.util;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

@SuppressWarnings("unchecked")
public interface IModelComponentMixin<T, C extends Component> {

    default IModel<T> getModel() {
        return (IModel<T>) ((Component) this).getDefaultModel();
    }

    default C setModel(IModel<T> model) {
        ((Component) this).setDefaultModel(model);
        return (C) this;
    }

    default T getModelObject() {
        return (T) ((Component) this).getDefaultModelObject();
    }

    default C setModelObject(T object) {
        ((Component) this).setDefaultModelObject(object);
        return (C) this;
    }
}
