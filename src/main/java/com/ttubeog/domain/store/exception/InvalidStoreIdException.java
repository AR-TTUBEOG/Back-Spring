package com.ttubeog.domain.store.exception;

public class InvalidStoreIdException extends RuntimeException {

    public InvalidStoreIdException() {
        super("존재하지 않는 매장 Id값입니다.");
    }
}