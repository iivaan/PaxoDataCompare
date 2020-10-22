package com.paxovision.db.comparator.equivalence;

import com.paxovision.db.exception.RaptorException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateEquivalence implements Equivalence<Object> {
    private final SimpleDateFormat baselineFormatter;
    private final SimpleDateFormat targetFormatter;

    public DateEquivalence(String baselinePattern, String targetPattern) {
        this.baselineFormatter = new SimpleDateFormat(baselinePattern);
        this.targetFormatter = new SimpleDateFormat(targetPattern);
    }

    public boolean doEquivalent(Object a, Object b) {
        if (!isEitherSideNull(a, b)) {
            if (a instanceof java.util.Date) {
                return a.equals(b);
            } else if (a instanceof String) {
                try {
                    Date baselineDate = baselineFormatter.parse(a.toString());
                    Date targetDate = targetFormatter.parse(b.toString());
                    return baselineDate.equals(targetDate);
                } catch (ParseException e) {
                    throw new RaptorException("Error while parsing", e);
                }
            }
        }
        return false;
    }

}
