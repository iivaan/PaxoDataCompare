package com.paxovision.db.comparator.model;

import com.paxovision.db.comparator.equivalence.Equivalence;
import com.paxovision.db.comparator.transform.Transform;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DiffConfig {

    public static final String INFO_COLUMN = "Info";
    //public static final DiffConfig EMPTY_CONFIG = new DiffConfig.Builder().build();
    private final List<String> infoColumns;
    private final List<String> ignoreColumns;
    private final Map<String, Equivalence> equivalentMap;
    private final Map<String, Transform> transformMap;
    private final boolean caseInsensitiveColumn;
    private final boolean excludeDuplicates;
    private final boolean ignoreMissingNullAttribute;
    private final DiffLimiter diffLimiter;
    private final List<String> compareColumns;

    public DiffConfig(List<String> infoColumns, List<String> ignoreColumns,
                      Map<String, Equivalence> equivalentMap, Map<String, Transform> transformMap,
                      boolean caseInsensitiveColumn, boolean excludeDuplicates,
                      boolean ignoreMissingNullAttribute, DiffLimiter diffLimiter,
                      List<String> compareColumns) {
        this.infoColumns = infoColumns;
        this.ignoreColumns = ignoreColumns;
        this.caseInsensitiveColumn = caseInsensitiveColumn;
        this.excludeDuplicates = excludeDuplicates;
        this.ignoreMissingNullAttribute = ignoreMissingNullAttribute;

        this.compareColumns = compareColumns;
        if(this.caseInsensitiveColumn){
            this.equivalentMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            this.equivalentMap.putAll(equivalentMap);

            this.transformMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            this.transformMap.putAll(transformMap);
        }
        else{
            this.equivalentMap = equivalentMap;
            this.transformMap = transformMap;
        }

        this.diffLimiter = diffLimiter;
    }

    public DiffLimiter getDiffLimiter() {
        return diffLimiter;
    }





    /** Control data set to improve memory efficiency */
    public static class DiffLimiter {
        private int maxDiff = -1;
        private int maxEqual = -1;
        private int maxOnlyIn1 = -1;
        private int max0nlyIn2 = -1;

        public DiffLimiter() {
        }

        public DiffLimiter(int maxDiff, int maxEqual, int maxOnlyInl, int maxOnlyIn2) {
            this.maxDiff = maxDiff;
            this.maxEqual = maxEqual;
            this.maxOnlyIn1 = maxOnlyInl;
            this.max0nlyIn2 = maxOnlyIn2;
        }

        public int getMaxDiff() {
            return maxDiff;
        }

        public void setMaxDiff(int maxDiff) {
            this.maxDiff = maxDiff;
        }

        public int getMaxEqual() {
            return maxEqual;
        }

        public void setMaxEqual(int maxEqual) {
            this.maxEqual = maxEqual;
        }

        public int getMaxOnlyIn1() {
            return maxOnlyIn1;
        }

        public int getMax0nlyIn2() {
            return max0nlyIn2;
        }
        public void setMax0nlyIn2(int max0nlyIn2) {
            this.max0nlyIn2 = max0nlyIn2;
        }
    }
}
