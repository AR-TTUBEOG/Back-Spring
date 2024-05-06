package com.ttubeog.domain.store.exception;

public class UnathorizedMemberException extends RuntimeException {

    public UnathorizedMemberException() {
        super("해당 매장에 대한 수정 및 삭제 권한이 없습니다.");
    }
}
