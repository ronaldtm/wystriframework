package org.wystriframework.ui.component.form;

import java.util.Locale;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.wystriframework.ui.component.behavior.textmask.TextMaskBehavior;
import org.wystriframework.ui.component.validator.CpfCnpjValidator;

public class CpfTextField extends TextField<String> {

    public CpfTextField(String id, IModel<String> model) {
        super(id, model, String.class);
    }

    public CpfTextField(String id) {
        super(id, String.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(CpfCnpjValidator.CPF);
        add(TextMaskBehavior.cpf());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> IConverter<C> getConverter(Class<C> type) {
        return (IConverter<C>) new IConverter<String>() {
            @Override
            public String convertToObject(String value, Locale locale) throws ConversionException {
                return value.replaceAll("[^0-9]", "");
            }
            @Override
            public String convertToString(String value, Locale locale) {
                return value;
            }
        };
    }
}
