package com.paxovision.db.comparator.processor;

import com.paxovision.db.comparator.equivalence.Equivalence;
import com.paxovision.db.comparator.model.DiffConfig;
import com.paxovision.db.comparator.model.MapDiffResult ;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ObjectMapDiffAlgo extends MapDiffAlgo<String, Object, Pair<Object, Object>>{

    private final DiffConfig diffConfig;

    public ObjectMapDiffAlgo() {
        this(DiffConfig.EMPTY_CONFIG);
    }

    public ObjectMapDiffAlgo(DiffConfig diffConfig) {
        this.diffConfig = diffConfig == null ? DiffConfig.EMPTY_CONFIG : diffConfig;
    }

    @Override
    protected void handleKeyExistsOnlyIn1(
            MapDiffResult<String, Object, Pair<Object, Object>> result,
            String key1,
            Object value1) {

        if (diffConfig.isInfoColumn(key1)) {
            result.addInfo(key1, value1, null);
        } else if (diffConfig.isIgnoreColumn(key1)) {
            result.addIgnore(key1, value1, null);
        } else {
            if (diffConfig.isCompareOnlyColumns() && !diffConfig.isCompareColumn(key1)) {
                result.addIgnore(key1, value1, null);
            } else {
                if (diffConfig.isIgnoreMissingNullAttribute()) {
                    if (!isValueNull(value1)) {
                        result.addOnlyIn1(key1, value1);
                    }
                } else {
                    result.addOnlyIn1(key1, value1);
                }
            }
        }
    }

    private boolean isValueNull(Object valuel) {
        if (valuel instanceof String) {
            return StringUtils.isBlank((String) valuel);
        } else if (valuel instanceof Number) {
            return ((Number) valuel).doubleValue() == 0;
        }
        return valuel == null;
    }

    @Override
    protected void handleKeyExistsOnlyIn2(
            MapDiffResult<String, Object, Pair<Object, Object>> result,
            String key2,
            Object value2) {
        if (diffConfig.isInfoColumn(key2)) {
            result.addInfo(key2, null, value2);
        } else if (diffConfig.isIgnoreColumn(key2)) {
            result.addIgnore(key2, null, value2);
        } else {
            if (diffConfig.isCompareOnlyColumns() && !diffConfig.isCompareColumn(key2)) {
                result.addIgnore(key2, null, value2);
            } else {
                if (diffConfig.isIgnoreMissingNullAttribute()) {
                    if (!isValueNull(value2)) {
                        result.addOnlyIn2(key2, value2);
                    }
                } else {
                    result.addOnlyIn2(key2, value2);
                }
            }
        }
    }


    @Override
    protected void handleKeyExistsInBoth(
            MapDiffResult<String, Object, Pair<Object, Object>> result,
            String key,
            Object value1,
            Object value2) {
        if (diffConfig.isInfoColumn(key)) {
            result.addInfo(key, value1, value2);
            result.addEqual(key, value1, value2, true, false);
        } else if (diffConfig.isIgnoreColumn(key)) {
            result.addIgnore(key, value1, value2);
        } else {
            if (diffConfig.isCompareOnlyColumns() && !diffConfig.isCompareColumn(key)) {
                result.addEqual(key, value1, value2, true, false);
            } else {
                compareValue(result, key, value1, value2);
            }
        }
    }

    @Override
    protected void compareValue(
            MapDiffResult<String, Object, Pair<Object, Object>> result,
            String key,
            Object valuel,
            Object value2) {

        Equivalence equivalence = diffConfig.getEquivalent(key);
        boolean equals = isEqual(valuel, value2, equivalence);
        if (equals) {
            result.addEqual(key, valuel, value2, equivalence != null, false);
        } else {
            result.addDiff(key, Pair.of(valuel, value2), equivalence != null, false);
        }
    }

    private boolean isEqual(Object value1, Object value2, Equivalence equivalence) {
        if (equivalence != null) {
            return equivalence.doDefaultEquivalent(value1, value2);
        }
        if (value1 == null && value2 == null) {
            return true;
        }
        if (value1 == null || value2 == null) {
            return false;
        }
        return value1.equals(value2);
    }

    public DiffConfig getDiffConfig() {
        return diffConfig;
    }

}
