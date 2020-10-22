package com.paxovision.db;

/**
 * Enumeration of the different types of change that are possible in the database (creation, modification or deletion of a row).
 */
public enum ChangeType {

  /**
   * The change is a creation of a row.
   * <p>At the start point the row do not exist and at the end point it is created.</p>
   */
  CREATION,
  /**
   * The change is a modification of a row.
   * <p>The row is modified between the start point and at the end point.</p>
   */
  MODIFICATION,
  /**
   * The change is a deletion of a row.
   * <p>At the start point the row exists but anymore at the end point.</p>
   */
  DELETION,
}
