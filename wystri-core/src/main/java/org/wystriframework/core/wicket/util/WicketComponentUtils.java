package org.wystriframework.core.wicket.util;

import static java.util.Arrays.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

public abstract class WicketComponentUtils {
    private WicketComponentUtils() {}

    public static void appendCssClasses(ComponentTag tag, String... newValues) {
        appendDistinctValueToAttribute(tag, "class", ' ', newValues);
    }

    public static void appendCssClasses(ComponentTag tag, Collection<String> newValues) {
        appendDistinctValueToAttribute(tag, "class", ' ', newValues);
    }

    public static void appendDistinctValueToAttribute(ComponentTag tag, String attributeName, char separator, String... newValues) {
        appendDistinctValueToAttribute(tag, attributeName, separator, asList(newValues));
    }

    public static void appendDistinctValueToAttribute(ComponentTag tag, String attributeName, char separator, Collection<String> newValues) {
        final String oldAttributeValue = defaultString(tag.getAttribute(attributeName));
        final Set<String> valueSet = Sets.newLinkedHashSet(asList(StringUtils.split(oldAttributeValue, separator)));
        valueSet.addAll(newValues);
        valueSet.remove(null);
        tag.put(attributeName, Joiner.on(separator).join(valueSet));
    }

    public static void appendValueToAttribute(ComponentTag tag, String attributeName, String newValue) {
        tag.put(attributeName, defaultString(tag.getAttribute(attributeName)) + newValue);
    }

    public static JavaScriptReferenceHeaderItem jsRefHeader(final Class<?> baseClass, String path) {
        return JavaScriptReferenceHeaderItem.forReference(new JavaScriptResourceReference(baseClass, path));
    }
}
