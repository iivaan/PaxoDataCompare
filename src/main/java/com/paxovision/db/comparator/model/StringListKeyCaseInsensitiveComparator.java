package com.paxovision.db.comparator.model;

import com.paxovision.db.comparator.util.CollectionUtil;
import java.util.Comparator;
import java.util.List;

public class StringListKeyCaseInsensitiveComparator implements Comparator<StringListKey>, java.io.Serializable {
    public int compare(StringListKey s1, StringListKey s2) {
        if (s1.equals(s2)) {
            return 0;
        } else {
            List<String> valueList1 = s1.getValueList();
            List<String> valueList2 = s2.getValueList();
            List<String> trimmedValueList1 = CollectionUtil.trimTrailingEmptyValues(valueList1);
            List<String> trimmedValueList2 = CollectionUtil.trimTrailingEmptyValues(valueList2);
            return CollectionUtil.compareList(trimmedValueList1, trimmedValueList2, true);
        }
    }
}
