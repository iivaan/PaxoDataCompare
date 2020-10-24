package com.paxovision.db.comparator.model;

import com.paxovision.db.comparator.model.DiffConfig.DiffLimiter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings("squid:S00119")
public class MapDiffResult<K, V, DiffV> {

    protected final Map<K, V> onlyIn1;
    protected final Map<K, V> onlyIn2;
    protected final Map<K, Map> equal;
    protected final Map<K, DiffV> diff;
    protected final Map<K, Pair<V, V>> info;
    protected final Map<K, Pair<V, V>> ignore;
    protected final Map<K, List<V>> duplicates1;
    protected final Map<K, List<V>> duplicates2;
    protected final Map<K, Map> conditionRule;
    protected final Map<K, Map> transformRule;
    protected DiffConfig.DiffLimiter limiter = new DiffConfig.DiffLimiter();
    protected AtomicInteger equalCount = new AtomicInteger(0);
    protected AtomicInteger diffCount = new AtomicInteger(0);
    protected AtomicInteger onlyIn1Count = new AtomicInteger(0);
    protected AtomicInteger onlyIn2Count = new AtomicInteger(0);

    protected boolean reachedMaxDiff = false;

    protected MapDiffResult(Map<K, List<V>> duplicates1, Map<K, List<V>> duplicates2) {
        this.onlyIn1 = new LinkedHashMap<>();
        this.onlyIn2 = new LinkedHashMap<>();
        this.equal = new LinkedHashMap<>();
        this.diff = new LinkedHashMap<>();
        this.info = new LinkedHashMap<>();
        this.ignore = new LinkedHashMap<>();
        this.duplicates1 = duplicates1;
        this.duplicates2 = duplicates2;
        this.conditionRule = new LinkedHashMap<>();
        this.transformRule = new LinkedHashMap<>();
        this.equalCount = new AtomicInteger(0);
        this.diffCount = new AtomicInteger(0);
        this.onlyIn1Count = new AtomicInteger(0);
        this.onlyIn2Count = new AtomicInteger(0);
        this.limiter = new DiffConfig.DiffLimiter(-1, -1, -1, -1);
    }

    public boolean isReachedMaxDiff() {
        return reachedMaxDiff;
    }

    public void setReachedMaxDiff(boolean reachedMaxDiff) {
        this.reachedMaxDiff = reachedMaxDiff;
    }

    public void setLimiter(DiffLimiter limiter) {
        if (limiter != null) {
            this.limiter = limiter;
        }
    }

    public String getSummaryText() {
        StringBuilder sb = new StringBuilder();
        sb.append(isEqual() ? "Passed" : "Failed");
        String[] categories =
                new String[]{"diff", "onlyInl", "onlyIn2", "duplicates1", "duplicates2", "equal"};
        int[] counts = new int[]{
                diffCount.get(),
                onlyIn1Count.get(),
                onlyIn2Count.get(),
                duplicates1.size(),
                duplicates2.size(),
                equalCount.get()
        };

        for (int i = 0; i < categories.length; i++) {
            sb.append('\n').append(String.format("%-15s : %d", categories[i], counts[i]));
        }
        return sb.toString();
    }

    @SuppressWarnings("squid:S00119")
    public static <K, V, DiffV> MapDiffResult<K, V, DiffV> newlnstance() {
        return new MapDiffResult<>(new LinkedHashMap<>(), new LinkedHashMap<>());
    }

    public void merge(MapDiffResult<K, V, DiffV> result2) {
        onlyIn1.putAll(result2.getOnlyIn1());
        onlyIn2.putAll(result2.getOnlyIn2());
        equal.putAll(result2.getEqual());
        diff.putAll(result2.getDiff());
        info.putAll(result2.getInfo());
        ignore.putAll(result2.getIgnore());
        duplicates1.putAll(result2.getDuplicatesIn1());
        duplicates2.putAll(result2.getDuplicatesIn2());
        conditionRule.putAll(result2.getConditionRule());
        transformRule.putAll(result2.getTransformRule());
    }

    public Map<K, Map> getConditionRule() {
        return conditionRule;
    }

    public Map<K, Map> getTransformRule() {
        return transformRule;
    }

    @SuppressWarnings("squid:S00119")
    public static <K, V, DiffV> MapDiffResult<K, V, DiffV> newInstance(
            Map<K, List<V>> duplicates1, Map<K, List<V>> duplicates2) {
        return new MapDiffResult<>(duplicates1, duplicates2);
    }

    public void addOnlyIn1(K key, V value) {
        onlyIn1.put(key, value);
        onlyIn1Count.incrementAndGet();
    }

    public void addOnlyIn2(K key, V value) {
        onlyIn2.put(key, value);
        onlyIn2Count.incrementAndGet();
    }

    public void addEqual(K key, V valuel, V value2) {
        Map map = new HashMap();
        map.put("valMetadata", Pair.of(valuel, value2));
        map.put("isEqual", true);
        map.put("condition", false);
        map.put("transform", false);
        equal.put(key, map);
        equalCount.incrementAndGet();
    }

    public void addEqual(K key, V valuel, V value2, boolean condition, boolean transform) {
        Map map = new HashMap();
        map.put("valMetadata", Pair.of(valuel, value2));
        map.put("isEqual", true);
        map.put("condition", condition);
        map.put("transform", transform);
        equal.put(key, map);
        if (condition) {
            conditionRule.put(key, null);
        }
        if (transform) {
            transformRule.put(key, null);
        }
        equalCount.incrementAndGet();
    }

    public void addDiff(K key, DiffV diffOfValue, boolean condition, boolean transform) {
        diff.put(key, diffOfValue);
        diffCount.incrementAndGet();
        if (condition) {
            conditionRule.put(key, null);
        }
        if (transform) {
            transformRule.put(key, null);
        }
    }

    public void addDiff(K key, DiffV diffOfValue) {
        diff.put(key, diffOfValue);
        diffCount.incrementAndGet();
    }

    public void addInfo(K key, V value1, V value2) {
        info.put(key, Pair.of(value1, value2));
    }

    public void addIgnore(K key, V value1, V value2) {
        ignore.put(key, Pair.of(value1, value2));
    }

    public Map<K, V> getOnlyIn1() {
        return onlyIn1;
    }

    public void removeOnlyIn1() {
        onlyIn1.clear();
    }

    public void removeOnlyIn2() {
        onlyIn2.clear();
    }

    public Map<K, V> getOnlyIn2() {
        return onlyIn2;

    }

    public Map<K, Map> getEqual() {
        return equal;
    }

    public int getEqualCount() {
        return equalCount.get();
    }

    public DiffLimiter getDiffLimiter() {
        return limiter;
    }

    public int getDiffCount() {
        return diffCount.get();
    }

    public int getOnlyIn2Count() {
        return onlyIn2Count.get();
    }

    public int getOnlyIn1Count() {
        return onlyIn1Count.get();
    }

    public Map<K, DiffV> getDiff() {
        return diff;
    }

    public Map<K, Pair<V, V>> getInfo() {
        return info;
    }

    public Map<K, Pair<V, V>> getIgnore() {
        return ignore;
    }

    public Map<K, List<V>> getDuplicatesIn1() {
        return duplicates1;
    }

    public int getDuplicatesIn1ValueSize() {
        if (!duplicates1.isEmpty()) {
            return duplicates1.values().stream().mapToInt(List::size).sum();
        }
        return 0;
    }

    public Map<K, List<V>> getDuplicatesIn2() {
        return duplicates2;
    }

    public int getDuplicatesIn2ValueSize() {
        if (!duplicates2.isEmpty()) {
            return duplicates2.values().stream().mapToInt(List::size).sum();
        }
        return 0;
    }

    public boolean isCountIn1Fail() {
        return false;
    }

    public boolean isCountIn2Fail() {
        return false;
    }

    public boolean isOnlyIn1Fail() {
        return !onlyIn1.isEmpty();

    }

    public boolean isOnlyIn2Fail() {
        return !onlyIn2.isEmpty();
    }

    public boolean isEqualFail() {
        return false;
    }

    public boolean isDiffFail() {
        return !diff.isEmpty();
    }

    public boolean isEqual() {
        return onlyIn1.isEmpty() && onlyIn2.isEmpty() && diff.isEmpty();
    }

    public int getSizeIn1() {
        return equalCount.get() + diffCount.get() + onlyIn1Count.get();
    }

    public int getSizeIn2() {
        return equalCount.get() + diffCount.get() + onlyIn2Count.get();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof MapDiffResult) {
            @SuppressWarnings("unchecked")
            MapDiffResult<K, V, DiffV> mdr = (MapDiffResult<K, V, DiffV>) obj;
            return onlyIn1.equals(mdr.onlyIn1)
                    && onlyIn2.equals(mdr.onlyIn2)
                    && equal.equals(mdr.equal)
                    && diff.equals(mdr.diff)
                    && duplicates1.equals(mdr.duplicates1)
                    && duplicates2.equals(mdr.duplicates2);
        }
        return false;
    }

}
