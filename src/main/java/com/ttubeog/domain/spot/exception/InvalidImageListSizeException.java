package com.ttubeog.domain.spot.exception;

public class InvalidImageListSizeException extends RuntimeException {

    public InvalidImageListSizeException() {
        super("유효하지 않은 이미지 개수입니다.");
    }
}
