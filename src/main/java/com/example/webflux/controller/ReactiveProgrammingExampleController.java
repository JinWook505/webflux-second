package com.example.webflux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reactive")
@Slf4j
public class ReactiveProgrammingExampleController {

    @GetMapping("/onenine/legacy")
    public Mono<List<Integer>> produceOneToNineLegacy() {
        return Mono.fromCallable(() -> {
            List<Integer> sink = new ArrayList<>();
            for (int i = 1 ; i <= 9 ; i++) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
                sink.add(i);
            } //4.5초 소요
            return sink;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    //1~9까지 출력하는 api
    @GetMapping("/onenine/list")
    public Mono<List<Integer>> produceOneToNine() {
        return Mono.defer(() -> {
            List<Integer> sink = new ArrayList<>();
            for (int i = 1 ; i <= 9 ; i++) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
                sink.add(i);
            } //4.5초 소요
            return Mono.just(sink);
        }).subscribeOn(Schedulers.boundedElastic());
    }
//    Mono 어쩌구 = webflux.produceOneToNine();
//    어쩌구.subscribe();

    @GetMapping("/onenine/flux")
    public Flux<Integer> produceOneToNineFlux() {
        return Flux.<Integer>create(sink -> {
            for (int i = 1 ; i <= 9 ; i++) {
                try {
                    log.info("현재 처리하고 있는 스레드 이름 : {}", Thread.currentThread().getName()); //reactor-http-nio-x 얘가 블로킹 되지 않게 만들어야함.
                    Thread.sleep(500);
                } catch (Exception e) {
                }
                sink.next(i);
            } //4.5초 소요

            sink.complete();
        }).subscribeOn(Schedulers.boundedElastic());
    }
    //리액티브 스트림 구현체 Flux, Mono를 사용하여 발생하는 데이터를 바로바로 리액티브하게 처리
    //비동기로동작 - 논 블로킹하게 동작 해야한다

    //리액티브 프로그래밍 필수 요소
    //1. 데이터가 준비 될 때 마다 바로바로 리액티브하게 처리 > 리엑티브 스트림 구현체 Flux, Mono를 사용하여 발생하는 데이터 바로바로 처리
    //2. 로직을 짤 때는 반드시 논 블로킹하게 짜야한다 > 이를 위해 **비동기 프로그래밍**이 필요하다.
}
