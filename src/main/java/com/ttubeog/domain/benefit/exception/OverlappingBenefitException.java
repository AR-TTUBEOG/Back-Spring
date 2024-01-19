package com.ttubeog.domain.benefit.exception;

public class OverlappingBenefitException extends RuntimeException {

    public OverlappingBenefitException() {
        super("이미 저장된 쿠폰입니다. 만료기간이 지난 후 다시 받을 수 있습니다."); }
}
