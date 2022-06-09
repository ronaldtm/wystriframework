package org.wystriframework.ui.component.behavior.textmask;

import static org.apache.wicket.markup.head.JavaScriptHeaderItem.*;
import static org.wystriframework.ui.component.jquery.JQuery.*;
import static org.wystriframework.ui.util.WicketComponentUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Range;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

import com.github.openjson.JSONObject;
import com.google.common.base.Joiner;

public class TextMaskBehavior extends Behavior {

    private boolean extraOnlyIfEnabled = false;

    @Override
    public final void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);

        response.render(forReference(component.getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
        response.render(jsRefHeader(TextMaskBehavior.class, "js/vanillaTextMask/vanillaTextMask.js"));
        response.render(jsRefHeader(TextMaskBehavior.class, "js/textMaskAddons/createAutoCorrectedDatePipe.js"));
        response.render(jsRefHeader(TextMaskBehavior.class, "js/textMaskAddons/createNumberMask.js"));
        response.render(jsRefHeader(TextMaskBehavior.class, "js/textMaskAddons/emailMask.js"));
        response.render(jsRefHeader(TextMaskBehavior.class, "js/jquery.textMask.js"));
        response.render(jsRefHeader(TextMaskBehavior.class, TextMaskBehavior.class.getSimpleName() + ".js"));

        extraRenderHead(component, response);
    }

    protected void extraRenderHead(Component component, IHeaderResponse response) {}

    public TextMaskBehavior setExtraOnlyIfEnabled(boolean extraOnlyIfEnabled) {
        this.extraOnlyIfEnabled = extraOnlyIfEnabled;
        return this;
    }

    @Override
    public boolean isEnabled(Component component) {
        return !extraOnlyIfEnabled || component.isEnabledInHierarchy();
    }

    public static TextMaskBehavior cpfCnpj() {
        return new TextMaskBehavior() {
            @Override
            protected void extraRenderHead(Component component, IHeaderResponse response) {
                response.render(OnDomReadyHeaderItem
                    .forScript(String.format("TextMaskBehavior.cpfCnpj(%s);",
                        $(component))));
            }
        };
    }

	public static TextMaskBehavior cpf() {
        return new TextMaskBehavior() {
            @Override
            protected void extraRenderHead(Component component, IHeaderResponse response) {
                response.render(OnDomReadyHeaderItem
                    .forScript(String.format("TextMaskBehavior.cpf(%s);",
                        $(component))));
            }
        };
    }

    public static TextMaskBehavior money(String currencySymbol) {
        return money(currencySymbol, true);
    }

    public static TextMaskBehavior money(String currencySymbol, boolean showSymbol) {
        return money(currencySymbol, showSymbol, false);
    }

	public static TextMaskBehavior money(String currencySymbol, boolean showSymbol, boolean allowNegative) {
        return new TextMaskBehavior() {
            @Override
            protected void extraRenderHead(Component component, IHeaderResponse response) {
                final JSONObject opts = new JSONObject();
                if (showSymbol)
                    opts.put("prefix", currencySymbol);
                if (allowNegative)
                    opts.put("allowNegative", true);

                response.render(OnDomReadyHeaderItem
                    .forScript(String.format("TextMaskBehavior.money(%s, %s);",
                        $(component),
                        opts.toString(3))));
            }
        };
    }

    public static TextMaskBehavior ddmmyyyyYearRange(Range<Integer> range) {
        return ddmmyyyyYearRange(range.getMinimum(), range.getMaximum());
    }
    
    public static TextMaskBehavior ddmmyyyyYearRange(Integer minYear, Integer maxYear) {
        return new TextMaskBehavior() {
            @Override
            protected void extraRenderHead(Component component, IHeaderResponse response) {
                List<String> opts = new ArrayList<>();
                if (minYear != null)
                    opts.add("minYear: " + minYear);
                if (maxYear != null)
                    opts.add("maxYear: " + maxYear);
                response.render(OnDomReadyHeaderItem
                    .forScript(String.format("TextMaskBehavior.ddmmyyyy(%s, { " + Joiner.on(',').join(opts) + " });",
                        $(component))));
            }
        };
    }

    public static TextMaskBehavior ddmmyyyy() {
        return new TextMaskBehavior() {
            @Override
            protected void extraRenderHead(Component component, IHeaderResponse response) {
                response.render(OnDomReadyHeaderItem
                    .forScript(String.format("TextMaskBehavior.ddmmyyyy(%s, { });",
                        $(component))));
            }
        };
    }

    public static TextMaskBehavior conta1DV() {
        return new TextMaskBehavior() {
            @Override
            protected void extraRenderHead(Component component, IHeaderResponse response) {
                response.render(OnDomReadyHeaderItem
                    .forScript(String.format("TextMaskBehavior.conta1DV(%s, { });",
                        $(component))));
            }
        };
    }

}
