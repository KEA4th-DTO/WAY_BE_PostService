package com.dto.way.post.global.exception.handler;

import com.dto.way.post.global.exception.GeneralException;
import com.dto.way.post.global.response.code.BaseErrorCode;

public class KafkaExceptionHandler extends GeneralException {
    public KafkaExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}