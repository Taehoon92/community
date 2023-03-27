package hoon.community.global.exception;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;
import java.util.Locale;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException e, Locale locale) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus().value())
                .body(new ErrorResponse(e.getErrorCode(), messageSource, locale));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e, Locale locale) {
        log.error("handleHttpRequestMethodNotSupportedException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.METHOD_NOT_ALLOWED.getStatus().value())
                .body(new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED, messageSource, locale));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(final Exception e, Locale locale) {
        log.error("handleException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value())
                .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, messageSource, locale));
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException e, Locale locale) {
        log.error("handleRuntimeException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getStatus().value())
                .body(new ErrorResponse(ErrorCode.BAD_REQUEST, messageSource, locale));
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(final AccessDeniedException e, Locale locale) {
        log.error("handleAccessDeniedException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.ACCESS_DENIED.getStatus().value())
                .body(new ErrorResponse(ErrorCode.ACCESS_DENIED, messageSource, locale));
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e, Locale locale) {
        log.error("handleAccessDeniedException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.NOT_FOUND.getStatus().value())
                .body(new ErrorResponse(ErrorCode.NOT_FOUND, messageSource, locale));

    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(final MissingRequestHeaderException e, Locale locale) {
        log.error("handleMissingRequestHeaderException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.MISSING_REQUEST_HEADER.getStatus().value())
                .body(new ErrorResponse(ErrorCode.MISSING_REQUEST_HEADER, messageSource, locale));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e, Locale locale) {
        log.error("handleBindException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BIND_ERROR.getStatus().value())
                .body(new ErrorResponse(ErrorCode.BIND_ERROR, messageSource, locale));
    }
}