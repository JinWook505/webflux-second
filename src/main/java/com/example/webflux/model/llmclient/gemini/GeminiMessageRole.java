package com.example.webflux.model.llmclient.gemini;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GeminiMessageRole {
    USER,
    MODEL
    ;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
