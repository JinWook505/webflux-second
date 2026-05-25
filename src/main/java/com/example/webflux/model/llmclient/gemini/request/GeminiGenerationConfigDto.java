package com.example.webflux.model.llmclient.gemini.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class GeminiGenerationConfigDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 7135085483251262838L;

    private String responseMimeType;

    public GeminiGenerationConfigDto () {
        this.responseMimeType = "application/json";
    }
}
