package com.ttubeog.domain.spot.exception;

public class NonExistentSpotException extends RuntimeException {

    public NonExistentSpotException() {
        super("존재하지 않는 산책스팟입니다.");
    }
}
