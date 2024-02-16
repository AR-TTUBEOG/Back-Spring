package com.ttubeog.domain.game.exception;

public class AlreadyExistGameException extends RuntimeException{
    public AlreadyExistGameException() {
        super("해당 매장에 이미 같은 종류의 게임이 존재합니다.");
    }
}
