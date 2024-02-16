package com.ttubeog.domain.road.exception;

public class InvalidRoadIdException extends RuntimeException {

    public InvalidRoadIdException() {
        super("올바르지 않은 산책로 ID 입니다.");
    }

    public InvalidRoadIdException(String message) {
        super(message);
    }
}
