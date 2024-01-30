package com.ttubeog.domain.auth.exception;


public class NotFoundMemberException extends RuntimeException {

    public NotFoundMemberException() {
        super("존재하지 않는 회원입니다.");
    }
}