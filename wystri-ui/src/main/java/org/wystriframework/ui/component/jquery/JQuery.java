package org.wystriframework.ui.component.jquery;

import static java.util.stream.Collectors.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Stream;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

import com.github.openjson.JSONObject;
import com.google.common.base.Joiner;

public final class JQuery implements Serializable, CharSequence {

    //private StringBuilder buffer = new StringBuilder();
    private final JQuery base;
    private final String segment;
    private final int    depth;

    private JQuery(String selector) {
        this(null, "$(" + selector + ")");
    }

    private JQuery(JQuery base, String segment) {
        this.base = base;
        this.segment = segment;
        this.depth = (base == null) ? 1 : base.depth + 1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    //@formatter:off
    public JQuery appendRaw(CharSequence rawString)       { return doAppend(rawString);                                          }
    public JQuery attr(String name, String value)         { return doAppend(".attr('" + jse(name) + "', '" + jse(value) + "')"); }
    public JQuery find(String selector)                   { return doAppend(".find('" + jse(selector) + "')");                   }
    public JQuery addClass(String... classNames)          { return doAppend(".addClass('" + classList(classNames) + "')");       }
    public JQuery removeClass(String... classNames)       { return doAppend(".removeClass('" + classList(classNames) + "')");    }
    public JQuery remove()                                { return doAppend(".remove()");                                        }
    public JQuery end()                                   { return doAppend(".end()");                                           }
    public JQuery hide()                                  { return doAppend(".hide()");                                          }
    public JQuery show(String effect, long duration)      { return doAppend(".show('" + effect + "', " + duration + ")");        }
    public JQuery each(String function)                   { return doAppend(".each(" + function + ")");                          }
    public JQuery popover(JSONObject options)             { return doAppend(".popover(" + options + ")");                        }
    public JQuery on(String evt, CharSequence function)   { return doAppend(".on('" + evt + "', " + function + ")");             }
    public JQuery one(String evt, CharSequence function)  { return doAppend(".one('" + evt + "', " + function + ")");            }
    public JQuery trigger(String evt)                     { return doAppend(".trigger('" + evt + "')");                          }

    public JQuery popover()                               { return popover(new JSONObject().put("trigger", "focus"));            }
    
    
    //@formatter:on

    private JQuery doAppend(CharSequence segment) {
        //buffer.append(segment);
        return new JQuery(this, segment.toString());
    }

    private Stream<CharSequence> segmentStream() {
        return queryStream().map(it -> it.segment);
    }

    private Stream<JQuery> queryStream() {
        final JQuery[] array = new JQuery[this.depth];
        JQuery q = this;
        for (int i = array.length - 1; (i >= 0) && (q != null); i--) {
            array[i] = q;
            q = q.base;
        }
        return Stream.of(array);
    }

    private int segmentLength() {
        return segment.length();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    public void appendTo(AjaxRequestTarget target) {
        if (target != null)
            target.appendJavaScript(this.toString());
    }
    public void prependTo(AjaxRequestTarget target) {
        if (target != null)
            target.prependJavaScript(this.toString());
    }
    public void renderOnDomReady(IHeaderResponse response) {
        response.render(OnDomReadyHeaderItem.forScript(this.toString()));
    }
    public Behavior onDomReady() {
        return new Behavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                JQuery.this.renderOnDomReady(response);
            }
        };
    }
    public String asSetTimeoutCall(long delay) {
        return String.format("setTimeout(function(){%s;}, %d)", this, delay);
    }
    public String asSetTimeoutFunction(long delay) {
        return String.format("(function(){ %s })", asSetTimeoutCall(delay));
    }
    public String asFunction() {
        return String.format("(function(){ %s })", this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return segmentStream().collect(joining());
    }
    @Override
    public int length() {
        return queryStream().collect(summingInt(it -> it.segmentLength()));
    }
    @Override
    public char charAt(int index) {
        return toString().charAt(index);
    }
    @Override
    public CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    public static JQuery $(Stream<? extends Component> components) {
        return new JQuery(components
            .map(it -> it.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true))
            .map(it -> "#" + it.getMarkupId())
            .collect(joining(", ", "'", "'")));
    }
    public static JQuery $(Component first, Component... other) {
        return $(Stream.concat(Stream.of(first), Stream.of(other)));
    }
    public static JQuery $(Collection<? extends Component> comp) {
        return $(comp.stream());
    }
    public static JQuery $document() {
        return new JQuery("document");
    }
    public static JQuery $this() {
        return new JQuery("this");
    }
    public static JQuery $(String selector) {
        return new JQuery("'" + jse(selector) + "'");
    }

    public static String scrollTo(Component c) {
        return ""
            + "var $scrollTo = " + JQuery.$(c) + ";"
            + "var $container = $('html,body');"
            + "var top = $scrollTo.offset().top - $container.offset().top + $container.scrollTop();"
            + "$container.animate({scrollTop:top});"
            + "setTimeout(function(){"
            + "  $scrollTo.find('input[type=text]:first:visible').each(function(){ this.focus();});"
            + "},50);";
    }
    private static CharSequence jse(CharSequence s) {
        return JavaScriptUtils.escapeQuotes(s);
    }
    private static CharSequence classList(String... classNames) {
        return jse(Joiner.on(" ").join(classNames));
    }

}
