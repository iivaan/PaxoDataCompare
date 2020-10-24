package com.paxovision.db.comparator.model;

import com.paxovision.db.comparator.util.CollectionUtil;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class StringListKey implements Comparable<StringListKey>, java.io.Serializable {

    private final List<String> valueList;

    public StringListKey(List<String> valueList) {
        this.valueList = valueList;
    }

    public void addKey(String value) {
        valueList.add(value);
    }

    public StringListKey(String[] valueList) {
        this.valueList = Arrays.asList(valueList);
    }

    /* (non-lavadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */

    @Override
    public int compareTo(StringListKey obj) {
        if (obj == this) {
            return 0;
        }
        if (obj == null) {
            return 1;
        }
        List<String> trimmedValueList1 = CollectionUtil.trimTrailingEmptyValues(valueList);
        List<String> trimmedValueList2 = CollectionUtil.trimTrailingEmptyValues(obj.valueList);
        return CollectionUtil.compareList(trimmedValueList1, trimmedValueList2, false);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof StringListKey) {
            return compareTo((StringListKey) obj) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return valueList.hashCode();
    }

    @Override
    public String toString() {
        return StringUtils.join(valueList, ",");
    }

    public void intern() {
        for (int i = 0; i < valueList.size(); i++) {
            String value = valueList.get(i);
            if (value != null) {
                valueList.set(i, value.intern());
            }
        }
    }

    public List<String> getValueList() {
        return valueList;
    }
}