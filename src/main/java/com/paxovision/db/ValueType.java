package com.paxovision.db;

/**
 * Enumeration of the different type of value that are in the database.
 *
 */
public enum ValueType {

  /**
   * Bytes type.
   */
  BYTES,
  /**
   * Boolean type.
   */
  BOOLEAN,
  /**
   * Text type (CHAR or VARCHAR column).
   */
  TEXT,
  /**
   * Date type.
   */
  DATE,
  /**
   * Time type.
   */
  TIME,
  /**
   * Date/time type (TIMESTAMP column).
   */
  DATE_TIME,
  /**
   * Number type (INT, SMALLINT, TINYINT, BIGINT, REAL or DECIMAL column).
   */
  NUMBER,
  /**
   * UUID type.
   */
  UUID,
  /**
   * Not identified type : null value for example.
   */
  NOT_IDENTIFIED;

  /**
   * Returns the types which are possible for the actual value (data) for the comparison with an expected value.
   *
   * @param expected The expected value
   * @return The possible types of the actual value
   */
  public static ValueType[] getPossibleTypesForComparison(Object expected) {
    if (expected instanceof byte[]) {
      return new ValueType[] { BYTES };
    }
    if (expected instanceof Boolean) {
      return new ValueType[] { BOOLEAN };
    }
    if (expected instanceof String) {
      return new ValueType[] { ValueType.TEXT, ValueType.NUMBER, ValueType.DATE, ValueType.TIME, ValueType.DATE_TIME,
              ValueType.UUID };
    }
    if (expected instanceof DateValue) {
      return new ValueType[] { ValueType.DATE, ValueType.DATE_TIME };
    }
    if (expected instanceof TimeValue) {
      return new ValueType[] { ValueType.TIME };
    }
    if (expected instanceof DateTimeValue) {
      return new ValueType[] { DATE_TIME };
    }
    if (expected instanceof Number) {
      return new ValueType[] { NUMBER };
    }
    if (expected instanceof java.util.UUID) {
      return new ValueType[] { UUID };
    }
    return new ValueType[] { NOT_IDENTIFIED };
  }
}
