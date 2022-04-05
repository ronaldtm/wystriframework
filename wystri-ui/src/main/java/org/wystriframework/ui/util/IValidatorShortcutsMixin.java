package org.wystriframework.ui.util;

import static org.apache.commons.lang3.StringUtils.*;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.RangeValidator;

public interface IValidatorShortcutsMixin {
    static IValidatorShortcutsMixin $v = ValidatorShortcutsImpl.$v;

    @SuppressWarnings("serial")
    default IFormValidator notAllBlank(String msg, FormComponent<?>... components) {
        return new IFormValidator() {
            @Override
            public void validate(Form<?> form) {
                if (allInputBlank())
                    form.error("Preencha pelo menos o Nome do contratado, CPF/CNPJ ou o Nº do documento.");
            }
            public boolean allInputBlank() {
                for (FormComponent<?> component : getDependentFormComponents())
                    if (isNotBlank(component.getInput()))
                        return false;
                return true;
            }
            @Override
            public FormComponent<?>[] getDependentFormComponents() {
                return components;
            }
        };
    }

    default RangeValidator<Integer> range(int min, int max) {
        return RangeValidator.range(min, max);
    }

    @SuppressWarnings("serial")
    default IValidator<Boolean> checkRequired(String msg) {
        return new IValidator<Boolean>() {
            @Override
            public void validate(IValidatable<Boolean> validatable) {
                if (!Boolean.TRUE.equals(validatable.getValue()))
                    validatable.error(new ValidationError(msg));
            }
        };
    }
}

enum ValidatorShortcutsImpl implements IValidatorShortcutsMixin {
    $v;
}