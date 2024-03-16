package com.ttubeog.global.payload;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorResponseDto {

    private HttpStatus status;
    private String code;
    private String message;

    public ErrorResponseDto(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
