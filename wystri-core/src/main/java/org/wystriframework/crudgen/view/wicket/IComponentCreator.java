package org.wystriframework.crudgen.view.wicket;

import org.apache.wicket.Component;

public interface IComponentCreator<C extends Component> {
    public C create(String id);
}
