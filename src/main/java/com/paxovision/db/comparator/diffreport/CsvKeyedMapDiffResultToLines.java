package com.paxovision.db.comparator.diffreport;

import com.paxovision.db.comparator.model.MapDiffResult;
import com.paxovision.db.comparator.model.StringListKey;
import com.paxovision.db.comparator.util.CollectionUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 *	This classes supposed to extract the diff result in csv format, but need few enhancement, eg: how
 *	to handle if the value has commas
 */
public class CsvKeyedMapDiffResultToLines implements DiffReport{

    @Override
    public List<String> getLinesForExistsOnlyIn1(
            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {
        return getLinesForExistsOnlyInOneSide(result.getOnlyIn1());
    }

    @Override
    public List<String> getLinesForExistsOnlyIn2(
            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {
        return getLinesForExistsOnlyInOneSide(result.getOnlyIn2());
    }

    private List<String> getLinesForExistsOnlyInOneSide(
        Map<StringListKey, Map<String, Object>> onlyInOneSide) {

        List<String> lines = new ArrayList<>();
        List<String> columns = getColumns(onlyInOneSide);
        // header
        String header = "Key," + StringUtils.join(columns,",");
        lines.add(header);

        // add rows
        for (Entry<StringListKey, Map<String, Object>> entry : onlyInOneSide.entrySet()) {
            StringBuilder sb = new StringBuilder(entry.getKey().toString().replace(",", "~"));
            for (String column : columns) {
                sb.append(",");
                sb.append(entry.getValue().get(column));
            }
            lines.add(sb.toString());
        }
        return lines;
    }

    private List<String> getColumns(Map<StringListKey, Map<String, Object>> map) {
        List<String> headers = new ArrayList<>();
        if (!map.isEmpty()) {
            for (Entry<StringListKey, Map<String, Object>> rowEntry : map.entrySet()) {
                headers.addAll(rowEntry.getValue().keySet());
                if (!headers.isEmpty()) {
                    break;
                }
            }
        }
        return headers;
    }

    @Override
    public List<String> getLinesForDuplicatesIn1(
            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {
        return getLinesForDuplicates(result.getDuplicatesIn1());
    }

    @Override
    public List<String> getLinesForDuplicatesIn2(
            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {
        return getLinesForDuplicates(result.getDuplicatesIn2());
    }

    private List<String> getLinesForDuplicates(Map<StringListKey, List<Map<String, Object>>> duplicates) {
        List<String> columns = getHeader(duplicates);
        List<String> lines = new ArrayList<>();
        for (Entry<StringListKey, List<Map<String, Object>>> duplicateEntry :
                duplicates.entrySet()) {
            List<Map<String, Object>> duplicateRows = duplicateEntry.getValue();
            // header
            lines.add("Keys," + StringUtils.join(columns, ","));
            // rows
            for (Map<String, Object> valueMap : duplicateRows) {
                StringBuilder sb = new StringBuilder();
                sb.append(duplicateEntry.getKey().toString().replace(",","~"));
                for (String column : columns) {
                    sb.append("?");
                    Object value = valueMap.get(column);
                    sb.append(value);
                }
                lines.add(sb.toString());
            }
        }
        return lines;
    }

    private List<String> getHeader(Map<StringListKey, List<Map<String, Object>>> duplicates) {
        List<String> columns = new ArrayList<>();
        for (Entry<StringListKey, List<Map<String, Object>>> duplicateEntry :
                duplicates.entrySet()) {
            List<Map<String, Object>> duplicateRows = duplicateEntry.getValue();
            for (Map<String, Object> valueMap : duplicateRows) {
                columns.addAll(valueMap.keySet());
                if (!columns.isEmpty()) {
                    break;
                }
            }
            if (!columns.isEmpty()) {
                break;
            }
        }
        return columns;
    }

    public List<String> getLinesForAll(
            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {
        throw new NotImplementedException("Not implemented");
    }

    public List<String> getLinesForInfo(
            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {
        throw new NotImplementedException("Not implemented");
    }

    public List<String> getLinesForDiff(

            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {

        Map<StringListKey, MapDiffResult<String, Object, Pair<Object, Object>>> diff =
                result.getDiff();
        List<String> lines = new ArrayList<>();

        Map<String, Integer> attributeMismatchSummary = valueMismatchSummaryByAttribute(result);
        StringBuilder header = new StringBuilder();
        header.append("Key");
        for (String attribute : attributeMismatchSummary.keySet()) {
            header.append(",");
            header.append(attribute);
            header.append("_LEFT");
            header.append(",");
            header.append(attribute);
            header.append("_RIGHT");
        }

        lines.add(header.toString());
        for (Entry<StringListKey, MapDiffResult<String, Object, Pair<Object, Object>>> entry :
                diff.entrySet()) {

            StringListKey key = entry.getKey();
            MapDiffResult<String, Object, Pair<Object, Object>> value = entry.getValue();
            StringBuilder sb = new StringBuilder(key.toString().replace(",", "~"));
            appendAttributesThatDiff(sb, value.getDiff(), attributeMismatchSummary);
            appendAttributesOnlyInOneSide(sb, "onlylnl", value.getOnlyIn1());
            appendAttributesOnlyInOneSide(sb, "onlyln2", value.getOnlyIn2());
            lines.add(sb.toString());
        }
        return lines;
    }

    private void appendAttributesThatDiff(
            StringBuilder sb,
            Map<String, Pair<Object, Object>> diff,
            Map<String, Integer> attributeMismatchSummary) {

        if (!diff.isEmpty()) {
            for (String column : attributeMismatchSummary.keySet()) {
                Pair<Object, Object> mismatch = diff.get(column);
                sb.append(",");
                if (mismatch != null) {
                    sb.append(distinguishNullAndBlankString(mismatch.getLeft()));
                    sb.append(",");
                    sb.append(distinguishNullAndBlankString(mismatch.getRight()));
                } else {
                    sb.append(",");
                }
            }
        }
    }

    private void appendAttributesOnlyInOneSide(
            StringBuilder sb, String label, Map<String, Object> onlylnOneSide) {

        if (!onlylnOneSide.isEmpty()) {
            sb.append(" ").append(label).append("|");
            for (Entry<String, Object> onlyEntry : onlylnOneSide.entrySet()) {
                sb.append(onlyEntry.getKey()).append("=").append(onlyEntry.getValue()).append("|");
            }
        }
    }

    private static Map<String, Integer> valueMismatchSummaryByAttribute(

            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {

        // Calculate the Summary
        Map<StringListKey, MapDiffResult<String, Object, Pair<Object, Object>>> diff = result.getDiff();
        Map<String, Integer> attributeToMismatchCount = new LinkedHashMap<>();
        for (Entry<StringListKey, MapDiffResult<String, Object, Pair<Object, Object>>> diffEntry:
             diff.entrySet()) {

            MapDiffResult<String, Object, Pair<Object, Object>> diffEntryValue =
                    diffEntry.getValue();

            Map<String, Pair<Object, Object>> diffs = diffEntryValue.getDiff();
            for (Entry<String, Pair<Object, Object>> diffCellEntry : diffs.entrySet()) {
                String attribute = diffCellEntry.getKey();
                Integer mismatchCount = attributeToMismatchCount.get(attribute);
                if (mismatchCount == null) {
                    attributeToMismatchCount.put(attribute, 1); // start with 1
                    // count
                } else {
                    attributeToMismatchCount.put(attribute, mismatchCount + 1); // increment
                    // the
                    // mismatch
                    // count
                }
            }
        }
        return CollectionUtil.sortByValueDsc(attributeToMismatchCount);
    }

    @Override
    public List<String> getSummary(

            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {

        int dataSizeIn1 = result.getSizeIn1();
        int dataSizeIn2 = result.getSizeIn2();
        int sizeOnlyIn1 = result.getOnlyIn1().size();
        int sizeOnlyIn2 = result.getOnlyIn2().size();
        int sizeDiff = result.getDiff().size();
        int sizeEqual = result.getEqual().size();
        int sizeDuplicates1 = result.getDuplicatesIn1().size();
        int sizeDuplicates2 = result.getDuplicatesIn2().size();

        List<String> lines = new ArrayList<>();
        lines.add("side,uniqueRows,rowsOnlyIn,duplicateKeys");
        lines.add(String.format("sidel,%d,%d,%d,", dataSizeIn1, sizeOnlyIn1, sizeDuplicates1));
        lines.add(String.format("side2,%d,%d,%d,", dataSizeIn2, sizeOnlyIn2, sizeDuplicates2));
        lines.add("rowsWithDifferentValue,rowsWithSameValue");
        lines.add(String.format("%d,%d", sizeDiff, sizeEqual));

        Map<String, Integer> attributeMismatchSummary = valueMismatchSummaryByAttribute(result);
        lines.add("valueMismatchByAttribute,count");

        for (Map.Entry<String, Integer> entry : attributeMismatchSummary.entrySet()) {
            lines.add(String.format("%s,%d", entry.getKey(), entry.getValue()));
        }
        return lines;
    }

}


