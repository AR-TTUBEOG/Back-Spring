package com.ttubeog.domain.auth.exception;

public class PlatformException  extends RuntimeException {

    public PlatformException(){
        super("플랫폼 에러입니다.");
    }
}
