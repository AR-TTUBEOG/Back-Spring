package com.ttubeog.domain.auth.exception;

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException() {
        super("회원 상태 정보가 올바르지 않습니다.");
    }
}
