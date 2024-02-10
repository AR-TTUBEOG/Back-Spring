package com.ttubeog.domain.spot.exception;

public class AlreadyExistsSpotException extends RuntimeException {

    public AlreadyExistsSpotException() {
        super("이미 존재하는 산책 장소명입니다.");
    }
}
