package com.ttubeog.domain.store.exception;

public class InvalidStoreIdException extends RuntimeException {

    public InvalidStoreIdException() {
        super("존재하지 않는 매장 Id값입니다. 또는 store에 권한이 없는 유저입니다.");
    }
}