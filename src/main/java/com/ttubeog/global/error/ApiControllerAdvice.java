package com.ttubeog.global.error;

import com.ttubeog.global.payload.CommonDto;
import com.ttubeog.global.payload.ErrorCode;
import com.ttubeog.global.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ApiControllerAdvice {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<CommonDto> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        
        final ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .code(e.getMessage())
                .clazz(e.getMethod())
                .message(e.getMessage())
                .build();
        CommonDto commonDto = CommonDto.builder().check(false).information(response).build();
        return new ResponseEntity<>(commonDto, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .code(e.getMessage())
                .clazz(e.getBindingResult().getObjectName())
                .message(e.toString())
                .fieldErrors(e.getFieldErrors())
                .build();

        CommonDto commonDto = CommonDto.builder().check(false).information(response).build();
        return new ResponseEntity<>(commonDto, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<CommonDto> handleInvalidParameterException(InvalidParameterException e) {
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .code(e.getMessage())
                .clazz(e.getErrors().getObjectName())
                .message(e.toString())
                .fieldErrors(e.getFieldErrors())
                .build();

        CommonDto commonDto = CommonDto.builder().check(false).information(response).build();
        return new ResponseEntity<>(commonDto, HttpStatus.OK);
    }

    @ExceptionHandler(DefaultException.class)
    protected ResponseEntity<CommonDto> handleDefaultException(DefaultException e) {
        
        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = ErrorResponse
                .builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(e.toString())
                .build();
        
        CommonDto commonDto = CommonDto.builder().check(false).information(response).build();
        return new ResponseEntity<>(commonDto, HttpStatus.resolve(errorCode.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonDto> handleException(Exception e) {
        
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.toString())
                .build();
        CommonDto commonDto = CommonDto.builder().check(false).information(response).build();
        return new ResponseEntity<>(commonDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<CommonDto> handleAuthenticationException(AuthenticationException e) {
        
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value())
                .message(e.getMessage())
                .build();
        CommonDto commonDto = CommonDto.builder().check(false).information(response).build();
        return new ResponseEntity<>(commonDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DefaultAuthenticationException.class)
    protected ResponseEntity<CommonDto> handleCustomAuthenticationException(DefaultAuthenticationException e) {
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value())
                .message(e.getMessage())
                .build();
        CommonDto commonDto = CommonDto.builder().check(false).information(response).build();
        return new ResponseEntity<>(commonDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
    @ExceptionHandler(DefaultNullPointerException.class)
    protected ResponseEntity<CommonDto> handleNullPointerException(DefaultNullPointerException e) {
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        CommonDto commonDto = CommonDto.builder().check(false).information(response).build();
        return new ResponseEntity<>(commonDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}