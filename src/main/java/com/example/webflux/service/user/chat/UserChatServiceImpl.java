package com.example.webflux.service.user.chat;

import com.example.webflux.model.llmclient.LlmChatRequestDto;
import com.example.webflux.model.llmclient.LlmChatResponseDto;
import com.example.webflux.model.llmclient.LlmType;
import com.example.webflux.model.user.chat.UserChatRequestDto;
import com.example.webflux.model.user.chat.UserChatResponseDto;
import com.example.webflux.service.llmclient.LlmWebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserChatServiceImpl implements UserChatService {

    private final Map<LlmType, LlmWebClientService> llmWebClientServiceMap;

    @Override
    public Mono<UserChatResponseDto> getOneShotChat(UserChatRequestDto userChatRequestDto) {
        LlmChatRequestDto llmChatRequestDto =
                new LlmChatRequestDto(userChatRequestDto, "요청에 적절히 응답해주세요.");

        Mono<LlmChatResponseDto> chatCompletionMono =
                llmWebClientServiceMap.get(userChatRequestDto.getLlmModel().getLlmType())
                        .getChatCompletion(llmChatRequestDto);

        return chatCompletionMono.map(UserChatResponseDto::new);
    }
}
