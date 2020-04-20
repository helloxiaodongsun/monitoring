package com.pactera.monitoring.exception;

/**
 * 业务异常
 */
public class BussinessException extends RuntimeException {

    public BussinessException() {
        super();
    }


    public BussinessException(String message) {
        super(message);
    }
}
