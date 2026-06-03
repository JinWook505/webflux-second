package com.example.webflux.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CustomErrorType {

    GEMINI_RESPONSE_ERROR(1),
    GPT_RESPONSE_ERROR(2),

    ;

    private int code;
}
