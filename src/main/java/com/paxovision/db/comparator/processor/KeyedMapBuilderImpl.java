package com.paxovision.db.comparator.processor;

import com.paxovision.db.comparator.model.DiffConfig;
import com.paxovision.db.comparator.model.StringListKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyedMapBuilderImpl extends KeyedMapBuilder {

    public KeyedMapBuilderImpl(List<String> keyAttrs, DiffConfig diffConfig) {
        super(keyAttrs, diffConfig);

    }

    public KeyedMapBuilderImpl(List<String> keyAttrs, DiffConfig diffConfig, int size) {
        super(keyAttrs, diffConfig, size);
    }

    @Override
    protected void addNewEntry(StringListKey key, Map<String, Object> valueMap) {
        // default do nothing, i.e., ignore the new entry as if there isn't duplicate
        // i.e., the first value will be used
        if (keyedMap.containsKey(key)) {
            if (duplicates.containsKey(key)) {
                duplicates.get(key).add(valueMap);
            } else {
                Map<String, Object> existingValueMap = keyedMap.get(key);
                addFirstTwoDuplicateEntries(key, new HashMap(existingValueMap), valueMap);
            }
        } else{
            keyedMap.put(key, valueMap);
        }
    }
}
