package com.paxovision.db.comparator.model;

import com.paxovision.db.comparator.equivalence.Equivalence;
import com.paxovision.db.comparator.transform.Transform;
import com.paxovision.db.comparator.util.CollectionUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DiffConfig {

    public static final String INFO_COLUMN = "Info";
    public static final DiffConfig EMPTY_CONFIG = new DiffConfig.Builder().build();
    private final List<String> infoColumns;
    private final List<String> ignoreColumns;
    private final Map<String, Equivalence> equivalentMap;
    private final Map<String, Transform> transformMap;
    private final boolean caseInsensitiveColumn;
    private final boolean excludeDuplicates;
    private final boolean ignoreMissingNullAttribute;
    private final DiffLimiter diffLimiter;
    private final List<String> compareColumns;

    public DiffLimiter getDiffLimiter() {
        return diffLimiter;
    }

//    public DiffConfig(List<String> infoColumns, List<String> ignoreColumns,
//                      Map<String, Equivalence> equivalentMap, Map<String, Transform> transformMap,
//                      boolean caseInsensitiveColumn, boolean excludeDuplicates,
//                      boolean ignoreMissingNullAttribute, DiffLimiter diffLimiter,
//                      List<String> compareColumns) {
//        this.infoColumns = infoColumns;
//        this.ignoreColumns = ignoreColumns;
//        this.caseInsensitiveColumn = caseInsensitiveColumn;
//        this.excludeDuplicates = excludeDuplicates;
//        this.ignoreMissingNullAttribute = ignoreMissingNullAttribute;
//
//        this.compareColumns = compareColumns;
//        if(this.caseInsensitiveColumn){
//            this.equivalentMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
//            this.equivalentMap.putAll(equivalentMap);
//
//            this.transformMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
//            this.transformMap.putAll(transformMap);
//        }
//        else{
//            this.equivalentMap = equivalentMap;
//            this.transformMap = transformMap;
//        }
//
//        this.diffLimiter = diffLimiter;
//    }


    /** Control data set to improve memory efficiency */
    public static class DiffLimiter {
        private int maxDiff = -1;
        private int maxEqual = -1;
        private int maxOnlyIn1 = -1;
        private int maxOnlyIn2 = -1;

        public DiffLimiter() {
        }

        public DiffLimiter(int maxDiff, int maxEqual, int maxOnlyInl, int maxOnlyIn2) {
            this.maxDiff = maxDiff;
            this.maxEqual = maxEqual;
            this.maxOnlyIn1 = maxOnlyInl;
            this.maxOnlyIn2 = maxOnlyIn2;
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
        public void setMaxOnlyIn1(int maxOnlyIn1) {
            this.maxOnlyIn1 = maxOnlyIn1;
        }

        public int getMaxOnlyIn2() {
            return maxOnlyIn2;
        }
        public void setMaxOnlyIn2(int maxOnlyIn2) {
            this.maxOnlyIn2 = maxOnlyIn2;
        }
    }

    @SuppressWarnings("squid:S00107")
    private DiffConfig(
            List<String> infoColumns,
            List<String> ignoreColumns,
            Map<String, Equivalence> equivalentMap,
            Map<String, Transform> transformMap,
            boolean caseInsensitiveColumn,
            boolean excIudeDuplicates,
            boolean ignoreMissingNullAttribute,
            DiffLimiter diffLimiter,
            List<String> compareColumns) {

        this.caseInsensitiveColumn = caseInsensitiveColumn;
        this.infoColumns = CollectionUtil.ensureListNotNull(infoColumns);
        this.ignoreColumns = CollectionUtil.ensureListNotNull(ignoreColumns);
        this .compareColumns = CollectionUtil.ensureListNotNull(compareColumns);
        this.ignoreMissingNullAttribute = ignoreMissingNullAttribute;
        this.excludeDuplicates = excIudeDuplicates;
        if (this.caseInsensitiveColumn) {
            this.equivalentMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            this.equivalentMap.putAll(equivalentMap);
            this.transformMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            this.transformMap.putAll(transformMap);
        } else {
            this.equivalentMap = equivalentMap;
            this.transformMap = transformMap;
        }
        this.diffLimiter = diffLimiter;
    }

    public List<String> getInfoColumns() {
        return infoColumns;
    }

    public boolean isIgnoreMissingNullAttribute() {
        return ignoreMissingNullAttribute;
    }

    public boolean isCaseInsensitiveColumn() {
        return caseInsensitiveColumn;
    }

    public boolean isExcludeDuplicates() {
        return excludeDuplicates;
    }

    public boolean isInfoColumn(String column) {
        if (caseInsensitiveColumn) {
            return infoColumns.stream().anyMatch(column ::equalsIgnoreCase);
        } else {
            return infoColumns.contains(column);
        }
    }

    public boolean isIgnoreColumn(String column) {
        if (caseInsensitiveColumn) {
            return ignoreColumns.stream().anyMatch(column ::equalsIgnoreCase);
        } else {
            return ignoreColumns.contains(column);
        }
    }

    public Equivalence getEquivalent(String column) {
        return equivalentMap.get(column);
    }

    public List<String> getCompareColumns() {
        return compareColumns;
    }

    public boolean isCompareOnlyColumns() {
        return compareColumns != null && !compareColumns.isEmpty();
    }

    public boolean isCompareColumn(String column) {
        if (caseInsensitiveColumn) {
            return compareColumns.stream().anyMatch(column::equalsIgnoreCase);
        } else {
            return compareColumns.contains(column);
        }
    }

    public Map<String, Equivalence> getAllEquivalence() {
        return equivalentMap;
    }

    public Transform getTransform(String column) {
        return transformMap.get(column);
    }

    public Map<String, Transform> getAllTransform() {
        return transformMap;
    }

    public static class Builder {


        private List<String> infoColumns;
        private List<String> ignoreColumns;
        private Map<String, Equivalence> equivalenceMap = new LinkedHashMap<>();
        private Map<String, Transform> transformMap = new LinkedHashMap<>();
        private boolean caseInsensitiveColumn;
        private boolean excIudeDuplicates;
        private boolean ignoreMissingNullAttribute;
        private DiffLimiter diffLimiter;
        private List<String> compareColumns;

        public DiffConfig.Builder setDiffLimiter(DiffLimiter diffLimiter) {
            this.diffLimiter = diffLimiter;
            return this;
        }

        public DiffConfig.Builder setInfoColumns(List<String> infoColumns) {
            this.infoColumns = infoColumns;
            return this;
        }

        public DiffConfig.Builder setCompareColumns(List<String> compareColumns) {
            this.compareColumns = compareColumns;
            return this;
        }

        public DiffConfig.Builder setIgnoreColumns(List<String> ignoreColumns) {
            this.ignoreColumns = ignoreColumns;
            return this;
        }

        public DiffConfig.Builder setCaseInsensitiveColumn(boolean caselnsensitiveColumn) {
            this.caseInsensitiveColumn = caselnsensitiveColumn;
            return this;
        }

        public DiffConfig.Builder setExcludeDuplicates(boolean excludeDuplicates) {
            this.excIudeDuplicates = excludeDuplicates;
            return this;
        }


        public DiffConfig.Builder setIgnoreMissingNullAttribute(

                boolean ignoreMissingNullAttribute) {

            this.ignoreMissingNullAttribute = ignoreMissingNullAttribute;
            return this;


        }

        public DiffConfig build() {
            return new DiffConfig(
                    infoColumns,
                    ignoreColumns,
                    equivalenceMap,
                    transformMap,
                    caseInsensitiveColumn,
                    excIudeDuplicates,
                    ignoreMissingNullAttribute,
                    diffLimiter,
                    compareColumns);
        }


        public DiffConfig.Builder addEquivalence(String column, Equivalence<?> equivalence) {
            equivalenceMap.put(column, equivalence);
            return this;
        }

        public DiffConfig.Builder addTransform(String column, Transform transform) {
            this.transformMap.put(column, transform);
            return this;
        }

    }


}
