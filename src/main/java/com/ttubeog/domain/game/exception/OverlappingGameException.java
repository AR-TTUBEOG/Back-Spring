package com.ttubeog.domain.game.exception;

public class OverlappingGameException extends RuntimeException {

    public OverlappingGameException() {
        super("하나의 혜택에 같은 종류의 게임은 저장할 수 없습니다.");}
}
