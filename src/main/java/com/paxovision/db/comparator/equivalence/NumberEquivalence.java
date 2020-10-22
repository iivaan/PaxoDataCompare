package com.paxovision.db.comparator.equivalence;

import org.apache.commons.lang3.StringUtils;

public class NumberEquivalence implements Equivalence<Object> {

    public boolean doEquivalent(Object a, Object b) {
        if (!isEitherSideNull(a, b)) {
            if (a instanceof Number && b instanceof Number) {
                // had to do this some time compare using equals not working , compare as primitives
                return ((Number) a).doubleValue() == ((Number) b).doubleValue();
            } else if (a instanceof String || b instanceof String) {
                if (StringUtils.isBlank(a.toString()) || StringUtils.isBlank(b.toString())) {
                    return false;
                }
                Double numl = Double.parseDouble(a.toString());
                Double num2 = Double.parseDouble(b.toString());
                return numl.doubleValue() == num2.doubleValue();
            }
        }
        return false;
    }
}
