package com.ttubeog.domain.spot.exception;

public class InvalidSpotIdException extends RuntimeException{

    public InvalidSpotIdException() {
        super("존재하지 않은 산책 스팟 Id 값입니다.");
    }
}
