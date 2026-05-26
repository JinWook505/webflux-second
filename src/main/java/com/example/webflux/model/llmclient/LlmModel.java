package com.example.webflux.model.llmclient;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LlmModel {
    GPT_4O("gpt-4o", LlmType.GPT),
    GPT_5_NANO("gpt-5-nano", LlmType.GPT),
    GEMINI_3_1_FLASH("gemini-3.1-flash-lite", LlmType.GEMINI)
    ;

    private final String code;
    private final LlmType llmType;

    @JsonValue
    @Override
    public String toString() {
        return code;
    }
}
