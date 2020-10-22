package com.paxovision.db;

import com.paxovision.db.lettercase.*;
import com.paxovision.db.util.*;

import java.util.List;

/**
 * Column in a {@link AbstractDbData}.
 * <p>
 * A column can have many rows with a value for each row.
 * </p>
 * <p>
 * Note : you never instantiate directly this class. You will get an object of this class from a {@link Table} or a
 * {@link Request} by using {@link AbstractDbData#getColumn(int)}.
 * </p>
 */
public class Column implements DbElement, WithColumnLetterCase {

  /**
   * The name of the column.
   */
  private final String name;
  /**
   * The values of the column.
   */
  private final List<Value> valuesList;
  /**
   * Letter case of the columns.
   * @since 1.1.0
   */
  private final LetterCase columnLetterCase;

  /**
   * Constructor of the column with visibility in the package.
   * 
   * @param name The name of the column.
   * @param valuesList The values in the column.
   * @param columnLetterCase The letter case of the columns.
   */
  Column(String name, List<Value> valuesList, LetterCase columnLetterCase) {
    this.name = name;
    this.valuesList = valuesList;
    this.columnLetterCase = columnLetterCase;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LetterCase getColumnLetterCase() {
    return columnLetterCase;
  }

  /**
   * Returns the name of the column.
   * 
   * @return The name of the column.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the values of the column.
   * 
   * @return The values of the column.
   */
  public List<Value> getValuesList() {
    return valuesList;
  }

  /**
   * Returns the value corresponding to the row index.
   * 
   * @param index The index
   * @return The value
   */
  public Value getRowValue(int index) {
    return valuesList.get(index);
  }
}
