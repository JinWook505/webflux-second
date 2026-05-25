package com.example.webflux.service.llmclient;

import com.example.webflux.model.llmclient.LlmChatRequestDto;
import com.example.webflux.model.llmclient.LlmChatResponseDto;
import com.example.webflux.model.llmclient.LlmType;
import reactor.core.publisher.Mono;

/*
느슨한 결합
이 인터페이스를 사용하는 서비스는 내부 구현체에 대해 전혀 몰라도 된다.

유연성 및 확장성
다양한 LlmType을 계속해서 자유롭게 추가 가능

다형성
원하는 webclient구현체를 자유롭게 선택해서 사용할 수 있음.
 */
public interface LlmWebClientService {
    Mono<LlmChatResponseDto> getChatCompletion(LlmChatRequestDto requestDto);

    LlmType getLlmType();
    //gptWebClientService, GeminiWebClientService
}
