package com.ttubeog.domain.auth.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        logger.error("[CustomException] errCode : " + ex.getErrorCode());
        logger.error("[CustomException] errMsg : " + ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(ex.getMessage()),
                ex.getErrorCode().getHttpStatus()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        logger.error("[RuntimeException] errMsg : " + ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException ex) {
        logger.error("[Exception] errMsg : " + ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}