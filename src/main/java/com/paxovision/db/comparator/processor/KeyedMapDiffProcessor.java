package com.paxovision.db.comparator.processor;

import com.paxovision.db.comparator.model.DiffConfig;
import com.paxovision.db.comparator.model.MapDiffResult;
import com.paxovision.db.comparator.model.StringListKey;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

public class KeyedMapDiffProcessor {

    Map<StringListKey, Map<String, Object>> dataSet1;
    Map<StringListKey, Map<String, Object>> dataSet2;
    Map<StringListKey, List<Map<String, Object>>> duplicates1;
    Map<StringListKey, List<Map<String, Object>>> duplicates2;

    public KeyedMapDiffProcessor(
            KeyedMapBuilderImpl firstBuilder,
            KeyedMapBuilderImpl secondBuilder) {

        this.dataSet1 = firstBuilder.getKeyedMap();
        this.dataSet2 = secondBuilder.getKeyedMap();
        this.duplicates1 = firstBuilder.getDuplicates();
        this.duplicates2 = secondBuilder.getDuplicates();
    }

    public MapDiffResult diff(DiffConfig diffConfig) {
        KeyedMapDiffAlgo diffAlgo = new KeyedMapDiffAlgo(diffConfig);
        MapDiffResult<
                StringListKey,
                Map<String, Object>,
                MapDiffResult<String, Object, Pair<Object, Object>>>
                result = null;

        result = MapDiffResult.newInstance(duplicates1, duplicates2);
        result.setLimiter(diffConfig.getDiffLimiter());
        diffAlgo.diff(dataSet1, dataSet2, result);
        return result;
    }

}
