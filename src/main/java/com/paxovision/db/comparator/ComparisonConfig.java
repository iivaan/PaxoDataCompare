package com.paxovision.db.comparator;

import com.google.common.base.Splitter;
import com.paxovision.db.comparator.diffreport.DiffReport;
import com.paxovision.db.comparator.diffreport.DiffReport.OutputFormat;
import com.paxovision.db.comparator.equivalence.Equivalence;
import com.paxovision.db.comparator.equivalence.SpringExpressionEquivalence;
import com.paxovision.db.comparator.transform.NumberTransformation;
import com.paxovision.db.comparator.transform.SoftNullToNumberTransformation;
import com.paxovision.db.comparator.transform.SpringExpressionTransformation;
import com.paxovision.db.comparator.transform.Transform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import org.skyscreamer.jsonassert.JSONCompareMode;
public class ComparisonConfig {
    private List<String> compareKey;
    private List<String> numericAttributes;
    private List<String> caseInsensitiveCompareAttributes;
    private List<String> infoColumns;
    private List<String> ignoreColumns;
    private List<String> compareColumns;
    private Map<String, String> attributeTolerance;
    private Map<String, Equivalence<?>> customEquivalences;
    private Map<String, Transform<?>> valueTransform;
    private boolean errorOnDuplicate;
    private boolean excludeOnDuplicate;
    private boolean printResultsOnSuccess;
    private boolean caseInsensitiveColumn;
    private boolean ignoreMissingNullAttribute;
    private boolean throwAssertionErrorOnDiff = true;
    private String comparatorWebApiURL;
    private String comparatorWebUiURL;
    protected int maxEqual = -1;
    protected int maxDiff = -1;
    protected int maxOnlyIn1 = -1;
    protected int maxOnlyIn2 = -1;

    // only for json
    private JSONCompareMode jsonCompareMode;
    private DiffReport.OutputFormat compareOutputFormat;
    private String compareOutputPath;


    public int getMaxEqual() {
        return maxEqual;
    }

    public int getMaxDiff() {
        return maxDiff;
    }

    public int getMaxOnlyIn1(){
        return maxOnlyIn1;
    }

    public int getMaxOnlyIn2() {
        return maxOnlyIn2;
    }

    public JSONCompareMode getJsonCompareMode() {
        return jsonCompareMode;
    }

    public String getComparatorWebApiURL() {
        return comparatorWebApiURL;
    }

    public String getComparatorWebUiURL() {
        return comparatorWebUiURL;
    }

    public OutputFormat getCompareOutputFormat() {
        return compareOutputFormat;
    }

    public boolean isThrowAssertionErrorOnDiff() {
        return throwAssertionErrorOnDiff;
    }

    public boolean isIgnoreMissingNullAttribute() {
        return ignoreMissingNullAttribute;
    }

    public String getCompareOutputPath() {
        return compareOutputPath;
    }

    public boolean isCaseInsensitiveColumn() {
        return caseInsensitiveColumn;
    }

    public List<String> getCompareKey() {
        return compareKey;
    }

    public List<String> getNumericAttributes() {
        return numericAttributes;
    }

    public List<String> getCaseInsensitiveCompareAttributes() {
        return caseInsensitiveCompareAttributes;
    }

    public boolean shouldErrorOnDuplicate() {
        return errorOnDuplicate;
    }

    // when this flag is on , it will not compare duplicate keys
    public boolean shouldExcludeOnDuplicate() {
        return excludeOnDuplicate;
    }

    @SuppressWarnings("squid:S1452")
    public Map<String, Equivalence<?>> getCustomEquivalence() {
        return customEquivalences;
    }

    @SuppressWarnings("squid:S1452")

    public Map<String, Transform<?>> getValueTransform() {
        return valueTransform;
    }

    public Map<String, String> getAttributeTolerance() {
        return attributeTolerance;
    }

    public List<String> getInfoColumns() {
        return infoColumns;
    }

    public List<String> getIgnoreColumns() {
        return ignoreColumns;
    }

    public List<String> getCompareColumns() {
        return compareColumns;
    }

    public boolean isPrintResultsOnSuccess() {
        return printResultsOnSuccess;
    }

    /**
     * This is set of attributes for instructions to tell the comparator what to behave based on the
     * settings
     *
     * @param compareKey                       Compare/Primary key to use as comparison between 2 data set
     * @param numericAttributes                Attributes to be compared as number
     * @param ignoreColumns                    Attributes to be ignored in comparisons and will not show up in the
     *                                         comparison result
     * @param caseInsensitiveCompareAttributes Attributes to be compared as ignore case
     * @param attributeTolerance               Attributes tolerance settings. Will work only on number
     * @param customEquivalences               Custom Equivalence logic for specific attributes. ( Used in
     *                                         EquivalenceComparator )
     * @param valueTransform                   Rule to transform the value
     * @param errorOnDuplicate                 Flag indicating the comparator should throw error on duplicates found
     *                                         in its own dataset
     * @param excludeOnDuplicate               Flag to exclude duplicate records from comparisons
     * @param printResultsOnSuccess            Flag indicating whether to print comparison result even on
     *                                         success
     * @param caseInsensitiveColumn            Flag indicating to treat attributes/column as ignore case
     * @param compareOutputFormat              Specify Output Format
     * @param compareOutputPath                Specify Output Path , if Path are specified results will be stored
     *                                         in the specified value
     * @param comparatorWebUiURL               Raptor Compare Web UI URL
     * @param comparatorWebApiURL              Raptor Compare API URL
     * @param throwAssertionErrorOnDiff        Flag inidicating whether we should throw the diff exception
     * @param jsonCompareMode                  ISON Compare Mode
     * @param maxEqual                         Max of number equals
     * @param maxDiff                          Max of number diff
     * @param maxOnlyIn1                       Max of number onlyl
     * @param maxOnlyIn2                       Max of number only2
     * @param compareColumns                   List of Inclusive Columns Only to be compared in comparisons
     */
    @SuppressWarnings("squid:S00107")

    public ComparisonConfig(
            List<String> compareKey,
            List<String> numericAttributes,
            List<String> infoColumns,
            List<String> ignoreColumns,
            List<String> caseInsensitiveCompareAttributes,
            Map<String, String> attributeTolerance,
            Map<String, Equivalence<?>> customEquivalences,
            Map<String, Transform<?>> valueTransform,
            boolean errorOnDuplicate,
            boolean excludeOnDuplicate,
            boolean printResultsOnSuccess,
            boolean caseInsensitiveColumn,
            DiffReport.OutputFormat compareOutputFormat,
            String compareOutputPath,
            String comparatorWebUiURL,
            String comparatorWebApiURL,
            boolean throwAssertionErrorOnDiff,
            JSONCompareMode jsonCompareMode,
            boolean ignoreNullAttribute,
            int maxEqual,
            int maxDiff,
            int maxOnlyIn1,
            int maxOnlyIn2,
            List<String> compareColumns) {

        this.compareKey = compareKey;
        this.numericAttributes = numericAttributes;
        this.caseInsensitiveCompareAttributes = caseInsensitiveCompareAttributes;
        this.attributeTolerance = attributeTolerance;
        this.infoColumns = infoColumns;
        this.ignoreColumns = ignoreColumns;
        this.errorOnDuplicate = errorOnDuplicate;
        this.excludeOnDuplicate = excludeOnDuplicate;
        this.customEquivalences = customEquivalences;
        this.valueTransform = valueTransform;
        this.printResultsOnSuccess = printResultsOnSuccess;
        this.caseInsensitiveColumn = caseInsensitiveColumn;
        this.compareOutputFormat = compareOutputFormat;
        this.compareOutputPath = compareOutputPath;
        this.comparatorWebUiURL = comparatorWebUiURL;
        this.comparatorWebApiURL = comparatorWebApiURL;
        this.throwAssertionErrorOnDiff = throwAssertionErrorOnDiff;
        this.jsonCompareMode = jsonCompareMode;
        this.ignoreMissingNullAttribute = ignoreNullAttribute;
        this.maxEqual = maxEqual;
        this.maxDiff = maxDiff;
        this.maxOnlyIn1 = maxOnlyIn1;
        this.maxOnlyIn2 = maxOnlyIn2;
        this.compareColumns = compareColumns;
    }


    public static class ComparisonConfigBuilder {

        protected List<String> compareKey;
        protected List<String> numericAttributes;
        protected List<String> caseInsensitiveCompareAttributes;
        protected List<String> infoColumns;
        protected List<String> ignoreColumns;
        protected Map<String, String> attributeTolerance;
        protected Map<String, Equivalence<?>> customEquivalences;
        protected Map<String, Transform<?>> valueTransform;
        protected boolean errorOnDuplicate;
        protected boolean excludeOnDuplicate;
        protected boolean printResultsOnSuccess;
        protected boolean caseInsensitiveColumn;
        protected DiffReport.OutputFormat compareOutputFormat;
        protected String compareOutputPath;
        protected String comparatorWebUiURL;
        protected String comparatorWebApiURL;
        protected boolean throwAssertionErrorOnDiff = true;
        protected JSONCompareMode jsonCompareMode;
        protected boolean ignoreMissingNullAttribute;

        protected int maxEqual = -1;
        protected int maxDiff = -1;
        protected int maxOnlyIn1 = -1;
        protected int maxOnlyIn2 = -1;

        protected List<String> compareColumns;

        /**
         * Sets the default value for the compareKey property.
         *
         * @param values the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withCompareKey(
                String... values) {

            if (this.compareKey == null) {
                this.compareKey = new ArrayList<>();
            }
            for (String str : values) {
                if (StringUtils.isNotBlank(str)) {
                    Splitter.on("j")
                            .split(str)
                            .forEach(splitVal -> this.compareKey.add(StringUtils.trim(splitVal)));
                }
            }
            return this;
        }

        /**
         * Sets the default value for the numericAttributes property.
         *
         * @param values the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withNumericAttributes(String... values) {
            if (this.numericAttributes == null) {
                this.numericAttributes = new ArrayList<>();
            }
            for (String str : values) {
                if (StringUtils.isNotBlank(str)) {
                    Splitter.on(",")
                            .split(str)
                            .forEach(splitVal -> this.numericAttributes.add(splitVal));
                }
            }
            return this;
        }

        /**
         * Sets the default value for the infoColumns property. Info columns is used to provide
         * context
         *
         * @param values the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withInfoColumns(
                String... values) {
            if (this.infoColumns == null) {
                this.infoColumns = new ArrayList<>();
            }
            for (String str : values) {
                if (StringUtils.isNotBlank(str)) {
                    Splitter.on(",").split(str).forEach(splitVal -> this.infoColumns.add(splitVal));
                }

            }
            return this;
        }

        /**
         * Sets the default value for the compareColumns property. Compare Columns is used to
         * compare attributes specified only
         *
         * @param values the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withCompareColumns(String... values) {

            if (this.compareColumns == null) {
                this.compareColumns = new ArrayList<>();
            }

            for (String str : values) {
                if (StringUtils.isNotBlank(str)) {
                    Splitter.on(",")
                            .split(str)
                            .forEach(splitVal -> this.compareColumns.add(splitVal));
                }
            }
            return this;
        }

        /**
         * Sets the default value for the ignoreColumns property. Ignore columns will be excluded
         * from
         *
         * @param values the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withIgnoreColumns(
                String... values) {

            if (this.ignoreColumns == null) {
                this.ignoreColumns = new ArrayList<>();
            }
            for (String str : values) {
                if (StringUtils.isNotBlank(str)) {
                    Splitter.on(",")
                            .split(str)
                            .forEach(splitVal -> this.ignoreColumns.add(splitVal));
                }
            }
            return this;
        }

        /**
         * Sets the default value for the caselnsensitiveCompareAttributes property.
         *
         * @param values the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withCaseInsensitiveCompareAttributes(String... values) {

            if (this.caseInsensitiveCompareAttributes == null) {
                this.caseInsensitiveCompareAttributes = new ArrayList<>();
            }

            for (String str : values) {
                if (StringUtils.isNotBlank(str)) {
                    Splitter.on(",")
                            .split(str)
                            .forEach(
                                    splitVal ->
                                            this.caseInsensitiveCompareAttributes.add(splitVal));
                }
            }
            return this;
        }

        /**
         * Sets the default value for the attributeTolerance property.
         *
         * @param values the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withAttributeTolerance(Map<String, String>... values) {

            if (this.attributeTolerance == null) {
                this.attributeTolerance = new HashMap<>();
            }

            for (int i = 0; i < values.length; i++) {
                this.attributeTolerance.putAll(values[i]);
            }
            return this;
        }

        /**
         * Sets the default value for the attributeTolerance property.
         *
         * @param column    column to be
         * @param tolerance Tolerance
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withAttributeTolerance(String column, String tolerance) {
            if (this.attributeTolerance == null) {
                this.attributeTolerance = new HashMap<>();
            }

            if (StringUtils.isNotBlank(column)) {
                Splitter.on(",")
                        .split(column)
                        .forEach(
                                splitVal -> {
                                    Map attributeMap = new HashMap();
                                    attributeMap.put(splitVal, tolerance);
                                    this.attributeTolerance.putAll(attributeMap);
                                });
            }
            return this;
        }

        /**
         * Sets the default value for the customEquivalences property.
         *
         * @param value the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withCustomEquivalences(Map<String, Equivalence<?>>... value) {

            if (this.customEquivalences == null) {
                this.customEquivalences = new HashMap<>();
            }

            for (int i = 0; i < value.length; i++) {
                this.customEquivalences.putAll(value[i]);
            }
            return this;
        }

        /**
         * Sets the default value for the Expression property.
         *
         * @param column     attributeName
         * @param expression Spring Expression
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withExpressionEquivalence(String column, String expression) {

            if (this.customEquivalences == null) {
                this.customEquivalences = new HashMap<>();
            }

            if (StringUtils.isNotBlank(column)) {
                Splitter.on(",")
                        .split(column)
                        .forEach(
                                splitVal ->
                                        this.customEquivalences.put(
                                                splitVal,
                                                new SpringExpressionEquivalence(expression)));
            }
            return this;
        }

        /**
         * Sets the default value for the errorOnDuplicate property.
         *
         * @param value the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withErrorOnDuplicate(boolean value) {
            this.errorOnDuplicate = value;
            return this;
        }

        /**
         * Sets the default value for the excludeOnDuplicate property. When this parameter is set
         * duplicate entry will NOT be compared
         * I
         *
         * @param value the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withExcludeOnDuplicate(boolean value) {
            this.excludeOnDuplicate = value;
            return this;
        }

        /**
         * Sets the default value for the printResultsOnSuccess property.
         *
         * @param value the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withPrintResultsOnSuccess(boolean value) {
            this.printResultsOnSuccess = value;
            return this;
        }

        /**
         * Sets the default value for the caselnsensitiveColumn property.
         *
         * @param value the default value
         *              ^return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withCaseInsensitiveColumn(boolean value) {
            this.caseInsensitiveColumn = value;
            return this;
        }

        /**
         * Sets the default value for the compareOutputFormat property.
         *
         * @param outputFormat the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withOutputFormat(DiffReport.OutputFormat outputFormat) {

            this.compareOutputFormat = outputFormat;
            return this;
        }

        /**
         * Sets the default value for the output path property. If this is set , the compare result
         * will be stored in a file
         *
         * @param outputPath the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withOutputPath(String outputPath) {
            this.compareOutputPath = outputPath;
            return this;
        }

        /**
         * Sets the default value for comparator web Api URL. If this is set , the compare result
         * will be uploaded to the URL
         *
         * @param comparatorWebApiURL the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withComparatorWebApiURL(String comparatorWebApiURL) {
            this.comparatorWebApiURL = comparatorWebApiURL;
            return this;
        }

        /**
         * Sets the default value for comparator web UI URL. If this is set , the compare result
         * will be uploaded to the URL
         * i
         *
         * @param comparatorWebUiURL the default value
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withComparatorWebUiURL(String comparatorWebUiURL) {
            this.comparatorWebUiURL = comparatorWebUiURL;
            return this;
        }

        /**
         * Sets the flag to throw error when there is a diff , sometimes client just want the diff
         * result without assertion error
         *
         * @param throwAssertionErrorOnDiff the default value is true
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withThrowAssertionErrorOnDiff(boolean throwAssertionErrorOnDiff) {
            this.throwAssertionErrorOnDiff = throwAssertionErrorOnDiff;
            return this;
        }

        /**
         * Sets the JSONCompareMode
         *
         * @param jsonCompareMode the default value is LENIENT
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withJSONCompareMode(JSONCompareMode jsonCompareMode) {
            this.jsonCompareMode = jsonCompareMode;
            return this;
        }

        /**
         * Exclude Empty Attribute ( Null / 0 ) If the attribute is missing from one side
         *
         * @param ignoreMissingNullAttribute the default value is false
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withIgnoreMissingNullAttribute(boolean ignoreMissingNullAttribute) {
            this.ignoreMissingNullAttribute = ignoreMissingNullAttribute;
            return this;
        }

        /**
         * Custom Value Transformation if needed
         *
         * @param column         attribute name
         * @param valueTransform Transformation Rule
         * @return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withValueTransform(String column, Transform<?> valueTransform) {

            if (this.valueTransform == null) {
                this.valueTransform = new HashMap<>();
            }

            if (StringUtils.isNotBlank(column)) {
                Splitter.on(",")
                        .split(column)
                        .forEach(splitVal -> this.valueTransform.put(splitVal, valueTransform));
            }
            return this;
        }

        /**
         * Default Soft null Transformation
         *
         *	@param column attribute name
         *	@return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withSoftNullNumberTransform(String column) {

            if (this.valueTransform == null) {
                this.valueTransform = new HashMap<>();
            }
            if (StringUtils.isNotBlank(column)) {
                Splitter.on(",")
                        .split(column)
                        .forEach(
                                splitVal ->
                                        this.valueTransform.put(
                                                splitVal, new SoftNullToNumberTransformation()));
            }
            return this;
        }

        /**
         *	Default Number Transformation
         *
         *	@param column attribute name
         *	(@return this builder
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withNumberTransform(String column) {

            if (this.valueTransform == null) {
                this.valueTransform = new HashMap<>();
            }

            if (StringUtils.isNotBlank(column)) {
                Splitter.on(",")
                        .split(column)
                        .forEach(
                                splitVal ->
                                        this.valueTransform.put(
                                                splitVal, new NumberTransformation()));
            }
            return this;
        }

        /**
        *	Default Soft null Transformation
        *
        *	@param column attribute name
        *	@param expression Spring Expression Transformation Rule
        *	@return this builder
        */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withExpressionTransformation(String column, String expression) {
            if (this.valueTransform == null) {
                this.valueTransform = new HashMap<>();
            }
            if (StringUtils.isNotBlank(column)) {
                Splitter.on(",")
                        .split(column)
                        .forEach(
                                splitVal ->
                                        this.valueTransform.put(
                                        splitVal,
                                        new SpringExpressionTransformation(expression)));
            }
            return this;
        }

        /**
         *	Set Diff Limiter max not of equals rows ( to improve memory efficiency )
         *
         *	@param maxEqual number of max equal in the map , default is -1 ( unlimited )
         *	^return
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withDiffLimiterMaxEqual(int maxEqual) {
            this.maxEqual = maxEqual;
            return this;
        }

        /**
         *	Set Diff Limiter max not of diff rows ( to improve memory efficiency )
         *
         *	@param maxDiff number of max diff in the map , default is -1 ( unlimited )
         *	@return
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withDiffLimiterMaxDiff(int maxDiff) {
            this.maxDiff = maxDiff;
            return this;
        }

        /**
         *	Set Diff Limiter max not of only in 1 rows ( to improve memory efficiency )
         *
         *	@param maxOnlyIn1 number of max side 1 only in the map , default is -1 ( unlimited )
         *	@return
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withDiffLimiterMaxOnlyIn1(int maxOnlyIn1) {
            this.maxOnlyIn1 = maxOnlyIn1;
            return this;
        }

        /**
         *	Set Diff Limiter max not of only in 2 rows ( to improve memory efficiency )
         *
         *	@param maxOnlyIn2 number of max side 2 only in the map , default is -1 ( unlimited )
         *	@return
         */
        public com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder withDiffLimiterMaxOnlyIn2(int maxOnlyIn2) {
            this.maxOnlyIn2 = maxOnlyIn2;
            return this;
        }


        /**
         *	Creates a new {(Slink ComparisonConfig} based on this builder's settings.
         *
         * (Sreturn the created ComparisonConfig
         */
        public ComparisonConfig build() {
            return new ComparisonConfig(
                    compareKey,
                    numericAttributes,
                    infoColumns,
                    ignoreColumns,
                    caseInsensitiveCompareAttributes,
                    attributeTolerance,
                    customEquivalences,
                    valueTransform,
                    errorOnDuplicate,
                    excludeOnDuplicate,
                    printResultsOnSuccess,
                    caseInsensitiveColumn,
                    compareOutputFormat,
                    compareOutputPath,
                    comparatorWebUiURL,
                    comparatorWebApiURL,
                    throwAssertionErrorOnDiff,
                    jsonCompareMode,
                    ignoreMissingNullAttribute,
                    maxEqual,
                    maxDiff,
                    maxOnlyIn1,
                    maxOnlyIn2,
                    compareColumns);
        }

    }
}