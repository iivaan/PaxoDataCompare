package com.paxovision.db.compare;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.paxovision.db.comparator.model.DiffConfig;
import com.paxovision.db.comparator.model.DiffConfig.DiffLimiter;
import org.apache.commons.lang3.tuple.Pair;

public class MapDiffResult<K, V, DiffV> {

    protected final Map<K, V> onlyIn1;
    protected final Map<K, V> onlyIn2;
    protected final Map<K,Map> equal;
    protected final Map<K, DiffV> diff;
    protected final Map<K, Pair<V, V>> info;
    protected final Map<K, Pair<V, V>> ignore;
    protected final Map<K, List<V>> duplicates1;
    protected final  Map<K, List<V>> duplicates2;

    protected final  Map<K, Map> conditionRule;
    protected final  Map<K, Map> transformRule;

    protected DiffConfig.DiffLimiter limiter = new DiffConfig.DiffLimiter();

    protected AtomicInteger equalCount = new AtomicInteger(0);
    protected AtomicInteger diffCount = new AtomicInteger(0);
    protected AtomicInteger onlyIn1Count = new AtomicInteger(0);
    protected AtomicInteger onlyIn2Count = new AtomicInteger(0);

    protected boolean reachedMaxDiff = false;


    public boolean isReachedMaxDiff() {
        return reachedMaxDiff;
    }

    public void setReachedMaxDiff(boolean reachedMaxDiff) {
        this.reachedMaxDiff = reachedMaxDiff;
    }

    public void setLimiter(DiffLimiter limiter){
        if(limiter != null) {
            this.limiter = limiter;
        }

    }

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



}
