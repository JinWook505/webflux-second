package com.example.webflux.service.user.chat;

import com.example.webflux.model.user.chat.UserChatRequestDto;
import com.example.webflux.model.user.chat.UserChatResponseDto;
import reactor.core.publisher.Flux;

public interface ChainOfThoughtService {

    Flux<UserChatResponseDto> getChainOfThoughtResponse(UserChatRequestDto responseDto);
}
