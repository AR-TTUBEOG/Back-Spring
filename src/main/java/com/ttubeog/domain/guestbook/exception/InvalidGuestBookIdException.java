package com.ttubeog.domain.guestbook.exception;

public class InvalidGuestBookIdException extends RuntimeException {

    public InvalidGuestBookIdException() {
        super("올바르지 않은 방명록 ID 입니다.");
    }
}
