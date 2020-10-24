package com.paxovision.db.comparator.processor;

import com.paxovision.db.comparator.model.DiffConfig;
import com.paxovision.db.comparator.model.StringListKey;
import com.paxovision.db.comparator.model.StringListKeyCaseInsensitiveComparator;
import com.paxovision.db.comparator.transform.SpringExpressionTransformation;
import com.paxovision.db.comparator.transform.Transform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
//import org.apache.commons.collections.map.CaseinsensitiveMap;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
public abstract class KeyedMapBuilder {

    private final List<String> keyAttrs;
    protected final Map<StringListKey, List<Map<String, Object>>> duplicates;
    protected final Map<StringListKey, Map<String, Object>> keyedMap;
    private DiffConfig diffConfig;

    public KeyedMapBuilder(List<String> keyAttrs, DiffConfig diffConfig) {
        this(keyAttrs, diffConfig, -1);
    }

    public KeyedMapBuilder(List<String> keyAttrs, DiffConfig diffConfig, int initialSize) {
        if (keyAttrs == null || keyAttrs.isEmpty()) {
            throw new IllegalArgumentException("keyAtrrs cannot be null or empty");
        }

        this.diffConfig = diffConfig;
        this.keyAttrs = keyAttrs;
        this.duplicates = new LinkedHashMap<>();
        if (diffConfig.isCaseInsensitiveColumn()) {
            this.keyedMap = new TreeMap<>(new StringListKeyCaseInsensitiveComparator());
        } else {
            if (initialSize > 0) {
                this.keyedMap = new HashMap<>(initialSize);
            } else {
                this.keyedMap = new HashMap<>();
            }
        }
    }

    private Map getDataMap(Map<String, Object> map) {
        if (diffConfig.isCaseInsensitiveColumn()) {
            return new CaseInsensitiveMap(map);
        } else {
            return map;
        }
    }

    public void add(Map<String, Object> map) {
        Map dataMap = getDataMap(map);
        transform(dataMap);
        StringListKey key = getStringListKey(dataMap);
        Map<String, Object> convertedValueMap = getValueMap(dataMap);
        addNewEntry(key, convertedValueMap);
    }

    /**
    *	Apply transformation rule is any
    *
    * @param map - data Map
    */
    public void transform(Map<String, Object> map) {
        if (!diffConfig.getAllTransform().isEmpty()) {
            for (Entry<String, Transform> entry : diffConfig.getAllTransform().entrySet()) {
                if (map.containsKey(entry.getKey())) {
                    Transform transform = entry.getValue();
                    if (transform instanceof SpringExpressionTransformation) {
                        ((SpringExpressionTransformation) transform).setDataMap(map);
                    }
                    Object modifiedValue = transform.transform(map.get(entry.getKey()));
                    map.put(entry.getKey(), modifiedValue);
                }
            }
        }
    }

    protected void addFirstTwoDuplicateEntries(
            StringListKey key,
            Map<String, Object> existingValueMap,
            Map<String, Object> newValueMap) {
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(existingValueMap);
            list.add(newValueMap);
            duplicates.put(key, list);
    }

    protected Map<String, Object> getValueMap(Map<String, Object> map) {
        Map<String, Object> convertedValueMap = map;
        if (diffConfig.isCaseInsensitiveColumn()) {
            convertedValueMap = new CaseInsensitiveMap(map);
        }
        return convertedValueMap;
    }

    private StringListKey getStringListKey(Map<String, Object> map) {
        // keyValueList.clear(); // perf improvement instead of creating new object everytime but it
        // might create issue
        StringListKey list = new StringListKey(new ArrayList<>());
        for (String keyAttr : keyAttrs) {
            Object value = map.get(keyAttr);
            String convertToString = null;
            if (value instanceof String) {
                convertToString = ((String) value);
            } else if (value != null) {
                convertToString = value.toString();
            }
            list.addKey(convertToString);
        }
        return list;
    }

    protected abstract void addNewEntry(StringListKey key, Map<String, Object> valueMap);

    public Map<StringListKey, Map<String, Object>> getKeyedMap() {
        if (diffConfig.isExcludeDuplicates()) {
            return getKeyedMapWithoutDuplicates();
        }
        return keyedMap;
    }

    public Map<StringListKey, Map<String, Object>> getKeyedMapWithoutDuplicates() {
        if (!duplicates.isEmpty()) {
            Set<StringListKey> listKeySet = duplicates.keySet();
            for (StringListKey key : listKeySet) {
                keyedMap.remove(key);
            }
        }
        return keyedMap;
    }

    public List<String> getKeyAttrs() {
        return keyAttrs;
    }

    public Map<StringListKey, List<Map<String, Object>>> getDuplicates() {
        return duplicates;
    }
}
