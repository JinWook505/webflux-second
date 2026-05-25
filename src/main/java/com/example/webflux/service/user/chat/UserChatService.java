package com.example.webflux.service.user.chat;

import com.example.webflux.model.user.chat.UserChatRequestDto;
import com.example.webflux.model.user.chat.UserChatResponseDto;
import reactor.core.publisher.Mono;

public interface UserChatService {
    Mono<UserChatResponseDto> getOneShotChat(UserChatRequestDto userChatRequestDto);
}
