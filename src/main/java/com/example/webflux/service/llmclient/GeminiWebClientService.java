package com.example.webflux.service.llmclient;

import com.example.webflux.exception.CommonError;
import com.example.webflux.exception.CustomErrorType;
import com.example.webflux.exception.ErrorTypeException;
import com.example.webflux.model.llmclient.LlmChatRequestDto;
import com.example.webflux.model.llmclient.LlmChatResponseDto;
import com.example.webflux.model.llmclient.LlmType;
import com.example.webflux.model.llmclient.gemini.request.GeminiChatRequestDto;
import com.example.webflux.model.llmclient.gemini.response.GeminiChatResponseDto;
import com.example.webflux.model.llmclient.gpt.response.GptChatResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Slf4j
@Service
public class GeminiWebClientService implements LlmWebClientService {

    private final WebClient webClient;

    @Value("${llm.gemini.key}")
    private String geminiApiKey;

    @Override
    public Mono<LlmChatResponseDto> getChatCompletion(LlmChatRequestDto requestDto) {
        GeminiChatRequestDto geminiChatRequestDto = new GeminiChatRequestDto(requestDto);
        return webClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-lite:generateContent?key=" + geminiApiKey)
                .bodyValue(geminiChatRequestDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(body -> {
                            log.error("Error Response: {}", body);
                            return Mono.error(new ErrorTypeException("API 요청 실패: " + body, CustomErrorType.GEMINI_RESPONSE_ERROR));
                        })))
                .bodyToMono(GeminiChatResponseDto.class)
                .map(LlmChatResponseDto::new);
    }

    @Override
    public Flux<LlmChatResponseDto> getChatCompletionStream(LlmChatRequestDto requestDto) {
        GeminiChatRequestDto geminiChatRequestDto = new GeminiChatRequestDto(requestDto);
        AtomicInteger counter = new AtomicInteger(0);

        return webClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-lite:streamGenerateContent?key=" + geminiApiKey)
                .bodyValue(geminiChatRequestDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(body -> {
                            log.error("Error Response: {}", body);
                            return Mono.error(new ErrorTypeException("API 요청 실패: " + body, CustomErrorType.GEMINI_RESPONSE_ERROR));
                        })))
                .bodyToFlux(GeminiChatResponseDto.class)
                .map(LlmChatResponseDto::new);
    }

    @Override
    public LlmType getLlmType() {
        return LlmType.GEMINI;
    }
}
