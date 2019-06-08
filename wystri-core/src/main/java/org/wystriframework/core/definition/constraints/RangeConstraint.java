package org.wystriframework.core.definition.constraints;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wystriframework.core.definition.IConstrainable;
import org.wystriframework.core.definition.IConverter;
import org.wystriframework.core.wicket.WystriConfiguration;

import com.google.common.collect.Range;

public class RangeConstraint<T extends Comparable<T>> implements ISimpleConstraint<T> {

    private static final Pattern REGEX = Pattern.compile("^([\\[\\(])?\\s*([^\\[\\(]+)?\\.\\.([^\\]\\)]+)?\\s*([\\]\\)])?$");

    private String               rangeExpression;

    @Override
    public String checkSimple(IConstrainable<T> c) {
        final Range<T> range = (isNotBlank(rangeExpression))
            ? parseRangeExpression(rangeExpression, WystriConfiguration.get().getConverter(c.getType()))
            : Range.all();
        return range.contains(c.getValue()) ? null : "RangeConstraint.range";
    }

    public void setRangeExpression(String expr) {
        this.rangeExpression = expr;
    }

    public String getRangeExpression() {
        return rangeExpression;
    }

    public static <T extends Comparable<T>> Range<T> parseRangeExpression(String expr, IConverter<T> converter) {
        final Matcher m = REGEX.matcher(expr.replace("+∞", "").replace("-∞", "").trim());
        if (!m.matches())
            throw new IllegalArgumentException("Invalid range expression: '" + expr + "'");

        final String g1 = m.group(1);
        final String g2 = m.group(2);
        final String g3 = m.group(3);
        final String g4 = m.group(4);

        Range<T> newRange = Range.all();

        if (isNotBlank(g2)) {
            newRange = ("(".equals(g1))
                ? newRange.intersection(Range.greaterThan(converter.stringToObject(g2)))
                : newRange.intersection(Range.atLeast(converter.stringToObject(g2)));
        }
        if (isNotBlank(g3)) {
            newRange = (")".equals(g4))
                ? newRange.intersection(Range.lessThan(converter.stringToObject(g3)))
                : newRange.intersection(Range.atMost(converter.stringToObject(g3)));
        }
        return newRange;
    }

    public static void main(String[] args) {
        IConverter<Integer> intConverter = new IConverter<Integer>() {
            @Override
            public Integer stringToObject(String s) {
                return Integer.parseInt(s);
            }
            @Override
            public String objectToString(Integer o) {
                return (o == null) ? null : String.valueOf(o);
            }
        };

        System.out.println(parseRangeExpression("[1..10]", intConverter));
        System.out.println(parseRangeExpression("(1..10)", intConverter));
        System.out.println(parseRangeExpression("(1..)", intConverter));
        System.out.println(parseRangeExpression("(..10)", intConverter));

    }
}
