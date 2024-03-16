package com.ttubeog.global.error;

import lombok.Getter;

@Getter
public class DefaultNullPointerException extends NullPointerException{

    private ErrorCode errorCode;

    public DefaultNullPointerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
