package com.paxovision.db;

import com.paxovision.db.lettercase.*;
import com.paxovision.db.util.*;

/**
 * A source to indicates the information to connect to the database with letter case.
 */
public class SourceWithLetterCase extends Source implements WithLetterCase {

  /**
   * Letter case of the tables.
   */
  private final LetterCase tableLetterCase;
  /**
   * Letter case of the columns.
   */
  private final LetterCase columnLetterCase;
  /**
   * Letter case of the primary keys.
   */
  private final LetterCase primaryKeyLetterCase;

  /**
   * Constructor with the information.
   *
   * @param url URL to the database.
   * @param user User to connect.
   * @param password Password to connect.
   * @param tableLetterCase Letter case of the tables.
   * @param columnLetterCase Letter case of the columns.
   * @param primaryKeyLetterCase Letter case of the primary keys.
   */
  public SourceWithLetterCase(String url, String user, String password,
                LetterCase tableLetterCase, LetterCase columnLetterCase, LetterCase primaryKeyLetterCase) {

    super(url, user, password);
    this.tableLetterCase = tableLetterCase;
    this.columnLetterCase = columnLetterCase;
    this.primaryKeyLetterCase = primaryKeyLetterCase;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LetterCase getColumnLetterCase() {
    return columnLetterCase;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LetterCase getPrimaryKeyLetterCase() {
    return primaryKeyLetterCase;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LetterCase getTableLetterCase() {
    return tableLetterCase;
  }
}
