package com.paxovision.db.comparator.diffreport;

import com.paxovision.db.comparator.model.MapDiffResult;
import com.paxovision.db.comparator.model.StringListKey;
import com.paxovision.db.comparator.util.CollectionUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonKeyedMapDiffResultToLines  implements DiffReport {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKeyedMapDiffResultToLines.class);
    private static final String SIDE_1 = "side1";
    private static final String SIDE_2 = "side2";
    private static final String DIFF_TYPE = "diffType";

    @SuppressWarnings("squid:S3008")
    private int indentation = 2;

    public JsonKeyedMapDiffResultToLines() {
    }

    public JsonKeyedMapDiffResultToLines(int indentation) {
        this.indentation = indentation;
    }

    @Override
    public List<String> getLinesForExistsOnlyIn1(
            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {

        return getLinesForExistsOnlyInOneSide(result.getOnlyIn1(), "side1only");
    }

    @Override
    public List<String> getLinesForExistsOnlyIn2(

            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {
        return getLinesForExistsOnlyInOneSide(result.getOnlyIn2(), "side2only");
    }

    private List<String> getLinesForExistsOnlyInOneSide(
            Map<StringListKey, Map<String, Object>> onlyInOneSide, String diffType) {

        List<String> lines = new ArrayList<>();
        for (Entry<StringListKey, Map<String, Object>> entry : onlyInOneSide.entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("key", entry.getKey().toString());
            Map<String, Object> detail = entry.getValue();
            for (Entry<String, Object> entrySet : detail.entrySet()) {
                JSONObject diffObject = new JSONObject();
                if (diffType.contains(SIDE_1)) {
                    diffObject.put(SIDE_1, distinguishNullAndBlankObject(entrySet.getValue()));
                } else {
                    diffObject.put(SIDE_2, distinguishNullAndBlankObject(entrySet.getValue()));
                }
                obj.accumulate(entrySet.getKey(), diffObject);
            }
            obj.put(DIFF_TYPE, diffType);
            lines.add(obj.toString(indentation));
        }
        return lines;
    }

    @Override
    public List<String> getLinesForDuplicatesIn1(
            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {
        return getLinesForDuplicates(result.getDuplicatesIn1(), "duplicates1");
    }

    @Override
    public List<String> getLinesForDuplicatesIn2(

            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {
        return getLinesForDuplicates(result.getDuplicatesIn2(), "duplicates2");
    }

    private List<String> getLinesForDuplicates(
            Map<StringListKey, List<Map<String, Object>>> duplicates, String diffType) {

        List<String> lines = new ArrayList<>();
        for (Entry<StringListKey, List<Map<String, Object>>> entry : duplicates.entrySet()) {
            List<Map<String, Object>> duplicateRows = entry.getValue();
            for (Map<String, Object> valueMap : duplicateRows) {
                JSONObject obj = new JSONObject();
                obj.put("key", entry.getKey().toString());

                for (Entry<String, Object> entrySet : valueMap.entrySet()) {
                    JSONObject diffObject = new JSONObject();
                    if (diffType.contains("duplicates1")) {
                        diffObject.put(SIDE_1, distinguishNullAndBlankObject(entrySet.getValue()));
                    } else {
                        diffObject.put(SIDE_2, distinguishNullAndBlankObject(entrySet.getValue()));
                    }
                    obj.accumulate(entrySet.getKey(), diffObject);
                }
                obj.put(DIFF_TYPE, diffType);
                lines.add(obj.toString(indentation));
            }
        }
        return lines;
    }

    /**
     * This calculates all the column both equals + diff
     */
    @Override
    public List<String> getLinesForAll(

            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {

        Map<StringListKey, MapDiffResult<String, Object, Pair<Object, Object>>> diff =
                result.getDiff();

        List<String> lines = new ArrayList<>();
        Map<String, Integer> attributeMismatchSummary = valueMismatchSummaryByAttribute(result);

        for (Entry<StringListKey, MapDiffResult<String, Object, Pair<Object, Object>>> entry :
                diff.entrySet()) {

            JSONObject sb = new JSONObject();
            String key = entry.getKey().toString();
            sb.put("key", key);
            sb.put(DIFF_TYPE, "normal");
            MapDiffResult<String, Object, Pair<Object, Object>> value = entry.getValue();
            appendAttributesThatDiff(sb, value.getDiff(), attributeMismatchSummary);
            appendAttributesThatEqual(sb, value.getEqual());
            appendAttributesOnlyInOneSide(sb, "onlyln1", value.getOnlyIn1());
            appendAttributesOnlyInOneSide(sb, "onlyln2", value.getOnlyIn2());
            lines.add(sb.toString(indentation));
        }

        for (Entry<StringListKey, Map<String, Object>> entry : result.getOnlyIn1().entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("key", entry.getKey().toString());
            Map<String, Object> detail = entry.getValue();
            for (Entry<String, Object> entrySet : detail.entrySet()) {
                JSONObject diffObject = new JSONObject();
                diffObject.put(SIDE_1, distinguishNullAndBlankObject(entrySet.getValue()));
                obj.accumulate(entrySet.getKey(), diffObject);
            }
            obj.put(DIFF_TYPE, "side1only");
            lines.add(obj.toString(indentation));
        }

        for (Entry<StringListKey, Map<String, Object>> entry : result.getOnlyIn2().entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("key", entry.getKey().toString());
            Map<String, Object> detail = entry.getValue();
            for (Entry<String, Object> entrySet : detail.entrySet()) {
                JSONObject diffObject = new JSONObject();
                diffObject.put(SIDE_2, distinguishNullAndBlankObject(entrySet.getValue()));
                obj.accumulate(entrySet.getKey(), diffObject);
            }
            obj.put(DIFF_TYPE, "side2only");
            lines.add(obj.toString(indentation));
        }
        return lines;
    }


    @Override
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
        for (Entry<StringListKey, MapDiffResult<String, Object, Pair<Object, Object>>> entry :
            diff.entrySet())	{
            JSONObject sb = new JSONObject();
            String key = entry.getKey().toString();
            sb.put("key", key);
            sb.put(DIFF_TYPE, "normal" ) ;
            MapDiffResult<String, Object, Pair<Object, Object>> value = entry.getValue();

            appendAttributesThatDiff(sb, value.getDiff(), attributeMismatchSummary);
            appendAttributesThatEqual(sb, value.getEqual());
            appendAttributesOnlyInOneSide(sb, "onlyIn1", value.getOnlyIn1() ) ;
            appendAttributesOnlyInOneSide(sb, "onlyIn2", value.getOnlyIn2() ) ;
            lines.add(sb.toString(indentation));
        }
         return lines;
    }

    @Override
    public List<String> getLinesForInfo(

            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {

        Map<StringListKey, MapDiffResult<String, Object, Pair<Object, Object>>> diff =
                result.getDiff();
        List<String> lines = new ArrayList<>();

        for (Entry<StringListKey, MapDiffResult<String, Object, Pair<Object, Object>>> entry :
                diff.entrySet()) {
            MapDiffResult<String, Object, Pair<Object, Object>> value = entry.getValue();
            JSONObject obj = new JSONObject();
            obj.put("key", entry.getKey());
            for (Entry<String, Pair<Object, Object>> infoEntry : value.getIgnore().entrySet()) {
                String key = infoEntry.getKey();
                JSONObject colInfo = new JSONObject();
                colInfo.put(SIDE_1, distinguishNullAndBlankObject(infoEntry.getValue().getLeft()));
                colInfo.put(SIDE_2, distinguishNullAndBlankObject(infoEntry.getValue().getRight()));
                obj.accumulate(key, colInfo);
            }
            lines.add(obj.toString(indentation));
        }
        return lines;
    }


    private void appendAttributesThatEqual(JSONObject sb, Map<String, Map> equal) {
        if (!equal.isEmpty()) {
            for (Entry entry : equal.entrySet()) {
                String column = (String) entry.getKey();
                Map valMap = (Map) entry.getValue();
                Pair<Object, Object> equals = (Pair) valMap.get("valMetadata");
                JSONObject diffObject = new JSONObject();
                diffObject.put(SIDE_1, distinguishNullAndBlankObject(equals.getLeft()));
                diffObject.put(SIDE_2, distinguishNullAndBlankObject(equals.getRight()));
                diffObject.put("isEqual", "true");
                diffObject.put("condition", valMap.get("condition"));
                sb.accumulate(column, diffObject);
            }
        }
    }

    private void appendAttributesThatDiff(
            JSONObject sb,
            Map<String, Pair<Object, Object>> diff,
            Map<String, Integer> attributeMismatchSummary) {

        if (!diff.isEmpty()) {
            for (String column : attributeMismatchSummary.keySet()) {
                Pair<Object, Object> mismatch = diff.get(column);
                if (mismatch != null) {
                    JSONObject diffObject = new JSONObject();
                    diffObject.put(SIDE_1, distinguishNullAndBlankObject(mismatch.getLeft()));
                    diffObject.put(SIDE_2, distinguishNullAndBlankObject(mismatch.getRight()));
                    diffObject.put("isEqual", "false");
                    sb.accumulate(column, diffObject);
                }
            }
        }
    }

    private void appendAttributesOnlyInOneSide(
            JSONObject sb, String label, Map<String, Object> onlylnOneSide) {

        if (!onlylnOneSide.isEmpty()) {
            JSONObject obj = new JSONObject();
            for (Entry<String, Object> onlyEntry : onlylnOneSide.entrySet()) {
                Object value = onlyEntry.getValue();
                obj.put(onlyEntry.getKey(), value == null ? JSONObject.NULL : value);
            }
            sb.put(label, obj);
        }
    }

    private static Map<String, Integer> valueMismatchSummaryByAttribute(

            MapDiffResult<
                    StringListKey,
                    Map<String, Object>,
                    MapDiffResult<String, Object, Pair<Object, Object>>>
                    result) {

        // Calculate the Summary|
        Map<StringListKey, MapDiffResult<String, Object, Pair<Object, Object>>> diff =
                result.getDiff();

        Map<String, Integer> attributeToMismatchCount = new HashMap<>();
        for (Entry<StringListKey, MapDiffResult<String, Object, Pair<Object, Object>>> diffEntry :
                diff.entrySet()) {
            if (diffEntry.getValue() instanceof MapDiffResult) {
                MapDiffResult<String, Object, Pair<Object, Object>> diffEntryValue =
                        diffEntry.getValue();

                Map<String, Pair<Object, Object>> diffs = diffEntryValue.getDiff();
                Map<String, Map> equals = diffEntryValue.getEqual();
                for (Entry<String, Pair<Object, Object>> diffCellEntry : diffs.entrySet()) {
                    String attribute = diffCellEntry.getKey();
                    Integer mismatchCount = attributeToMismatchCount.get(attribute);
                    if (mismatchCount == null) {
                        attributeToMismatchCount.put(attribute, 1); // start with 1
                        // count
                    } else {
                        attributeToMismatchCount.put(attribute, mismatchCount + 1); // increment
                        // the
                        //mismatch
                        //count
                    }
                }

                for (Entry<String, Map> equalsCellEntry : equals.entrySet()) {
                    String attribute = equalsCellEntry.getKey();
                    if (!attributeToMismatchCount.containsKey(attribute)) {
                        attributeToMismatchCount.put(attribute, 0);
                    }
                }
            }
        }
        buildAttributeMismatchCountForEqual(attributeToMismatchCount, result.getEqual());
        return CollectionUtil.sortByValueDsc(attributeToMismatchCount);
    }

    private static void buildAttributeMismatchCountForEqual(
            Map<String, Integer> attributeToMismatchCount,
            Map<StringListKey, Map> equalsAtRowLevel) {

        LOGGER.info("rows sampling equality size = {}", equalsAtRowLevel.size());
        if (!equalsAtRowLevel.isEmpty()) {
            Entry<StringListKey, Map> equalsEntry = equalsAtRowLevel.entrySet().iterator().next();
            Map metadataMap = equalsEntry.getValue();
            Map dataMap = (Map) ((Pair) metadataMap.get("valMetadata")).getLeft();
            Iterator iter = dataMap.keySet().iterator();
            while (iter.hasNext()) {
                String attribute = (String) iter.next();
                if (!attributeToMismatchCount.containsKey(attribute)) {
                    attributeToMismatchCount.put(attribute, 0);
                }
            }
        }
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
        int sizeOnlyIn1 = result.getOnlyIn1Count();
        int sizeOnlyIn2 = result.getOnlyIn2Count();
        int sizeDiff = result.getDiffCount();
        int sizeEqual = result.getEqualCount();
        int sizeDuplicates1 = result.getDuplicatesIn1().size();
        int sizeDuplicates2 = result.getDuplicatesIn2().size();

        JSONObject summary = new JSONObject();
        JSONObject uniqueRows = new JSONObject();
        uniqueRows.put(SIDE_1, dataSizeIn1);
        uniqueRows.put(SIDE_2, dataSizeIn2);
        JSONObject rowsOnlyIn = new JSONObject();
        rowsOnlyIn.put(SIDE_1, sizeOnlyIn1);
        rowsOnlyIn.put(SIDE_2, sizeOnlyIn2);
        summary.put("rowsWithSameValue", sizeEqual);
        summary.put("rowsWithDifferentValue", sizeDiff);

        JSONObject duplicateKeys = new JSONObject();
        duplicateKeys.put(SIDE_1, sizeDuplicates1);
        duplicateKeys.put(SIDE_2, sizeDuplicates2);
        summary.put("uniqueRows", uniqueRows);
        summary.put("rowsOnlyln", rowsOnlyIn);
        summary.put("duplicateKeys", duplicateKeys);

        boolean reachedMaxDiff = false;
        if (result.getDiffLimiter().getMaxDiff() != -1
                && result.getDiffCount() > result.getDiffLimiter().getMaxDiff()) {
            reachedMaxDiff = true;
        }

        summary.put("reachedMaxDiff", reachedMaxDiff);
        Map<String, Integer> attributeMismatchSummary = valueMismatchSummaryByAttribute(result);
        JSONObject valueMismatchByAttribute = new JSONObject(attributeMismatchSummary);

        summary.put("valueMismatchByAttribute", valueMismatchByAttribute);
        List<String> lines = new ArrayList<>();
        lines.add(summary.toString(indentation));
        return lines;
    }
}