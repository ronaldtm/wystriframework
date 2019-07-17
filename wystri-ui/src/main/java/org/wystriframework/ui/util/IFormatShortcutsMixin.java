package org.wystriframework.ui.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public interface IFormatShortcutsMixin {
    static IFormatShortcutsMixin $f = FormatShortcutsImpl.$f;

    default String ddMMyyyy(LocalDate date) {
        return (date == null) ? "" : FormatShortcutsImpl.ddMMyyyy.get().format(date);
    }

    default String MMyyyy(YearMonth yearMonth) {
        return (yearMonth == null) ? "" : yearMonth.format(FormatShortcutsImpl.MMyyyy.get());
    }
    default String yyyyMM(YearMonth yearMonth) {
        return (yearMonth == null) ? "" : yearMonth.format(FormatShortcutsImpl.yyyyMM.get());
    }
}

enum FormatShortcutsImpl implements IFormatShortcutsMixin {
    $f;

    static ThreadLocal<DateTimeFormatter> ddMMyyyy = ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    static ThreadLocal<DateTimeFormatter> MMyyyy   = ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern("MM/yyyy"));
    static ThreadLocal<DateTimeFormatter> yyyyMM   = ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern("yyyy-MM"));

}