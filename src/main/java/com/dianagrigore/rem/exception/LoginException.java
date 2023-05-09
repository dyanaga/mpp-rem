package com.dianagrigore.rem.exception;

import org.springframework.http.HttpStatus;

public class LoginException extends BaseException {

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
        super.setStatus(HttpStatus.BAD_REQUEST);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginException(Throwable cause) {
        super(cause);
    }

    protected LoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
