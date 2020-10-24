package com.paxovision.db.comparator.processor;

//import com.paxovision.db.comparator.model.MapDiffResult;
import com.paxovision.db.comparator.model.MapDiffResult;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
@SuppressWarnings("squid:S00119")
public abstract class MapDiffAlgo<K, V, DiffV> {

    public void diff(
            Map<K, ? extends V> dataSet1,
            Map<K, ? extends V> dataSet2,
            MapDiffResult<K, V, DiffV> result) {
        dataSet1 = ensureNotNull(dataSet1);
        dataSet2 = ensureNotNull(dataSet2);

        for (Entry<? extends K, ? extends V> entry : dataSet1.entrySet()) {
            K leftKey = entry.getKey();
            V leftValue = entry.getValue();
            if (dataSet2.containsKey(leftKey)) {
                V rightvalue = dataSet2.remove(leftKey);
                handleKeyExistsInBoth(result, leftKey, leftValue, rightvalue);
            } else {
                handleKeyExistsOnlyIn1(result, leftKey, dataSet1.get(leftKey));
            }
        }

        Iterator iter = dataSet2.keySet().iterator();
        while (iter.hasNext()) {
            K rightKey = (K) iter.next();
            handleKeyExistsOnlyIn2(result, rightKey, dataSet2.get(rightKey));
        }
    }

        protected void handleKeyExistsOnlyIn1(MapDiffResult< K, V, DiffV > result, K key1, V value1){
            result.addOnlyIn1(key1, value1);
        }

        protected void handleKeyExistsOnlyIn2 (MapDiffResult < K, V, DiffV > result, K key2, V value2){
            result.addOnlyIn2(key2, value2);
        }

        protected void handleKeyExistsInBoth (MapDiffResult< K, V, DiffV > result, K key1, V value1, V value2){
            compareValue(result, key1, value1, value2);
        }

        protected abstract void compareValue( MapDiffResult < K, V, DiffV > result, K keyl, V value1, V value2);

        private Map<K, ? extends V> ensureNotNull(Map<K, ? extends V> dataSet) {
            if (dataSet == null) {
                return new TreeMap<>();
            }
            return dataSet;
        }

}
