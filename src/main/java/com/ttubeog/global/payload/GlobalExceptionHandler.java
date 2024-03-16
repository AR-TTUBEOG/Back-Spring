package com.ttubeog.global.payload;

import com.ttubeog.global.payload.code.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception) {
        log.error("handleException", exception);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorStatus.)
    }
}
