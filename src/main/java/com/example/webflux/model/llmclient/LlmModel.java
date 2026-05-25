package com.example.webflux.model.llmclient;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LlmModel {
    GPT_4O("gpt-4o", LlmType.GPT),
    GEMINI_2_0_FLASH("gemini-2.0-flash", LlmType.GEMINI)
    ;

    private final String code;
    private final LlmType llmType;

    @JsonValue
    @Override
    public String toString() {
        return code;
    }
}
