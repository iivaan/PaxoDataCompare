package com.paxovision.db;

import com.paxovision.db.lettercase.LetterCase;
import com.paxovision.db.lettercase.WithLetterCase;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class represents element from the database (either a {@link AbstractDbData} or a {@link Change}).
 * So this class contains : the way to access the database with {@link #getSource()} and {@link #getDataSource()} (one
 * of them need to be set before loading the data).<br>
 *
 * @param <D> Class of the subclass (an implementation of {@link AbstractDbElement}) : useful for the fluent methods
 *          (setters).
 */
public abstract class AbstractDbElement<D extends AbstractDbElement<D>> implements DbElement, WithLetterCase {

  /**
   * Class of the element.
   */
  protected final D myself;
  /**
   * Source of the data.
   */
  private Source source;
  /**
   * Data source.
   */
  private DataSource dataSource;
  /**
   * Letter case of the tables.
   * @since 1.1.0
   */
  private LetterCase tableLetterCase;
  /**
   * Letter case of the columns.
   * @since 1.1.0
   */
  private LetterCase columnLetterCase;
  /**
   * Letter case of the primary keys.
   * @since 1.1.0
   */
  private LetterCase primaryKeyLetterCase;

  /**
   * Default constructor.
   * @param selfType Class of this element : a sub-class of {@code AbstractDbElement}.
   */
  AbstractDbElement(Class<D> selfType) {
    myself = selfType.cast(this);
    setLetterCases();
  }

  /**
   * Constructor.
   * @param selfType Class of this element : a sub-class of {@code AbstractDbElement}.
   * @param source The {@link Source} to connect to the database (must be not {@code null}).
   * @throws NullPointerException If {@code source} is {@code null}.
   */
  AbstractDbElement(Class<D> selfType, Source source) {
    this(selfType);
    this.source = source;
    setLetterCases();
  }

  /**
   * Constructor.
   * @param selfType Class of this element : a sub-class of {@code AbstractDbElement}.
   * @param dataSource The {@link DataSource} (must be not {@code null}).
   * @throws NullPointerException If {@code dataSource} is {@code null}.
   */
  AbstractDbElement(Class<D> selfType, DataSource dataSource) {
    this(selfType);
    this.dataSource = dataSource;
    setLetterCases();
  }

  /**
   * Sets the letter cases from information in parameters.
   * @param tableLetterCase Letter case of the tables.
   * @param columnLetterCase Letter case of the columns.
   * @param primaryKeyLetterCase Letter case of the primary keys.
   * @return The actual instance.
   */
  D setLetterCases(LetterCase tableLetterCase, LetterCase columnLetterCase, LetterCase primaryKeyLetterCase) {
    this.tableLetterCase = tableLetterCase;
    this.columnLetterCase = columnLetterCase;
    this.primaryKeyLetterCase = primaryKeyLetterCase;
    return myself;
  }

  /**
   * Sets the letter cases from information in {@code dataSource} and {@code source}.
   */
  private void setLetterCases() {
    if (dataSource instanceof WithLetterCase) {
      WithLetterCase withLetterCase = (WithLetterCase) dataSource;
      tableLetterCase = withLetterCase.getTableLetterCase();
      columnLetterCase = withLetterCase.getColumnLetterCase();
      primaryKeyLetterCase = withLetterCase.getPrimaryKeyLetterCase();
    }
    else if (source instanceof WithLetterCase) {
      WithLetterCase withLetterCase = (WithLetterCase) source;
      tableLetterCase = withLetterCase.getTableLetterCase();
      columnLetterCase = withLetterCase.getColumnLetterCase();
      primaryKeyLetterCase = withLetterCase.getPrimaryKeyLetterCase();
    }
    else {
      tableLetterCase = LetterCase.TABLE_DEFAULT;
      columnLetterCase = LetterCase.COLUMN_DEFAULT;
      primaryKeyLetterCase = LetterCase.PRIMARY_KEY_DEFAULT;
    }
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
   * Return the source.
   * 
   * @see #setSource(Source)
   * @return The {@link Source} to connect.
   */
  public Source getSource() {
    return source;
  }

  /**
   * Sets the source.
   * 
   * @see #getSource()
   * @param source {@link Source} to connect to the database (must be not {@code null}).
   * @return The actual instance.
   * @throws NullPointerException If {@code source} is {@code null}.
   */
  public D setSource(Source source) {
    if (source == null) {
      throw new NullPointerException("source must be not null");
    }
    this.source = source;
    this.dataSource = null;
    setLetterCases();
    return myself;
  }

  /**
   * Return the data source.
   * 
   * @see #setDataSource(DataSource)
   * @return The data source.
   */
  public DataSource getDataSource() {
    return dataSource;
  }

  /**
   * Sets the data source.
   * 
   * @see #getDataSource()
   * @param dataSource The {@link DataSource} (must be not {@code null}).
   * @return The actual instance.
   * @throws NullPointerException If {@code dataSource} is {@code null}.
   */
  public D setDataSource(DataSource dataSource) {
    if (dataSource == null) {
      throw new NullPointerException("dataSource must be not null");
    }
    this.source = null;
    this.dataSource = dataSource;
    setLetterCases();
    return myself;
  }

  /**
   * Returns a {@link Connection} from a {@link DataSource} or from a {@link Source}.
   * 
   * @return A {@link Connection} differently, depending if it is a {@link DataSource} or a {@link Source}.
   * @throws SQLException SQL Exception
   */
  protected Connection getConnection() throws SQLException {
    if (dataSource == null && source == null) {
      throw new NullPointerException("connection or dataSource must be not null");
    }

    // Get a Connection differently, depending if it is a DataSource or a Source.
    if (dataSource != null) {
      return dataSource.getConnection();
    } else {
      return DriverManager.getConnection(source.getUrl(), source.getUser(), source.getPassword());
    }
  }

  /**
   * Returns the catalog from a connection.
   * @param connection The connection with the catalog
   * @return The catalog from a connection.
   * @throws SQLException SQL Exception
   */
  protected static String getCatalog(Connection connection) throws SQLException {
    try {
      return connection.getCatalog();
    }
    catch (SQLException exception) {
      throw exception;
    }
    catch (Exception throwable) {
      return null;
    }
  }

  /**
   * Returns the schema from a connection.
   * @param connection The connection with the catalog
   * @return The schema from a connection.
   * @throws SQLException SQL Exception
   */
  protected static String getSchema(Connection connection) throws SQLException {
    try {
      return connection.getSchema();
    }
    catch (SQLException exception) {
      throw exception;
    }
    catch (Exception throwable) {
      return null;
    }
  }
}
