package com.ttubeog.domain.game.exception;

public class NonExistentGameException extends RuntimeException{

    public NonExistentGameException() {
        super("존재하지 않는 게임입니다.");}
}
