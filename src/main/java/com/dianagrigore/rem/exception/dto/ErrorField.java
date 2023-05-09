package com.dianagrigore.rem.exception.dto;

public class ErrorField {
    private String object;
    private String field;
    private Object value;
    private String message;

    public String getObject() {
        return object;
    }

    public ErrorField setObject(String object) {
        this.object = object;
        return this;
    }

    public String getField() {
        return field;
    }

    public ErrorField setField(String field) {
        this.field = field;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public ErrorField setValue(Object value) {
        this.value = value;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorField setMessage(String message) {
        this.message = message;
        return this;
    }
}
