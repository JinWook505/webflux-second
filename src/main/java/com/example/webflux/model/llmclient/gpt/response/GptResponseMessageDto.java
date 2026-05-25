package com.example.webflux.model.llmclient.gpt.response;

import com.example.webflux.model.llmclient.gpt.GptMessageRole;
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
public class GptResponseMessageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 6667907440387219186L;

    private String content;
}
