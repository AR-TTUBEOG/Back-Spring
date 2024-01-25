package com.ttubeog.domain.auth.exception;


public class AccessTokenExpiredException extends RuntimeException {
    public AccessTokenExpiredException() { super("Access Token 유효기간이 만료되었습니다.");}
}
