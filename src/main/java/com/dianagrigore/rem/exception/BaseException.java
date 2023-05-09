package com.dianagrigore.rem.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

    private HttpStatus status;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public BaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    protected BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"AbisException\", " +
                "\"status\":" + (status == null ? "null" : status) +
                "}";
    }
}
