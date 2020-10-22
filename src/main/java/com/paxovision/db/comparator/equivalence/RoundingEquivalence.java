package com.paxovision.db.comparator.equivalence;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundingEquivalence implements Equivalence<Object> {

    private int scale;

    public RoundingEquivalence(int scale) {
        this.scale = scale;
    }

    private BigDecimal convertToBigDecimal(Object o) {
        if (o instanceof String) {
            return new BigDecimal((String) o);
        } else if (o instanceof Double) {
            return BigDecimal.valueOf((Double) o);
        } else if (o instanceof Float) {
            return BigDecimal.valueOf((Float) o);
        } else if (o instanceof Number) {
            return BigDecimal.valueOf(((Number) o).doubleValue());
        } else {
            throw new IllegalArgumentException("Not number");
        }
    }

    public boolean doEquivalent(Object a, Object b) {
        if (!isEitherSideNull(a, b)) {
            BigDecimal bdl = convertToBigDecimal(a);
            BigDecimal bd2 = convertToBigDecimal(b);
            BigDecimal bd3 = bdl.setScale(scale, RoundingMode.HALF_UP);
            BigDecimal bd4 = bd2.setScale(scale, RoundingMode.HALF_UP);
            return bd3.equals(bd4);
        }
        return false;
    }
}