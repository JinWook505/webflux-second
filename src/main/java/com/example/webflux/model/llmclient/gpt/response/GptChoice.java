package com.example.webflux.model.llmclient.gpt.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GptChoice implements Serializable {
    @Serial
    private static final long serialVersionUID = -2708972457378829110L;

    private String finishReason;
    private GptResponseMessageDto message;
}
