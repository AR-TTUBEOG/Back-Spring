package com.ttubeog.global.error.exception;

import com.ttubeog.global.error.ErrorCode;
import com.ttubeog.global.error.GeneralException;

public class TempException extends GeneralException {
    public TempException(ErrorCode errorCode) {
        super(errorCode);
    }
}
