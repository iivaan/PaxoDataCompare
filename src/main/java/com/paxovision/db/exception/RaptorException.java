package com.paxovision.db.exception;

public class RaptorException extends RuntimeException {

    public RaptorException(String errorMsg) {
        super(errorMsg);
    }
    public RaptorException(String errorMsg, Exception e) {
        super(errorMsg, e);
    }
}
