package com.ttubeog.domain.auth.exception;


public class BlankTokenException extends RuntimeException {
    public BlankTokenException() { super("토큰은 공백일 수 없습니다.");}
}
