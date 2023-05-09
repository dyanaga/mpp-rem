package com.dianagrigore.rem.interceptor;

import com.dianagrigore.rem.exception.BaseException;
import com.dianagrigore.rem.exception.LoginException;
import com.dianagrigore.rem.exception.PermissionDeniedException;
import com.dianagrigore.rem.exception.ResponseException;
import com.dianagrigore.rem.exception.ValidationException;
import com.dianagrigore.rem.interceptor.logging.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 5500)
public class ExceptionHandlers {

    final LoggingService loggingService;

    @Autowired
    HttpServletRequest httpServletRequest;

    public ExceptionHandlers(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @ExceptionHandler(value = {BaseException.class, LoginException.class, PermissionDeniedException.class})
    public ResponseEntity<ResponseException> handleBaseException(BaseException ex, WebRequest webRequest) {
        loggingService.logError(httpServletRequest, ex);
        String requestId = httpServletRequest.getHeader("request-id");
        return new ResponseEntity<>(new ResponseException()
                .message(ex.getMessage(), ex.getStatus())
                .setRequestId(requestId)
                .setException(ex.toString()), ex.getStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationException> handleValidationException(MethodArgumentNotValidException ex, WebRequest webRequest) {
        loggingService.logError(httpServletRequest, ex);
        String requestId = httpServletRequest.getHeader("request-id");
        ValidationException validationException = new ValidationException(ex);
        validationException.setRequestId(requestId);
        return new ResponseEntity<>(validationException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseException> handleBaseException(Exception ex, WebRequest webRequest) {
        loggingService.logError(httpServletRequest, ex);
        String requestId = httpServletRequest.getHeader("request-id");
        return new ResponseEntity<>(new ResponseException()
                .message(ex.getMessage())
                .setRequestId(requestId)
                .setException(ex.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
