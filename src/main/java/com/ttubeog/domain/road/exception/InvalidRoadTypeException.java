package com.ttubeog.domain.road.exception;

public class InvalidRoadTypeException extends RuntimeException {

    public InvalidRoadTypeException() {
        super("유효하지 않은 산책로 타입입니다.");
    }

    public InvalidRoadTypeException(String message) {
        super(message);
    }
}
