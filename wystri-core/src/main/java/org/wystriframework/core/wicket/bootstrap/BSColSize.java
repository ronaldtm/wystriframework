package org.wystriframework.core.wicket.bootstrap;

import static java.util.stream.Collectors.*;

import java.util.stream.Stream;

public interface BSColSize {
    BSResponsiveBreakpoint breakpoint();
    int number();
    String cssClass();

    //@formatter:off
    static BSColSize col       = BSColSizeDefaults.col      ;
    static BSColSize col_xs    = BSColSizeDefaults.col_xs   ;
    static BSColSize col_sm    = BSColSizeDefaults.col_sm   ;
    static BSColSize col_md    = BSColSizeDefaults.col_md   ;
    static BSColSize col_lg    = BSColSizeDefaults.col_lg   ;
    static BSColSize col_1     = BSColSizeDefaults.col_1    ;
    static BSColSize col_2     = BSColSizeDefaults.col_2    ;
    static BSColSize col_3     = BSColSizeDefaults.col_3    ;
    static BSColSize col_4     = BSColSizeDefaults.col_4    ;
    static BSColSize col_5     = BSColSizeDefaults.col_5    ;
    static BSColSize col_6     = BSColSizeDefaults.col_6    ;
    static BSColSize col_7     = BSColSizeDefaults.col_7    ;
    static BSColSize col_8     = BSColSizeDefaults.col_8    ;
    static BSColSize col_9     = BSColSizeDefaults.col_9    ;
    static BSColSize col_10    = BSColSizeDefaults.col_10   ;
    static BSColSize col_11    = BSColSizeDefaults.col_11   ;
    static BSColSize col_12    = BSColSizeDefaults.col_12   ;
    static BSColSize col_xs_1  = BSColSizeDefaults.col_xs_1 ;
    static BSColSize col_xs_2  = BSColSizeDefaults.col_xs_2 ;
    static BSColSize col_xs_3  = BSColSizeDefaults.col_xs_3 ;
    static BSColSize col_xs_4  = BSColSizeDefaults.col_xs_4 ;
    static BSColSize col_xs_5  = BSColSizeDefaults.col_xs_5 ;
    static BSColSize col_xs_6  = BSColSizeDefaults.col_xs_6 ;
    static BSColSize col_xs_7  = BSColSizeDefaults.col_xs_7 ;
    static BSColSize col_xs_8  = BSColSizeDefaults.col_xs_8 ;
    static BSColSize col_xs_9  = BSColSizeDefaults.col_xs_9 ;
    static BSColSize col_xs_10 = BSColSizeDefaults.col_xs_10;
    static BSColSize col_xs_11 = BSColSizeDefaults.col_xs_11;
    static BSColSize col_xs_12 = BSColSizeDefaults.col_xs_12;
    static BSColSize col_sm_1  = BSColSizeDefaults.col_sm_1 ;
    static BSColSize col_sm_2  = BSColSizeDefaults.col_sm_2 ;
    static BSColSize col_sm_3  = BSColSizeDefaults.col_sm_3 ;
    static BSColSize col_sm_4  = BSColSizeDefaults.col_sm_4 ;
    static BSColSize col_sm_5  = BSColSizeDefaults.col_sm_5 ;
    static BSColSize col_sm_6  = BSColSizeDefaults.col_sm_6 ;
    static BSColSize col_sm_7  = BSColSizeDefaults.col_sm_7 ;
    static BSColSize col_sm_8  = BSColSizeDefaults.col_sm_8 ;
    static BSColSize col_sm_9  = BSColSizeDefaults.col_sm_9 ;
    static BSColSize col_sm_10 = BSColSizeDefaults.col_sm_10;
    static BSColSize col_sm_11 = BSColSizeDefaults.col_sm_11;
    static BSColSize col_sm_12 = BSColSizeDefaults.col_sm_12;
    static BSColSize col_md_1  = BSColSizeDefaults.col_md_1 ;
    static BSColSize col_md_2  = BSColSizeDefaults.col_md_2 ;
    static BSColSize col_md_3  = BSColSizeDefaults.col_md_3 ;
    static BSColSize col_md_4  = BSColSizeDefaults.col_md_4 ;
    static BSColSize col_md_5  = BSColSizeDefaults.col_md_5 ;
    static BSColSize col_md_6  = BSColSizeDefaults.col_md_6 ;
    static BSColSize col_md_7  = BSColSizeDefaults.col_md_7 ;
    static BSColSize col_md_8  = BSColSizeDefaults.col_md_8 ;
    static BSColSize col_md_9  = BSColSizeDefaults.col_md_9 ;
    static BSColSize col_md_10 = BSColSizeDefaults.col_md_10;
    static BSColSize col_md_11 = BSColSizeDefaults.col_md_11;
    static BSColSize col_md_12 = BSColSizeDefaults.col_md_12;
    static BSColSize col_lg_1  = BSColSizeDefaults.col_lg_1 ;
    static BSColSize col_lg_2  = BSColSizeDefaults.col_lg_2 ;
    static BSColSize col_lg_3  = BSColSizeDefaults.col_lg_3 ;
    static BSColSize col_lg_4  = BSColSizeDefaults.col_lg_4 ;
    static BSColSize col_lg_5  = BSColSizeDefaults.col_lg_5 ;
    static BSColSize col_lg_6  = BSColSizeDefaults.col_lg_6 ;
    static BSColSize col_lg_7  = BSColSizeDefaults.col_lg_7 ;
    static BSColSize col_lg_8  = BSColSizeDefaults.col_lg_8 ;
    static BSColSize col_lg_9  = BSColSizeDefaults.col_lg_9 ;
    static BSColSize col_lg_10 = BSColSizeDefaults.col_lg_10;
    static BSColSize col_lg_11 = BSColSizeDefaults.col_lg_11;
    static BSColSize col_lg_12 = BSColSizeDefaults.col_lg_12;
    //@formatter:on
}

enum BSColSizeDefaults implements BSColSize {

    col,

    col_xs,
    col_sm,
    col_md,
    col_lg,

    col_1,
    col_2,
    col_3,
    col_4,
    col_5,
    col_6,
    col_7,
    col_8,
    col_9,
    col_10,
    col_11,
    col_12,

    col_xs_1,
    col_xs_2,
    col_xs_3,
    col_xs_4,
    col_xs_5,
    col_xs_6,
    col_xs_7,
    col_xs_8,
    col_xs_9,
    col_xs_10,
    col_xs_11,
    col_xs_12,

    col_sm_1,
    col_sm_2,
    col_sm_3,
    col_sm_4,
    col_sm_5,
    col_sm_6,
    col_sm_7,
    col_sm_8,
    col_sm_9,
    col_sm_10,
    col_sm_11,
    col_sm_12,

    col_md_1,
    col_md_2,
    col_md_3,
    col_md_4,
    col_md_5,
    col_md_6,
    col_md_7,
    col_md_8,
    col_md_9,
    col_md_10,
    col_md_11,
    col_md_12,

    col_lg_1,
    col_lg_2,
    col_lg_3,
    col_lg_4,
    col_lg_5,
    col_lg_6,
    col_lg_7,
    col_lg_8,
    col_lg_9,
    col_lg_10,
    col_lg_11,
    col_lg_12,

    ;
    private final BSResponsiveBreakpoint breakpoint;
    private final int                    number;
    private final String                 cssClass;

    private BSColSizeDefaults() {
        final String[] s = name().split("_");

        this.cssClass = Stream.of(s).collect(joining("-"));

        if (s.length == 1) {
            this.breakpoint = BSResponsiveBreakpoint.NONE;
            this.number = 0;
        } else if (s.length == 2) {
            final String s1 = s[1];
            if (Character.isDigit(s1.charAt(0))) {
                this.breakpoint = BSResponsiveBreakpoint.NONE;
                this.number = Integer.parseInt(s1);
            } else {
                this.breakpoint = BSResponsiveBreakpoint.valueOf(s1);
                this.number = 0;
            }
        } else {
            this.breakpoint = BSResponsiveBreakpoint.valueOf(s[1]);
            this.number = Integer.parseInt(s[2]);
        }
    }

    @Override
    public BSResponsiveBreakpoint breakpoint() {
        return breakpoint;
    }
    @Override
    public int number() {
        return number;
    }
    @Override
    public String cssClass() {
        return cssClass;
    }
    
    @Override
    public String toString() {
        return cssClass();
    }
}