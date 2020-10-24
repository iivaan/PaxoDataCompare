package com.paxovision.db.comparator.processor;

import com.paxovision.db.comparator.model.DiffConfig;
import com.paxovision.db.comparator.model.MapDiffResult;
import com.paxovision.db.comparator.model.StringListKey;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

public class KeyedMapDiffAlgo
        extends MapDiffAlgo<
        StringListKey,
        Map<String, Object>,
        MapDiffResult<String, Object, Pair<Object, Object>>>{

    private final DiffConfig diffConfig;

    public KeyedMapDiffAlgo(DiffConfig diffConfig) {
        this.diffConfig = diffConfig;
    }

    @Override
    protected void compareValue(
            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result,
                    StringListKey key,
                    Map<String, Object> value1,
                    Map<String, Object> value2) {

        if (value1 == null) {
            value1 = new HashMap<>();
        }

        if (value2 == null) {
            value2 = new HashMap<>();
        }

        if (value1.equals(value2)) {
            result.addEqual(key, value1, value2);
        } else {
            MapDiffResult<String, Object, Pair<Object, Object>> valueDiff =
                    MapDiffResult.newlnstance();
            ObjectMapDiffAlgo algo = new ObjectMapDiffAlgo(diffConfig);
            algo.diff(value1, value2, valueDiff);
            if (valueDiff.isEqual()) {
                result.addEqual(key, value1, value2);
            } else {
                result.addDiff(key, valueDiff);
            }
        }
    }

}
