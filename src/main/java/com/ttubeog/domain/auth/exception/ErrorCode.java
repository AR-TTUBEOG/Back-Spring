package com.ttubeog.domain.auth.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNAUTHORIZED("인증되지 않은 요청입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("유효하지 않은 액세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_USER("존재하지 않는 유저입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_APPLE_OAUTH_TOKEN("Apple OAuth Identity Token 값이 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    PUBLIC_KEY_GENERATION_ERROR("Apple OAuth 로그인 중 public key 생성에 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    APPLE_OAUTH_TOKEN_EXPIRED("Apple OAuth 로그인 중 Identity Token 유효기간이 만료됐습니다.", HttpStatus.UNAUTHORIZED);


    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
