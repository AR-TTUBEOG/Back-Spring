package com.ttubeog.domain.road.exception;

public class NullRoadCoordinateException extends NullPointerException {

    public NullRoadCoordinateException() {
        super("빈 경로 배열입니다.");
    }

    public NullRoadCoordinateException(String message) {
        super(message);
    }
}
