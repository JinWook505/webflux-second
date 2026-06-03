package com.example.webflux.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CommonError {

    private String errorCode;
    private String errorMessage;

    public CommonError(int errorCode, String errorMessage) {
        this.errorCode = String.valueOf(errorCode);
        this.errorMessage = errorMessage;
    }
}
