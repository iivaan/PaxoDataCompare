package com.paxovision.db.comparator.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java .util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class CollectionUtil {
    public static final String NULL = "(NULL)";

    public CollectionUtil() {
    }

    public static <E> boolean isEmptyList(List<E> list) {
        return list == null || list.isEmpty();
    }

    public static <K, V> boolean isEmptyMap(Map<K, V> map) {
        return map == null || map.size() == 0;
    }

    public static List<String> trimTrailingEmptyValues(List<String> originalValueList) {
        List<String> trimmed = new ArrayList<>();
        int indexOfLastNonNullElement = originalValueList.size() - 1;
        while (indexOfLastNonNullElement >= 0) {
            String value = originalValueList.get(indexOfLastNonNullElement);
            if (value != null) {
                break;
            }
            indexOfLastNonNullElement--;
        }

        if (indexOfLastNonNullElement == (originalValueList.size() - 1)) {
            return originalValueList;
        }

        for (int i = 0; i <= indexOfLastNonNullElement; i++) {
            trimmed.add(originalValueList.get(i));
        }
        return trimmed;
    }


    private static int compareTwoString(String sl, String s2, boolean ignoreCase) {
        if (ignoreCase) {
            return sl.toUpperCase().compareTo(s2.toUpperCase());
        } else {
            return sl.compareTo(s2);
        }

    }

    @SuppressWarnings("squid:S3776")
    public static int compareList(List<String> list1, List<String> list2, boolean ignoreCase) {
        int index = 0;

        while (index < list1.size()) {
            if (index < list2.size()) {
                String keyColVall = list1.get(index);
                String keyColVal2 = list2.get(index);
                if (keyColVall == null) {
                    if (keyColVal2 != null) {
                        return -1;
                    }
                } else {
                    if (keyColVal2 == null) {
                        return 1;
                    }
                    int compareTo = compareTwoString(keyColVall, keyColVal2, ignoreCase);
                    if (compareTo != 0) {
                        return compareTo;
                    }
                }
            } else { // this key has more columns Jhan key2
                return 1; // at least the last is hot null
            }
            index++;
        }
        if (index < list2.size()) { // if key2 has more columns than this key
            return -1;
        }
        return 0;
    }

    public static <E> List<E> ensureListNotNull(List<E> list) {
        if (list == null) {
            @SuppressWarnings("unchecked")
            List<E> emptyList = Collections.emptyList();
            return emptyList;
        }
        return list;
    }

    public static <K, V> Map<K, V> ensureMapNotNull(Map<K, V> map) {
        if (map == null) {
            @SuppressWarnings("unchecked")
            Map<K, V> emptyMap = Collections.emptyMap();
            return emptyMap;
        }
        return map;
    }

    public static <E> List<E> unmodifiableList(List<E> list) {
        if (list == null) {
            @SuppressWarnings("unchecked")
            List<E> emptyList = Collections.emptyList();
            return emptyList;
        }
        return Collections.unmodifiableList(list);
    }

    public static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
        if (map == null) {
            @SuppressWarnings("unchecked")
            Map<K, V> emptyMap = Collections.emptyMap();
            return emptyMap;
        }
        return Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("squid:S1168")
    public static <E> List<E> shallowCopyList(List<E> list) {
        if (list == null) {
            return null;
        }
        List<E> copy = new ArrayList(list.size());
        copy.addAll(list);
        return copy;
    }

    public static <K, V> Map<K, V> shallowCopyHashMap(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        if (map != null && !map.isEmpty()) {
            for (Entry<K, V> entry : map.entrySet()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @SuppressWarnings("squid:S1168")

    public static <E> List<E> subtractCollection(Collection<E> minuend, Collection<E> subtrahend) {
        if (minuend == null) {
            return null;
        }
        List<E> result = new ArrayList<>();
        if (subtrahend == null) {
            for (E e : minuend) {
                result.add(e);
            }
        } else {
            for (E e : minuend) {
                if (!subtrahend.contains(e)) {
                    result.add(e);
                }
            }
        }
        return result;
    }

    public static <E> List<E> unionList(List<E> listl, List<E> list2) {
        List<E> combined = new ArrayList<>();
        if (listl != null) {
            combined.addAll(listl);
        }
        if (list2 != null) {
            combined.addAll(list2);
        }
        return combined;

    }

    public static <E> String joinCollection(Collection<E> collection, String connector) {
        if (collection == null) {
            return null;
        }
        if (connector == null) {
            connector = "";
        }
        StringBuilder sb = new StringBuilder();
        int size = collection.size();
        int index = 1;
        for (E value : collection) {
            if (value == null) {
                sb.append(NULL);
            } else {
                sb.append(value.toString());
            }
            if (index++ < size) {
                sb.append(connector);
            }
        }
        return sb.toString();
    }

    public static <K, V> Map<K, V> getSubMap(Map<K, V> map, List<K> keys) {
        Map<K, V> result = new LinkedHashMap<>();
        for (K key : keys) {
            if (map.containsKey(key)) {
                result.put(key, map.get(key));
            }
        }
        return result;
    }

    public static <K, V> SortedMap<K, V> ensureTreeMap(Map<K, V> map) {
        if (map == null) {
            return null;
        }
        if (map instanceof TreeMap) {
            return (TreeMap) map;
        }
        return new TreeMap(map);
    }

    public static <K, V> Map<K, V> substituteKeyInMap(Map<K, V> map, Map<K, K> keyMap) {
        if (CollectionUtil.isEmptyMap(keyMap) || CollectionUtil.isEmptyMap(map)) {
            return new HashMap(map);
        }
        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            K newKey = keyMap.get(key);
            if (newKey == null) {
                result.put(key, entry.getValue());
            } else {
                result.put(newKey, entry.getValue());
            }
        }
        return result;
    }

    public static <E> List<E> substituteValueInList(List<E> list, Map<E, E> substitutionMap) {
        if (CollectionUtil.isEmptyList(list) || CollectionUtil.isEmptyMap(substitutionMap)) {
            return list;
        }
        List<E> result = new ArrayList<>();
        for (E e : list) {
            E sub = substitutionMap.get(e);
            if (sub == null) {
                result.add(e);
            } else {
                result.add(sub);
            }
        }
        return result;
    }

    @SuppressWarnings("squid:S1168")
    public static <E> List<E> collectionToList(Collection<E> collection) {
        if (collection == null) {
            return null;
        }
        if (collection instanceof List) {
            return (List<E>) collection;
        }
        return new ArrayList(collection);
    }

    public static <K, V> Map<V, K> switchMap(Map<K, V> map) {
        if (map == null) {
            return null;
        }

        Map<V, K> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : map.entrySet()) {
            result.put(entry.getValue(), entry.getKey()) ;
        }
        return result;
    }

    public static <E> List<E> intersectCollection(Collection<E> listl, Collection<E> list2) {
        List<E> list = new ArrayList<>();
        if (listl == null || list2 == null) {
            return list;
        }
        for (E e : listl) {
            if (list2.contains(e)) {
                list.add(e);
            }
        }
        return list;
    }

    public static <K, V> List<V> getValueList(Map<K, V> map, List<K> keys) {

        List<V> valueList = new ArrayList<>();
        if (map != null && keys != null) {
            for (K key : keys) {
                if (map.containsKey(key)) {
                    valueList.add(map.get(key));
                }
            }
        }
        return valueList;
    }

    public static <E> E[] listToObjectArray(List<E> list) {
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    private static <K, V> Map<K, V> sortByValue(Map<K, V> map, final int order) {
        List<Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Comparator<Object> comparatorInstance =
                (Object o1, Object o2) ->
                        (((Comparable<V>) ((Map.Entry<K, V>) (o1)).getValue())
                                .compareTo(((Map.Entry<K, V>) (o2)).getValue())
                                * order);
        Collections.sort(list, comparatorInstance);
        Map<K, V> result = new LinkedHashMap<>();
        for (Iterator<Entry<K, V>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<K, V> entry = it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    public static <K, V> Map<K,V> sortByValueAsd(Map<K, V> map) {
        return sortByValue(map,1);
    }

    public static <K, V> Map<K, V> sortByValueDsc(Map<K, V> map) {
        return sortByValue(map, -1);
    }

    public static Map<String, String> getKeyValueArrayAsMap(String... keyValueArr) {
        Map<String, String> additionalSearchCriteria = new LinkedHashMap<>();
        if (keyValueArr != null && keyValueArr.length > 0){
            for (int i = 0; i + 1 < keyValueArr.length; i += 2) {
                additionalSearchCriteria.put(keyValueArr[i], keyValueArr[i + 1]);
            }
        }
        return additionalSearchCriteria;
    }



}


