package com.ttubeog.domain.member.exception;

public class InvalidMemberException extends RuntimeException {

    public InvalidMemberException(){
        super("유저가 올바르지 않습니다.");
    }

}
