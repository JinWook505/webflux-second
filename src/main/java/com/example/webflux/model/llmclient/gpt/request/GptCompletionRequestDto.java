package com.example.webflux.model.llmclient.gpt.request;

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
public class GptCompletionRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 3146540550334068026L;

    private GptMessageRole role; //
    private String content; //채팅 내용
}
