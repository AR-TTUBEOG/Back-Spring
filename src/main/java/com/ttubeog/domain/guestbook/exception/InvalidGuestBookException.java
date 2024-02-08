package com.ttubeog.domain.guestbook.exception;

public class InvalidGuestBookException extends RuntimeException {

    public InvalidGuestBookException() {
        super("방명록 타입과 장소 ID를 확인해주세요.");
    }

}
