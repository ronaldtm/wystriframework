package org.wystriframework.core.wicket.bootstrap;

import java.io.Serializable;

import org.apache.wicket.Component;

public interface ITemplateProvider<CONFIG extends Serializable> extends Serializable {

    CharSequence getTemplate(CONFIG config, Component component);

}
