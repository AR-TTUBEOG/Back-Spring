package com.ttubeog.domain.benefit.exception;

public class InvalidMemberBenefitException extends RuntimeException {

    public InvalidMemberBenefitException(){
        super("유저에 저장된 혜택이 올바르지 않습니다.");
    }

}
