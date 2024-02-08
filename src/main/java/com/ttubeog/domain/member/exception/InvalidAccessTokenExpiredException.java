package com.ttubeog.domain.member.exception;


public class InvalidAccessTokenExpiredException extends RuntimeException {
    public InvalidAccessTokenExpiredException() { super("새로운 Access Token 생성에 실패하였습니다.");}
}
