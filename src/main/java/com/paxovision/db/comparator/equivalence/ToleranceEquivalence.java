package com.paxovision.db.comparator.equivalence;

public class ToleranceEquivalence implements Equivalence<Object> {
    private final String toleranceStr;

    public ToleranceEquivalence(String toleranceStr) {
        this.toleranceStr = toleranceStr;
    }

    private boolean doRelativeTolerance(double tolerance, Object a, Object b) {
        double numl = -1;
        double num2 = -2;

        if (a instanceof String) {
            numl = Double.valueOf(a.toString());
        } else if (a instanceof Number) {
            numl = ((Number) a).doubleValue();
        }

        if (b instanceof String) {
            num2 = Double.valueOf(b.toString());
        } else if (b instanceof Number) {
            num2 = ((Number) b).doubleValue();
        }

        if (numl == 0.0) {
            return num2 == 0.0;
        }

        double absDiff = Math.abs((numl - num2) / numl);
        return (absDiff <= tolerance);
    }

    private boolean doAbsoluteTolerance(double tolerance, Object a, Object b) {

        double numl = -1;
        double num2 = -2;
        if (a instanceof String) {
            numl = Double.valueOf(a.toString());
        } else if (a instanceof Number) {
            numl = ((Number) a).doubleValue();
        }
        if (b instanceof String) {
            num2 = Double.valueOf(b.toString());
        } else if (b instanceof Number) {
            num2 = ((Number) b).doubleValue();
        }

        double absDiff = Math.abs(numl - num2);

        if (absDiff <= tolerance) {
            return true;
        }
        return numl == num2;
    }

    public boolean doEquivalent(Object a, Object b) {

        if (!isEitherSideNull(a, b)) {
            if(toleranceStr.endsWith("%")) {
                    Double tolerance =
                            Double.valueOf(toleranceStr.substring(0, toleranceStr.length() - 1)) / 100;
                    return doRelativeTolerance(tolerance, a, b);
            }
           else {
                Double tolerance = Double.valueOf(toleranceStr);
                return doAbsoluteTolerance(tolerance, a, b);
            }
        }
        return false;
    }

}
