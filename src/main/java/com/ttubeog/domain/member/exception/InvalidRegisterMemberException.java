package com.ttubeog.domain.member.exception;

public class InvalidRegisterMemberException extends RuntimeException {
    public InvalidRegisterMemberException() {
        super("해당 객체를 등록한 유저가 아닙니다.");
    }

    public InvalidRegisterMemberException(String message) {
        super(message);
    }
}
