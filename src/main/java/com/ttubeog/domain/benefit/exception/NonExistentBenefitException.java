package com.ttubeog.domain.benefit.exception;

public class NonExistentBenefitException extends RuntimeException{

    public NonExistentBenefitException() {
        super("존재하지 않는 혜택입니다.");}
}
