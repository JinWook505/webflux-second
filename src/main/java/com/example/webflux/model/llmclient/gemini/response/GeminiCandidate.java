package com.example.webflux.model.llmclient.gemini.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeminiCandidate implements Serializable {
    @Serial
    private static final long serialVersionUID = 3103427709131152167L;

    private GeminiContent content;
}
