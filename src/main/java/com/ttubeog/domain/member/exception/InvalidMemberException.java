package com.ttubeog.domain.member.exception;

import com.ttubeog.domain.spot.exception.InvalidDongAreaException;

public class InvalidMemberException extends RuntimeException {

    public InvalidMemberException(){
        super("멤버가 올바르지 않습니다.");
    }

    public InvalidMemberException(String message) {
        super(message);
    }
}
