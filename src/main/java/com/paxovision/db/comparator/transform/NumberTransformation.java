package com.paxovision.db.comparator.transform;

public class NumberTransformation implements Transform {

    /**
     *   To convert value to number
     *
     *   @param value
     *   (âreturn
     */

    public Object transform(Object value) {
        if (value == null) {
            return null;
        } else {
            return Double.parseDouble(value.toString().trim());
        }
    }
}