package com.dianagrigore.rem.exception;

import com.dianagrigore.rem.exception.dto.ErrorField;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ValidationException extends ResponseException {

    private final List<ErrorField> errors;

    public ValidationException(MethodArgumentNotValidException exception) {
        setStatus(400);
        setMessage("Validation error!");
        setException(exception.getMessage());
        this.errors = Optional.of(exception)
                .map(BindException::getBindingResult)
                .map(Errors::getAllErrors)
                .map(objectErrors -> objectErrors.stream()
                        .filter(objectError -> objectError instanceof FieldError)
                        .map(objectError -> (FieldError) objectError)
                        .map(objectError -> new ErrorField()
                                .setField(objectError.getField())
                                .setMessage(objectError.getDefaultMessage())
                                .setObject(objectError.getObjectName())
                                .setValue(objectError.getRejectedValue()))
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    public List<ErrorField> getErrors() {
        return errors;
    }
}
