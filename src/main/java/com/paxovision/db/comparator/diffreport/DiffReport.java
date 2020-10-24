package com.paxovision.db.comparator.diffreport;

import com.paxovision.db.comparator.model.MapDiffResult;
import com.paxovision.db.comparator.model.StringListKey;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public interface DiffReport {


    enum OutputFormat {
        JSON(new JsonKeyedMapDiffResultToLines(2)),
        CSV(new CsvKeyedMapDiffResultToLines());
        DiffReport reportImpl;
        OutputFormat(DiffReport reportImpl) {
            this.reportImpl = reportImpl;
        }
        public DiffReport getlnstance() {
            return reportImpl;
        }
    }

    default Object distinguishNullAndBlankObject(Object v) {
        if (v == null) {
            return "(NULL)";
        } else {
            if (v instanceof String) {
                return StringUtils.isBlank(v.toString()) ?	"\"" + v + "\"" : v.toString();
            } else if (v instanceof Number) {
                return ((Number) v).doubleValue();
            }
            return StringUtils.isBlank(v.toString()) ? "\""	+ v + "\"" : v.toString();
        }
    }

    default String distinguishNullAndBlankString(Object v) {
        if (v == null) {
            return "(NULL)";
        } else {
            return StringUtils.isBlank(v.toString()) ?	"\"" + v +	"\"" : v.toString();
        }
    }

    List<String> getLinesForExistsOnlyIn1(
            MapDiffResult<StringListKey,Map<String, Object>, MapDiffResult<String, Object, Pair<Object, Object>>> result);

    List<String> getLinesForExistsOnlyIn2(
            MapDiffResult<StringListKey, Map<String, Object>,MapDiffResult<String, Object, Pair<Object, Object>>> result);

    List<String> getLinesForDuplicatesIn1(
            MapDiffResult<StringListKey,Map<String, Object>, MapDiffResult<String, Object, Pair<Object, Object>>> result);

    List<String> getLinesForDuplicatesIn2(
            MapDiffResult<StringListKey,Map<String, Object>,MapDiffResult<String, Object, Pair<Object, Object>>> result);

    List<String> getLinesForDiff(
            MapDiffResult<StringListKey,Map<String, Object>,MapDiffResult<String, Object, Pair<Object, Object>>> result);

    List<String> getLinesForInfo(
            MapDiffResult<StringListKey,Map<String, Object>,MapDiffResult<String, Object, Pair<Object, Object>>> result);

    List<String> getLinesForAll(
            MapDiffResult<StringListKey,Map<String, Object>,MapDiffResult<String, Object, Pair<Object, Object>>> result);

    List<String> getSummary(
            MapDiffResult<StringListKey,Map<String, Object>,MapDiffResult<String, Object, Pair<Object, Object>>> result);


}
