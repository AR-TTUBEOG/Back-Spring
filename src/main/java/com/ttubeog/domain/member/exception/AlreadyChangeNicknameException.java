package com.ttubeog.domain.member.exception;


public class AlreadyChangeNicknameException extends RuntimeException {

    public AlreadyChangeNicknameException(){
        super("이미 닉네임을 변경한 회원입니다.");
    }
}
