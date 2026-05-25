package com.example.webflux.model.llmclient.gemini.request;

import com.example.webflux.model.llmclient.gemini.GeminiMessageRole;
import com.example.webflux.model.llmclient.gpt.GptMessageRole;
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
public class GeminiCompletionRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1146540550334068026L;

    private GeminiMessageRole role; //
    private String content; //채팅 내용

    public GeminiCompletionRequestDto(String content) {
        this.content = content;
    }
}
