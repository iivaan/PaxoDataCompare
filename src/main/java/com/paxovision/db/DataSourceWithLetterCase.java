package com.paxovision.db;

import com.paxovision.db.lettercase.*;
import com.paxovision.db.util.*;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * A data source to connect to the database with letter case.
 *
 */
public class DataSourceWithLetterCase implements DataSource, WithLetterCase {

  /**
   * The data source.
   */
  private final DataSource dataSource;
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
   * Constructor.
   * @param dataSource The data source.
   * @param tableLetterCase Letter case of the tables.
   * @param columnLetterCase Letter case of the columns.
   * @param primaryKeyLetterCase Letter case of the primary keys.
   */
  public DataSourceWithLetterCase(DataSource dataSource, LetterCase tableLetterCase,
                                  LetterCase columnLetterCase, LetterCase primaryKeyLetterCase) {

    this.dataSource = dataSource;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return dataSource.getConnection(username, password);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return dataSource.getLogWriter();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    dataSource.setLogWriter(out);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    dataSource.setLoginTimeout(seconds);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLoginTimeout() throws SQLException {
    return dataSource.getLoginTimeout();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return dataSource.getParentLogger();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return dataSource.unwrap(iface);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return dataSource.isWrapperFor(iface);
  }
}
