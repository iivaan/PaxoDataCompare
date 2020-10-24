package com.paxovision.db.comparator;

import com.google.common.collect.ImmutableMap;
import com.paxovision.db.comparator.context.TestContext;
import com.paxovision.db.comparator.diffreport.DiffReport;
import com.paxovision.db.comparator.diffreport.JsonKeyedMapDiffResultToLines;
import com.paxovision.db.comparator.equivalence.CaseInsensitiveEquivalence;
import com.paxovision.db.comparator.equivalence.Equivalence;
import com.paxovision.db.comparator.equivalence.NumberEquivalence;
import com.paxovision.db.comparator.equivalence.ToleranceEquivalence;
import com.paxovision.db.comparator.model.DiffConfig;
import com.paxovision.db.comparator.model.MapDiffResult;
import com.paxovision.db.comparator.model.StringListKey;
import com.paxovision.db.comparator.processor.KeyedMapBuilderImpl;
import com.paxovision.db.comparator.processor.KeyedMapDiffProcessor;
import com.paxovision.db.comparator.util.JvmUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.FieldComparisonFailure;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EquivalenceComparator implements com.paxovision.db.comparator.Comparator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquivalenceComparator.class);
    private final ComparisonConfig comparisonConfig;
    private final DataSet dataSet1;
    private final DataSet dataSet2;
    private DiffConfig diffConfig;
    private MapDiffResult<
            StringListKey,
            Map<String, Object>,
            MapDiffResult<String, Object, Pair<Object, Object>>> diffResult;

    /**
     * Equivalene Comparator - Much of ideas are still extracted from qa-common and removed
     * unnecessary class/variable like context
     *
     * @param comparisonConfig Instructions/Comparison Config
     * @param dataSet1         Data Set 1 ( Map or ISON string )
     * @param dataSet2         Data Set 2 ( Map or ISON string )
     */
    public EquivalenceComparator(ComparisonConfig comparisonConfig, DataSet dataSet1, DataSet dataSet2) {
        this.comparisonConfig = comparisonConfig;
        this.dataSet1 = dataSet1;
        this.dataSet2 = dataSet2;
    }

    public void printResults() {
        LOGGER.info("{}", getDiffResult(diffResult));
    }

    private void debug(String msg) {
        LOGGER.info(msg);
    }

    private String getJsonDataFromDataSet(DataSet dataSet) {
        StringBuilder jsonStringData = new StringBuilder();
        dataSet.getData().stream().forEach(jsonStringData::append);
        return jsonStringData.toString();
    }

    @javax.annotation.ParametersAreNonnullByDefault
    private String extractJsonAttr(String key) {
        if (key.indexOf('.') >= 0) {
            return key.substring(key.lastIndexOf('.') + 1);
        }
        return key;
    }


    /**
     * Compare 2 json string using JSONAssert Need to find a way to standardize the result
     *
     * @return
     */
    private MapDiffResult compareAsJSON() {
        Map<String, Equivalence> customEquivalences = diffConfig.getAllEquivalence();
        List<Customization> customizationsList = new ArrayList<>();
        customEquivalences.forEach((k, v) -> customizationsList.add(new Customization(k, v)));
        JSONCompareResult result =
                JSONCompare.compareJSON(
                        getJsonDataFromDataSet(dataSet1),
                        getJsonDataFromDataSet(dataSet2),
                        new CustomComparator(
                                comparisonConfig.getJsonCompareMode() == null
                                        ? JSONCompareMode.LENIENT
                                        : comparisonConfig.getJsonCompareMode(),
                                customizationsList.toArray(new Customization[0])));

        diffResult = MapDiffResult.newlnstance();

        if (result.failed()) {
            List<FieldComparisonFailure> comparisonFailures = result.getFieldUnexpected();
            for (FieldComparisonFailure failure : comparisonFailures) {
                diffResult.addOnlyIn2(
                        new StringListKey(Arrays.asList(failure.getField())),
                        ImmutableMap.of(extractJsonAttr(failure.getField()), failure.getActual()));
            }

            List<FieldComparisonFailure> failures = result.getFieldFailures();
            for (FieldComparisonFailure failure : failures) {
                MapDiffResult local = MapDiffResult.newlnstance();
                local.addDiff(
                        extractJsonAttr(failure.getField()),
                        Pair.of(failure.getExpected(), failure.getActual()));
                diffResult.addDiff(new StringListKey(Arrays.asList(failure.getField())), local);
            }

            List<FieldComparisonFailure> missings = result.getFieldMissing();
            for (FieldComparisonFailure failure : missings) {
                diffResult.addOnlyIn1(
                        new StringListKey(Arrays.asList(failure.getField())),
                        ImmutableMap.of(
                                extractJsonAttr(failure.getField()), failure.getExpected()));
            }
        }

        try {
            JSONAssert.assertEquals(
                    getJsonDataFromDataSet(dataSet1),
                    getJsonDataFromDataSet(dataSet2),
                    new CustomComparator(
                            comparisonConfig.getJsonCompareMode() == null
                                    ? JSONCompareMode.LENIENT
                                    : comparisonConfig.getJsonCompareMode(),
                            customizationsList.toArray(new Customization[0])));
            LOGGER.info("json compare is done");
        } catch (AssertionError e) {
            LOGGER.error("{}", e);
        }
        return diffResult;

    }

    /**
    *	Compare as list of map for 2 data set
    *
    *	<p>Sometimes it can performs comparison in chunk mode to avoid OOM
    *
    * (©return MapDiffResult
    */
    private MapDiffResult compareAsMap() {

        JvmUtil.printMemoryStats();
        diffResult = getCompareResult(this.dataSet1.getData(), this.dataSet2.getData());
        debug("finished diff");
        JvmUtil.printMemoryStats();
        return diffResult;
    }

    private MapDiffResult getCompareResult(List dataSet1, List dataSet2) {

        debug("start preparing the keyedmap for size " + dataSet1.size());
        KeyedMapDiffProcessor comparator =
                new KeyedMapDiffProcessor(addDataToConfig(dataSet1), addDataToConfig(dataSet2));
        debug("finished preparing the keyedmap");
        JvmUtil.printMemoryStats();
        return comparator.diff(diffConfig);
    }

    private boolean isJSON() {
        if (this.dataSet1.getData() != null && !this.dataSet1.getData().isEmpty()) {
            Object sample = this.dataSet1.getData().get(0);
            return sample instanceof String || sample instanceof JSONObject;
        }
        return false;
    }

    @Override
    public MapDiffResult compare() {
        init();

        if (isJSON()) {
            LOGGER.info("comparing as json");
            diffResult = compareAsJSON();
        } else {
            diffResult = compareAsMap();
        }
        TestContext.CONTEXT.setDiffResult(diffResult);
        TestContext.CONTEXT.setConfig(comparisonConfig);

        // if the method called from raptor-compare-web , it will NOT throw assertion Error
        if (comparisonConfig.isThrowAssertionErrorOnDiff() && hasDifference(diffResult)) {
            throw new AssertionError(getDiffResult(diffResult));
        }
        if (comparisonConfig.isPrintResultsOnSuccess()) {
            printResults();
        }
        return diffResult;
    }

    private boolean hasDifference(MapDiffResult diffResult) {
        boolean hasDifferent = !diffResult.isEqual();

        if (comparisonConfig.shouldErrorOnDuplicate() && !hasDifferent) {
            hasDifferent =
                    !diffResult.getDuplicatesIn1().isEmpty()
                            || !diffResult.getDuplicatesIn2().isEmpty();
        }
        return hasDifferent;
    }

    private String getDiffResult(MapDiffResult diffResult) {

        StringBuilder builder = new StringBuilder();
        DiffReport instance = null;
        if (comparisonConfig.getCompareOutputFormat() == null) {
            instance = new JsonKeyedMapDiffResultToLines(2);
        } else {
            instance = comparisonConfig.getCompareOutputFormat().getlnstance();
        }
        builder.append("\n------Diff Summary------\n");
        builder.append(instance.getSummary(diffResult) + "\n");

        builder. append ("------Diff------\n");
        builder.append(instance.getLinesForDiff(diffResult) + "\n");

        builder.append("------Rows in Side1 only------“\n");
        builder.append(instance.getLinesForExistsOnlyIn1(diffResult) + "\n");

        builder.append("------Rows in Side2 only------\n");
        builder.append(instance.getLinesForExistsOnlyIn2(diffResult) + "\n");

        builder.append("------Duplicates in Side1------\n");
        builder.append(instance.getLinesForDuplicatesIn1(diffResult) + "\n");

        builder.append("------Duplicates in Side2------\n");
        builder.append(instance.getLinesForDuplicatesIn2(diffResult) + "\n");

        builder.append("----- -Info-------\n");
        builder.append(instance.getLinesForInfo(diffResult) + "\n");

        return builder.toString();
    }


    private void init() {

        DiffConfig.Builder diffConfigBuilder = new DiffConfig.Builder();

        if (comparisonConfig.isCaseInsensitiveColumn()) {
            diffConfigBuilder.setCaseInsensitiveColumn(comparisonConfig.isCaseInsensitiveColumn());
        }

        if (comparisonConfig.getIgnoreColumns() != null) {
            diffConfigBuilder.setIgnoreColumns(comparisonConfig.getIgnoreColumns());
        }

        if (comparisonConfig.getInfoColumns() != null) {
            diffConfigBuilder.setInfoColumns(comparisonConfig.getInfoColumns());
        }

        if (comparisonConfig.getCompareColumns() != null) {
            diffConfigBuilder.setCompareColumns(comparisonConfig.getCompareColumns());
        }

        if (comparisonConfig.getNumericAttributes() != null) {
            comparisonConfig
                    .getNumericAttributes()
                    .forEach(k -> diffConfigBuilder.addEquivalence(k, new NumberEquivalence()));
        }

        if (comparisonConfig.getAttributeTolerance() != null) {
            comparisonConfig
                    .getAttributeTolerance()
                    .forEach(
                            (attr, tolerance) ->
                                    diffConfigBuilder.addEquivalence(
                                            attr, new ToleranceEquivalence(tolerance)));
        }

        if (comparisonConfig.getCaseInsensitiveCompareAttributes() != null) {
            comparisonConfig
                    .getCaseInsensitiveCompareAttributes()
                    .forEach(
                            k ->
                                    diffConfigBuilder.addEquivalence(
                                            k, new CaseInsensitiveEquivalence()));
        }

        if (comparisonConfig.getCustomEquivalence() != null) {
            comparisonConfig.getCustomEquivalence().forEach(diffConfigBuilder::addEquivalence);
        }

        if (comparisonConfig.getValueTransform() != null) {
            comparisonConfig.getValueTransform().forEach(diffConfigBuilder::addTransform);
        }

        DiffConfig.DiffLimiter diffLimiter = new DiffConfig.DiffLimiter();
        diffLimiter.setMaxDiff(comparisonConfig.getMaxDiff() );
        diffLimiter.setMaxEqual(comparisonConfig.getMaxEqual());
        diffLimiter.setMaxOnlyIn1(comparisonConfig.getMaxOnlyIn1());
        diffLimiter.setMaxOnlyIn2(comparisonConfig.getMaxOnlyIn2());
        diffConfigBuilder.setDiffLimiter(diffLimiter);

        diffConfigBuilder.setIgnoreMissingNullAttribute(
                comparisonConfig.isIgnoreMissingNullAttribute());

        diffConfigBuilder.setExcludeDuplicates(comparisonConfig.shouldExcludeOnDuplicate());

        diffConfig = diffConfigBuilder.build();
    }

    private KeyedMapBuilderImpl addDataToConfig( List<Map<String, Object>> dataSet) {
        KeyedMapBuilderImpl keyMapBuiler =
                new KeyedMapBuilderImpl(
                        comparisonConfig.getCompareKey(), diffConfig, dataSet.size());

        JvmUtil.printMemoryStats();
        dataSet.stream().forEach(keyMapBuiler::add);
        debug("data set size = " + dataSet.size());
        JvmUtil.printMemoryStats();
        return keyMapBuiler;
    }


}