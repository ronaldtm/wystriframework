package org.wystriframework.ui.util;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableBooleanSupplier;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.wystriframework.ui.component.behavior.AjaxFormComponentUpdatingActionBehavior;
import org.wystriframework.ui.component.behavior.EnabledIfBehavior;
import org.wystriframework.ui.component.behavior.VisibleIfBehavior;
import org.wystriframework.ui.component.jquery.JQuery;

public interface IBehaviorShortcutsMixin {
    static IBehaviorShortcutsMixin $b = BehaviorShortcutsImpl.$b;

    default AttributeModifier attrReplace(String name, IModel<?> valueModel) {
        return AttributeModifier.replace(name, valueModel);
    }
    default AttributeModifier attrReplace(String name, Serializable value) {
        return AttributeModifier.replace(name, value);
    }
    default AttributeAppender attrAppend(String name, IModel<?> valueModel) {
        return AttributeModifier.append(name, valueModel);
    }
    default AttributeAppender attrAppend(String name, Serializable value) {
        return AttributeModifier.append(name, value);
    }

    default Behavior onConfigure(SerializableConsumer<Component> callback) {
        return new Behavior() {
            @Override
            public void onConfigure(Component component) {
                callback.accept(component);
            }
        };
    }

    default VisibleIfBehavior visibleIf(SerializablePredicate<Component> predicate) {
        return new VisibleIfBehavior(predicate);
    }
    default VisibleIfBehavior visibleIf(IModel<Boolean> model) {
        return visibleIf(c -> model.getObject());
    }
    default VisibleIfBehavior visibleIf(SerializableBooleanSupplier test) {
        return visibleIf(c -> test.getAsBoolean());
    }
    default VisibleIfBehavior visibleIfModelObjectNotNull() {
        return visibleIf(c -> c.getDefaultModelObject() != null);
    }

    default EnabledIfBehavior enabledIf(SerializablePredicate<Component> predicate) {
        return new EnabledIfBehavior(predicate);
    }
    default EnabledIfBehavior enabledIf(IModel<Boolean> model) {
        return enabledIf(c -> model.getObject());
    }
    default EnabledIfBehavior enabledIf(SerializableBooleanSupplier test) {
        return enabledIf(c -> test.getAsBoolean());
    }

    default Behavior requiredIf(SerializableBooleanSupplier test) {
        return new Behavior() {
            @Override
            public void onConfigure(Component component) {
                ((FormComponent<?>) component).setRequired(test.getAsBoolean());
            }
        };
    }

    default AjaxFormComponentUpdatingActionBehavior ajaxUpdate(String event) {
        return new BehaviorShortcutsImpl.NonBlockingAjaxFormComponentUpdatingBehavior(event);
    }

    default Behavior refreshOnAjax() {
        return refreshOnAjax(c -> true);
    }
    default Behavior refreshOnAjax(SerializablePredicate<Component> active) {
        final class RefreshOnAjaxBehavior extends BehaviorShortcutsImpl.NonBlockingBehavior {
            @Override
            public void bind(Component component) {
                component
                    .setOutputMarkupId(true)
                    .setOutputMarkupPlaceholderTag(true);
            }
            @Override
            public void onEvent(Component component, IEvent<?> event) {
                if (event.getPayload() instanceof AjaxRequestTarget) {
                    if ((component.getParent() != null) && component.getParent().isVisibleInHierarchy())
                        ((AjaxRequestTarget) event.getPayload()).add(component);
                }
            }
            @Override
            public boolean isEnabled(Component component) {
                return active.test(component);
            }
        }
        return new RefreshOnAjaxBehavior();
    }

    default Behavior highlightIfNew() {
        return new Behavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                if (!component.hasBeenRendered())
                    JQuery.$(component).hide().show("highlight", 300)
                        .renderOnDomReady(response);
            }
            @Override
            public boolean isTemporary(Component component) {
                return true;
            }
        };
    }

    default Behavior recursiveSetOutputMarkupIdAndPlaceholder(boolean output) {
        return new Behavior() {
            @Override
            public void onConfigure(Component component) {
                component
                    .setOutputMarkupId(true)
                    .setOutputMarkupPlaceholderTag(true);
                if (component instanceof MarkupContainer)
                    ((MarkupContainer) component).streamChildren()
                        .filter(it -> !it.getRenderBodyOnly())
                        .forEach(it -> it
                            .setOutputMarkupId(true)
                            .setOutputMarkupPlaceholderTag(true));
            }
            @Override
            public boolean isTemporary(Component component) {
                return true;
            }
        };
    }

    default Behavior ready(SerializableSupplier<? extends CharSequence> script) {
        return ready(c -> script.get());
    }
    default Behavior ready(SerializableFunction<Component, ?> script) {
        return new Behavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                response.render(OnDomReadyHeaderItem.forScript(String.valueOf(script.apply(component))));
            }
        };
    }

    default Behavior renderHead(HeaderItem item) {
        return new Behavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                response.render(item);
            }
        };
    }
}

enum BehaviorShortcutsImpl implements IBehaviorShortcutsMixin {
    $b;

    static final class NonBlockingAjaxFormComponentUpdatingBehavior extends AjaxFormComponentUpdatingActionBehavior implements NullAjaxIndicatorAware {
        NonBlockingAjaxFormComponentUpdatingBehavior(String event) {
            super(event);
        }
    }
    static abstract class NonBlockingBehavior extends Behavior implements NullAjaxIndicatorAware {}
    static interface NullAjaxIndicatorAware extends IAjaxIndicatorAware {
        @Override
        default String getAjaxIndicatorMarkupId() {
            return null;
        }
    }
}