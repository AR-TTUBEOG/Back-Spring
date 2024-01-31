package com.ttubeog.domain.auth.exception;

public class InvalidBearerException extends RuntimeException {
    public InvalidBearerException() {
        super("로그인이 필요한 서비스입니다.");
    }
}
