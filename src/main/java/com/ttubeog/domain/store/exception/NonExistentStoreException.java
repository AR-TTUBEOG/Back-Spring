package com.ttubeog.domain.store.exception;

public class NonExistentStoreException extends RuntimeException {

    public NonExistentStoreException() {
        super("존재하지 않는 매장입니다.");
    }
}
