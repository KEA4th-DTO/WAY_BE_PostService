package com.dto.way.post.global.exception;

import com.dto.way.post.global.exception.GeneralException;
import com.dto.way.post.global.response.code.BaseErrorCode;
public class ExceptionHandler extends GeneralException {
    public ExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}