package com.ttubeog.domain.comment.exception;

public class UnauthorizedMemberException extends RuntimeException {

    public UnauthorizedMemberException() {
        super("해당 댓글의 작성자가 아닙니다.");
    }
}
