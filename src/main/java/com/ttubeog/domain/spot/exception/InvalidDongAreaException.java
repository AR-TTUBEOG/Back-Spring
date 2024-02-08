package com.ttubeog.domain.spot.exception;

public class InvalidDongAreaException extends RuntimeException{

    public InvalidDongAreaException() {
        super("유효하지 않은 지역 정보입니다.");
    }

    public InvalidDongAreaException(String message) {
        super(message);
    }
}
