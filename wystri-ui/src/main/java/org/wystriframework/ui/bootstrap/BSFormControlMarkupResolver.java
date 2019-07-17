package org.wystriframework.ui.bootstrap;

import static org.apache.commons.lang3.ObjectUtils.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.wystriframework.core.util.UncheckedAppendable;

import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

public class BSFormControlMarkupResolver implements Serializable {

    private static final MetaDataKey<ITemplateProvider<BSFormLayoutConfig>> TEMPLATE_PROVIDER_KEY  = new MetaDataKey<ITemplateProvider<BSFormLayoutConfig>>() {};
    private static final MetaDataKey<SerializableSupplier<String>>           CSS_CLASS_PROVIDER_KEY = new MetaDataKey<SerializableSupplier<String>>() {};
    private static final MetaDataKey<String>                                 INPUT_TYPE_KEY         = new MetaDataKey<String>() {};

    public void appendFieldMarkup(UncheckedAppendable ta, BSFormLayoutConfig config, Component child) {
        final Map<String, Object> params = ImmutableMap.<String, Object> builder()
            .put("id", child.getId())
            .put("inputType", defaultIfNull(child.getMetaData(INPUT_TYPE_KEY), "text"))
            .put("cssClasses", defaultString(resolve(child.getMetaData(CSS_CLASS_PROVIDER_KEY))))
            .build();

        final String markup = getMarkup(config, child, params);

        ta.append(markup);
    }

    private String getMarkup(BSFormLayoutConfig config, Component child, final Map<String, Object> params) {
        final ITemplateProvider<BSFormLayoutConfig> templateProvider = child.getMetaData(TEMPLATE_PROVIDER_KEY);
        final String templateString = getTemplateString(config, child, templateProvider);
        final Template template = createTemplate(config, templateString);

        return template.execute(params);
    }

    protected Template createTemplate(BSFormLayoutConfig config, String templateString) {
        return Mustache.compiler()
            .defaultValue("")
            .emptyStringIsFalse(true)
            .zeroIsFalse(true)
            .withFormatter(config::format)
            .compile(templateString);
    }

    protected String getTemplateString(BSFormLayoutConfig config, Component child, final ITemplateProvider<BSFormLayoutConfig> templateProvider) {
        final BSSize size = config.getComponentsSize();

        if (templateProvider != null) {
            return templateProvider.getTemplate(config, child).toString();

        } else if (child instanceof IFormSubmittingComponent) {
            final IFormSubmittingComponent btn = (IFormSubmittingComponent) child;
            final String btnType = (btn.getDefaultFormProcessing()) ? "submit" : "button";
            final String btnCss = (!btn.getDefaultFormProcessing()) ? "btn-link"
                : (btn.getForm().getDefaultButton() == btn) ? "btn-primary"
                    : "btn-secondary";
            return "<button wicket:id='{{id}}' "
                + " type='" + btnType + "' "
                + " class='btn " + btnCss + " " + size.prefixWith("btn") + " {{cssClasses}}'></button>";

        } else if (child instanceof AbstractLink) {
            return "<a wicket:id='{{id}}' class='btn btn-link" + size.prefixWith("btn") + " {{cssClasses}}'></a>";

        } else if (child instanceof Label) {
            return "<span wicket:id='{{id}}' class='form-control-plaintext " + size.prefixWith("form-control") + " {{cssClasses}}'></span>";

        } else if (child instanceof DropDownChoice<?>) {
            return "<select wicket:id='{{id}}' class='form-control " + size.prefixWith("form-control") + " {{cssClasses}}'></select>";

        } else if (child instanceof CheckBox) {
            return "<input wicket:id='{{id}}' type='checkbox' class='form-check-input {{cssClasses}}'/>";

        } else if (child instanceof FormComponent<?> && !(child instanceof FormComponentPanel<?>)) {
            return "<input wicket:id='{{id}}' type='{{inputType}}' class='form-control " + size.prefixWith("form-control") + " {{cssClasses}}'/>";

        } else {
            return "<div wicket:id='{{id}}' class='{{cssClasses}}'></div>";
        }
    }

    private static <T> T resolve(Supplier<T> supplier) {
        return (supplier != null ? supplier.get() : null);
    }
}
