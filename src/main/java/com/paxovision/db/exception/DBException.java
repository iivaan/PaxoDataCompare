package com.paxovision.db.exception;

/**
 * Exception during the assertion (for example : when getting the data in the database, or accessing to file system).
 */
public class DBException extends RuntimeException {

    /**
     * Serial version UID of the class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the Exception.
     *
     * @param exception Exception inside this one.
     */
    public DBException(Exception exception) {
        super(exception);
    }

    /**
     * Constructor of the Exception.
     *
     * @param message Message of the exception
     * @param objects Parameters of the message
     */
    public DBException(String message, Object... objects) {
        super(String.format(message, objects));
    }
}