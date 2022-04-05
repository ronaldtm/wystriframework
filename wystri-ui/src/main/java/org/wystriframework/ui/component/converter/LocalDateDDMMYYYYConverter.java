package org.wystriframework.ui.component.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

@SuppressWarnings("serial")
public class LocalDateDDMMYYYYConverter implements IConverter<LocalDate> {

    private static final DateTimeFormatter ddMMyyyy = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public LocalDate convertToObject(String value, Locale locale) throws ConversionException {
        try {
            return parse(value);
        } catch (UnsupportedOperationException | IllegalArgumentException ex) {
            throw new ConversionException(ex.getMessage());
        }
    }

    public static LocalDate parse(String value) {
        return LocalDate.parse(value, ddMMyyyy);
    }

    @Override
    public String convertToString(LocalDate value, Locale locale) {
        return value.format(ddMMyyyy);
    }

}
