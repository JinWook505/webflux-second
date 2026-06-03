package com.example.webflux.model.user.chat;

import com.example.webflux.exception.CommonError;
import com.example.webflux.model.llmclient.LlmChatResponseDto;
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
public class UserChatResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -2479640483541743119L;

    private String response;
    private CommonError error;

    public UserChatResponseDto(LlmChatResponseDto llmChatResponseDto) {
        this.response = llmChatResponseDto.getLlmResponse();
        this.error = llmChatResponseDto.getError();
    }
}
