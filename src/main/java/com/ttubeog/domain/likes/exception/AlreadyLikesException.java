package com.ttubeog.domain.likes.exception;

public class AlreadyLikesException extends RuntimeException {

    public AlreadyLikesException() {
        super("이미 좋아요를 눌렀습니다.");
    }
}
