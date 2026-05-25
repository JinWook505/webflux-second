package com.example.webflux.model.llmclient;

import com.example.webflux.model.user.chat.UserChatRequestDto;
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
public class LlmChatRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 5009353172145761069L;

    private String userRequest;
    /*
    systemPrompt가 userRequest에 포함되는 내용보다 더 높은 강제성과 우선순위를 가집니다.
     */
    private String systemPrompt;
    private boolean useJson;
    private LlmModel llmModel;

    public LlmChatRequestDto(UserChatRequestDto userChatRequestDto, String systemPrompt) {
        this.llmModel = userChatRequestDto.getLlmModel();
        this.systemPrompt = systemPrompt;
        this.userRequest = userChatRequestDto.getRequest();
    }
}
