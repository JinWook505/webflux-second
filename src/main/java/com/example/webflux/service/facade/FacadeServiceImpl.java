package com.example.webflux.service.facade;

import com.example.webflux.model.facade.FacadeAvailableModel;
import com.example.webflux.model.facade.FacadeHomeResponseDto;
import com.example.webflux.model.llmclient.LlmModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class FacadeServiceImpl implements FacadeService {

    @Override
    public Mono<FacadeHomeResponseDto> getFacadeHomeResponseDto() {
        return Mono.fromCallable(() ->
                new FacadeHomeResponseDto(
                        Arrays.stream(LlmModel.values())
                                .map(availableModel ->
                                        new FacadeAvailableModel(availableModel.name(), availableModel.getCode()))
                                .toList()));
    }
}
