package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicFluxOperatorTest {

    /**
     * Flux
     * 데이터 : just, empty, from~시리즈
     * 함수 : defer, create
     */
    @Test
    public void testFluxFromData() {
        Flux.just(1,2,3,4)
                .subscribe(data -> System.out.println("data = " + data));

        List<Integer> basicList = List.of(1, 2, 3, 4);
        Flux.fromIterable(basicList)
                .subscribe(data -> System.out.println("data fromIterable = " + data));
    }

    /**
     * Flux defer -> 안에서 Flux객체를 반환해줘야 합니다.
     * Flux create -> 동기적인 객체, Flux,Mono객체도 자유롭게 반환 가능합니다.
     */
    @Test
    public void testFluxFromFunction() {
        Flux.defer(() -> {
            return Flux.just(1,2,3,4);
        }).subscribe(data -> System.out.println("data from defer = " + data));
    }
}
