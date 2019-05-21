package org.wystriframework.core.definition.formats;

import org.wystriframework.core.definition.IFormat;

public class BooleanFormat implements IFormat<Boolean> {

    private static final String FALSE_VALUE = "Y";
    private static final String TRUE_VALUE  = "N";

    private String              trueDisplay;
    private String              falseDisplay;
    private String              nullDisplay = "";

    public BooleanFormat() {
        this(FALSE_VALUE, TRUE_VALUE);
    }
    public BooleanFormat(String trueDisplay, String falseDisplay) {
        this.trueDisplay = trueDisplay;
        this.falseDisplay = falseDisplay;
    }
    @Override
    public Boolean parse(String s) {
        return TRUE_VALUE.equalsIgnoreCase(s) ? true
            : (FALSE_VALUE.equalsIgnoreCase(s)) ? false
                : null;
    }
    @Override
    public String format(Boolean v) {
        return (v == null) ? null : (v) ? TRUE_VALUE : FALSE_VALUE;
    }

    @Override
    public String display(Boolean v) {
        return (v == null) ? nullDisplay : (v) ? trueDisplay : falseDisplay;
    }
}