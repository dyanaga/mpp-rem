package com.dianagrigore.rem.exception;

import org.springframework.http.HttpStatus;

import java.util.Optional;

public class ResponseException {
    private int status;
    private String message;
    private String exception;
    private String requestId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseException message(String message) {
        this.setMessage(message);
        return this;
    }

    public ResponseException message(String message, HttpStatus httpStatus) {
        this.setMessage(message);
        this.setStatus(httpStatus);
        return this;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = Optional.ofNullable(status).map(HttpStatus::value).orElse(500);
    }

    public ResponseException setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getException() {
        return exception;
    }

    public ResponseException setException(String exception) {
        this.exception = exception;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public ResponseException setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"ResponseException\", " +
                "\"status\":\"" + status + "\"" + ", " +
                "\"message\":" + (message == null ? "null" : "\"" + message + "\"") + ", " +
                "\"exception\":" + (exception == null ? "null" : "\"" + exception + "\"") + ", " +
                "\"requestId\":" + (requestId == null ? "null" : "\"" + requestId + "\"") +
                "}";
    }

}
