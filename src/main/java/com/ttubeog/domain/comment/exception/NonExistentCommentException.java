package com.ttubeog.domain.comment.exception;

public class NonExistentCommentException extends RuntimeException {

    public NonExistentCommentException() {
        super("존재하지 않는 댓글입니다.");
    }
}
