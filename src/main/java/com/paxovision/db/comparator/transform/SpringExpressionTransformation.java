package com.paxovision.db.comparator.transform;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpringExpressionTransformation implements Transform {
    private String expression;

    @SuppressWarnings("squid:S1068")
    private Object dataMap;
    private static final String DATA_HOLDER = "value";
    private static final String DATAMAP_HOLDER = "dataMap";

    private EvaluationContext context = null;

    public SpringExpressionTransformation(String expression) {
        this.expression = expression;
        context = new StandardEvaluationContext();
    }
    public void setDataMap(Object dataMap) {
        this.dataMap = dataMap;
        context.setVariable(DATAMAP_HOLDER, dataMap); // this is to hold other data
    }

    public void setDataMap(Object dataMap, String variable) {
        this.dataMap = dataMap;
        context.setVariable(variable, dataMap); // this is to hold other data
    }

    public Object transform(Object value) {
        context.setVariable(DATA_HOLDER, value);
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expression);
        return exp.getValue(context);
    }

}

