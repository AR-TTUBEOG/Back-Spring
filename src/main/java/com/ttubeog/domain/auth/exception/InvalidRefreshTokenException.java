package com.ttubeog.domain.auth.exception;


public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("올바르지 않은 Refresh Token 입니다. 다시 로그인해주세요.");
    }
}
