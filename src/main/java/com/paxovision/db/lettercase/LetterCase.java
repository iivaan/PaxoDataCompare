package com.paxovision.db.lettercase;

import java.util.HashMap;
import java.util.Map;

/**
 * The letter case which indicates a case conversion and a case comparison.
 */
public class LetterCase implements CaseConversion, CaseComparison {

  /**
   * The default letter case for table.
   */
  public static final LetterCase TABLE_DEFAULT = getLetterCase(CaseConversions.NO, CaseComparisons.IGNORE);
  /**
   * The default letter case for table.
   */
  public static final LetterCase COLUMN_DEFAULT = getLetterCase(CaseConversions.UPPER, CaseComparisons.IGNORE);
  /**
   * The default letter case for table.
   */
  public static final LetterCase PRIMARY_KEY_DEFAULT = getLetterCase(CaseConversions.UPPER, CaseComparisons.IGNORE);

  /**
   * The conversion of the case of a {@link String}.
   */
  private final CaseConversion conversion;

  /**
   * The comparison on {@link String} which consider the case.
   */
  private final CaseComparison comparison;

  /**
   * The cache containing the different possible letter case after the first instantiation.
   */
  private static Map<CaseConversion, Map<CaseComparison, LetterCase>> cache;

  /**
   * Returns a instance of a letter case.
   * @param conversion The conversion of the case of a {@link String}.
   * @param comparison The comparison on {@link String} which consider the case.
   * @return An instance of a letter case.
   */
  public static synchronized LetterCase getLetterCase(CaseConversion conversion, CaseComparison comparison) {
    if (conversion == null) {
      throw new NullPointerException("The case conversion must be not null");
    }
    if (comparison == null) {
      throw new NullPointerException("The case comparison must be not null");
    }

    if (cache == null) {
      cache = new HashMap<>();
    }
    Map<CaseComparison, LetterCase> map = cache.get(conversion);
    if (map == null) {
      map = new HashMap<>();
      cache.put(conversion, map);
      LetterCase letterCase = new LetterCase(conversion, comparison);
      map.put(comparison, letterCase);
      return letterCase;
    }
    LetterCase letterCase = map.get(comparison);
    if (letterCase == null) {
      letterCase = new LetterCase(conversion, comparison);
      map.put(comparison, letterCase);
    }
    return letterCase;
  }

  /**
   * Constructor.
   * @param conversion The conversion of the case of a {@link String}.
   * @param comparison The comparison on {@link String} which consider the case.
   */
  private LetterCase(CaseConversion conversion, CaseComparison comparison) {
    this.conversion = conversion;
    this.comparison = comparison;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getComparisonName() {
    return comparison.getComparisonName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEqual(String value1, String value2) {
    return comparison.isEqual(value1, value2);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare(String value1, String value2) {
    return comparison.compare(value1, value2);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getConversionName() {
    return conversion.getConversionName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String convert(String value) {
    return conversion.convert(value);
  }
}
