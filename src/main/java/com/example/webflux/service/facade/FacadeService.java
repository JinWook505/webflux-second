package com.example.webflux.service.facade;

import com.example.webflux.model.facade.FacadeHomeResponseDto;
import reactor.core.publisher.Mono;

public interface FacadeService {

    Mono<FacadeHomeResponseDto> getFacadeHomeResponseDto();
}
