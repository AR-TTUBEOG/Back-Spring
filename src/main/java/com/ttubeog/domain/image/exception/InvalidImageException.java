package com.ttubeog.domain.image.exception;

public class InvalidImageException extends RuntimeException {

    public InvalidImageException() {
        super("유효하지 않은 이미지입니다.");
    }
}
