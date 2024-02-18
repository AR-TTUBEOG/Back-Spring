package com.ttubeog.domain.image.exception;

public class InvalidImageTypeException extends RuntimeException {

    public InvalidImageTypeException() {
        super("올바르지 않은 이미지 타입입니다.");
    }
}