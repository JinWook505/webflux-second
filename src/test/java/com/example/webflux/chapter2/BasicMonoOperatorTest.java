package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class BasicMonoOperatorTest {

    //just, empty
    @Test
    public void startMonoFromData() {
        Mono.just(1).subscribe(data -> System.out.println("data = " + data));

        //ex) 사소한 에러가 발생했을 때 로그를 남기고 empty인 Mono를 전파
        Mono.empty().subscribe(data -> System.out.println("empty data = " + data));
    }

    //fromCallable, defer

    /**
     * fromCallable -> 동기적인 객체를 반환할 때 사용
     * defer -> Mono를 반환하고 싶을 때 사용합니다.
     */
    @Test
    public void startMonoFromFunction() {
        /**
         * 임시 마이그래이션
         * restTemplate, JPA >> 블로킹이 발생하는 라이브러리 Mono로 스레드 분리하여 처리
         */
        Mono<String> monoFromCallable = Mono.fromCallable(() -> {
            //우리 로직을 실행하고
            return callRestTemplate("안녕!");
        }).subscribeOn(Schedulers.boundedElastic());

        /**
         * Mono객체를 Mono객체로 반환하고 있어요
         */
        Mono<String> monoFromDefer = Mono.defer(() -> {
            return callWebClient("안녕!");
        });
        monoFromDefer.subscribe();

        Mono<String> monoFromJust = callWebClient("안녕!");
    }

    @Test
    public void testDeferNecessity() {
        //abc를 만드는 로직도 Mono의 흐름 안에서 관리하고싶다!
        //하나의 큰 흐름을 하나의 Mono안에서 관리하고 싶을 때
        Mono<String> stringMono = Mono.defer(() -> {
            String a = "안녕";
            String b = "하세"; //blocking!
            String c = "요";
            return callWebClient(a + b + c);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> callWebClient(String request) {
        return Mono.just(request + "callWebClient");
    }

    public String callRestTemplate(String request) {
        return request + "callRestTemplate 응답";
    }
    /**
     * Mono의 흐름 시작 방법
     * 1.데이터로부터 시작 -> 일반적인 경우 just / 특이한 상황 empty (Optional.empty())
     * 2.함수로부터 시작
     *      -> 동기적인 객체를 Mono로 반환하고 싶을 때 fromCallable / 코드의 흐름을 Mono안에서 관리하면서 Mono를 반환하고 싶을 때 defer
     */

    @Test
    public void testBasicFluxMono() {
        Flux.<Integer>just(1,2,3,4,5)  //첫번째는 빈 함수로부터 , 두 번째는 데이터로부터 시작할 수 있습니다.
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .subscribe(data -> System.out.println("Flux가 구독한 data! = " + data));
        //1. just 데이터로부터 흐름을 시작했습니다.
        //2. map과 filter같은 연산자로 데이터를 가공했습니다.
        //3. subscribe하면서 데이터를 방출시켰습니다.

        //Mono 0개부터 1개의 데이터만 방출할 수 있는 객체 -> Optional 정도
        //Flux 0개 이상의 데이터를 방출할 수 있는 객체 -> List, Stream 0개 이상의 데이터 방출

        Mono.<Integer>just(2)  //첫번째는 빈 함수로부터 , 두 번째는 데이터로부터 시작할 수 있습니다.
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .subscribe(data -> System.out.println("Mono가 구독한 data! = " + data));
    }
    //흐름 시작 / 데이터 가공 / 구독

    //mono에서 데이터 방출의 개수가 많아져서 Flux로 바꾸고 싶다. -> flatMapMany
    @Test
    public void monoToFlux() {
        Mono<Integer> one = Mono.just(1);
        Flux<Integer> integerFlux = one.flatMapMany(data -> {
            return Flux.just(data, data + 1, data + 2);
        });
        integerFlux.subscribe(data -> System.out.println("data = " + data));
    }

    //flatmap, collectlist << 다음시간에!
}
