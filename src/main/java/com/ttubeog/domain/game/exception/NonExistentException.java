package com.ttubeog.domain.game.exception;

public class NonExistentException extends RuntimeException{

    public NonExistentException() {
        super("존재하지 않는 게임입니다.");}
}
