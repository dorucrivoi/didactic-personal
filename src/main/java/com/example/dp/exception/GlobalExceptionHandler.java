package com.example.dp.exception;


import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.ConnectException;
import java.time.LocalDateTime;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        return buildErrorMessage(HttpStatus.NOT_FOUND, "Entity not found", ex, request);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorMessage> handleDatabaseError(DataAccessException ex, WebRequest request) {
        return buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Database error", ex, request);
    }

    @ExceptionHandler(AmqpException.class)
    public ResponseEntity<ErrorMessage> handleAmqpError(AmqpException ex, WebRequest request) {
        return buildErrorMessage(HttpStatus.SERVICE_UNAVAILABLE, "Message broker error", ex, request);
    }

    @ExceptionHandler(AmqpConnectException.class)
    public ResponseEntity<ErrorMessage> handleAmqpConnectError(AmqpConnectException ex, WebRequest request) {
        log.error("RabbitMQ connection error", ex);
        return buildErrorMessage(HttpStatus.SERVICE_UNAVAILABLE, "RabbitMQ connection error", ex, request);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorMessage> handleConnectException(ConnectException ex, WebRequest request) {
        return buildErrorMessage(HttpStatus.SERVICE_UNAVAILABLE, "Connection refused", ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericException(Exception ex, WebRequest request) {
        return buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", ex, request);
    }

    private ResponseEntity<ErrorMessage> buildErrorMessage(HttpStatus status, String error, Exception ex, WebRequest request) {
        ErrorMessage apiError = new ErrorMessage(
                LocalDateTime.now(),
                status.value(),
                error,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(status).body(apiError);
    }
}
