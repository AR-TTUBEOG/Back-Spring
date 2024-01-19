package com.ttubeog.domain.benefit.exception;

public class AlreadyUsedBenefitException extends RuntimeException {

    public AlreadyUsedBenefitException(){
        super("이미 사용된 혜택입니다.");
    }
}
