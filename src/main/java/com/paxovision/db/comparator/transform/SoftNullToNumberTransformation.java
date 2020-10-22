package com.paxovision.db.comparator.transform;

import org.apache.commons.lang3.StringUtils;

public class SoftNullToNumberTransformation implements Transform {
    /**
     * To handle if either side of the value is NULL/empty String or 0.0
     * @param value
     * @return
     */

    public Object transform(Object value) {
        if (value == null) {
            return Double.valueOf(0.0);
        } else if (value instanceof String) {
            if (StringUtils.isBlank(value.toString())) {
                return Double.valueOf(0.0);
            }
            return Double.parseDouble(value.toString());
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return value;
    }
}
