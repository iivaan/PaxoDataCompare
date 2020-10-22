package com.paxovision.db.comparator.equivalence;

import com.paxovision.db.comparator.transform.SpringExpressionTransformation;

import java.util.Objects;

public class SpringExpressionEquivalence implements Equivalence<Object> {
    private SpringExpressionTransformation transformer;

    public SpringExpressionEquivalence(String expression) {
        Objects.requireNonNull(expression, "Expression cannot be null");
        this.transformer = new SpringExpressionTransformation(expression);
    }

    public void setDataMap(Object dataMap) {
        this.transformer.setDataMap(dataMap);
    }

    @Override
    public boolean doEquivalent(Object a, Object b) {
        if (!isEitherSideNull(a, b)) {
            return this.transformer.transform(a).equals(this.transformer.transform(b));
        }
        return false;
    }
}


