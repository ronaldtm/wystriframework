package org.wystriframework.core.wicket.component.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public enum CpfCnpjValidator implements IValidator<String> {
    CPF_OR_CNPJ((va, e, v) -> {
        if (v.length() == 11) {
            if (!isValidCPF(v))
                va.error(e);

        } else if (v.length() == 14) {
            if (!isValidCNPJ(v))
                va.error(e);

        } else {
            va.error(e);
        }
    }),
    CPF((va, e, v) -> {
        if (!isValidCPF(v))
            va.error(e);
    }),
    CNPJ((va, e, v) -> {
        if (!isValidCNPJ(v))
            va.error(e);
    });

    private final Callback callback;
    private CpfCnpjValidator(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        final String val = validatable.getValue().replaceAll("[^0-9]", "");

        final ValidationError error = new ValidationError(this)
            .setVariable("value", val);

        this.callback.validate(validatable, error, val);
    }

    public static boolean isValidCPF(String val) {
        if (!StringUtils.isNumeric(val) || (val.length() != 11) || allSameChar(val))
            return false;

        final String dv = val.substring(9, 11);
        final String calculatedDV = getDigitoVerificadorCPF(val);
        return StringUtils.equals(dv, calculatedDV);
    }

    public static String getDigitoVerificadorCPF(String val) {
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 0; i < 9; i++) {
            int n = Character.getNumericValue(val.charAt(i));
            sum1 += n * (10 - i);
            sum2 += n * (11 - i);
        }
        int dv1 = (11 - (sum1 % 11)) % 10;
        sum2 += dv1 * 2;
        int dv2 = (11 - (sum2 % 11)) % 10;

        return new StringBuilder(2).append(dv1).append(dv2).toString();
    }

    public static boolean isValidCNPJ(String val) {
        if (!StringUtils.isNumeric(val) || (val.length() != 14) || allSameChar(val))
            return false;

        final String dv = val.substring(12, 14);
        final String calculatedDV = getDigitoVerificadorCNPJ(val);

        return StringUtils.equals(dv, calculatedDV);
    }

    private static final int[] PESO_CNPJ = new int[] { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
    public static String getDigitoVerificadorCNPJ(String val) {
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 0; i < 12; i++) {
            int n = Character.getNumericValue(val.charAt(i));
            sum1 += n * PESO_CNPJ[i + 1];
            sum2 += n * PESO_CNPJ[i];
        }

        int dv1 = mod11CNPJ(sum1);
        sum2 += dv1 * PESO_CNPJ[12];
        int dv2 = mod11CNPJ(sum2);

        return new StringBuilder(2).append(dv1).append(dv2).toString();
    }

    private static int mod11CNPJ(int n) {
        int mod = (n % 11);
        return (mod < 2) ? 0 : (11 - mod);
    }

    private static boolean allSameChar(String s) {
        char ch = s.charAt(0);
        for (int i = 1; i < s.length(); i++)
            if (s.charAt(i) != ch)
                return false;
        return true;
    }

    private interface Callback {
        void validate(IValidatable<String> validatable, ValidationError error, String value);
    }
}
