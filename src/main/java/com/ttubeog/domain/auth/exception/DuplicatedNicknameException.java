package com.ttubeog.domain.auth.exception;

public class DuplicatedNicknameException extends RuntimeException {
    public DuplicatedNicknameException() {
        super("중복된 닉네임입니다.");
    }
}
