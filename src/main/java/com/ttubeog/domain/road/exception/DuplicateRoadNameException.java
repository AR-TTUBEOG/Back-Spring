package com.ttubeog.domain.road.exception;

public class DuplicateRoadNameException extends RuntimeException {

    public DuplicateRoadNameException() {
        super("중복된 산책로 이름입니다.");
    }

    public DuplicateRoadNameException(String message) {
        super(message);
    }
}
