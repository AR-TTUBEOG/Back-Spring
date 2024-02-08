package com.ttubeog.domain.member.exception;


public class InvalidMemberException extends RuntimeException {

    public InvalidMemberException(){
        super("멤버가 올바르지 않습니다.");
    }

    public InvalidMemberException(String message) {
        super(message);
    }
}
