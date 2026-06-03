package com.example.webflux.service.user.chat;

import com.example.webflux.exception.CustomErrorType;
import com.example.webflux.exception.ErrorTypeException;
import com.example.webflux.model.llmclient.LlmChatRequestDto;
import com.example.webflux.model.llmclient.LlmChatResponseDto;
import com.example.webflux.model.llmclient.LlmModel;
import com.example.webflux.model.llmclient.LlmType;
import com.example.webflux.model.llmclient.jsonformat.AnswerListResponseDto;
import com.example.webflux.model.user.chat.UserChatRequestDto;
import com.example.webflux.model.user.chat.UserChatResponseDto;
import com.example.webflux.service.llmclient.LlmWebClientService;
import com.example.webflux.util.ChatUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChainOfThoughtServiceImpl implements ChainOfThoughtService {


    private final Map<LlmType, LlmWebClientService> llmWebClientServiceMap;
    private final ObjectMapper objectMapper;


    /*
        1. 사용자의 요청을 효율적으로 분석하기 위한 단계를 LLM에게 위임
            -> answerList : 분석 단계를 LLM이 응답

        2. 분석 단계별로 LLM 에게 요청을 보내 상세하게 분석

        3. 단계별로 분석된 결과를 종합하여 최종 응답.
     */
    @Override
    public Flux<UserChatResponseDto> getChainOfThoughtResponse(UserChatRequestDto requestDto) {
        return Flux.create(sink -> {
            String userRequest = requestDto.getRequest();
            LlmModel requestModel = requestDto.getLlmModel();

            String establishingThoughtChainPrompt = String.format("""
                    다음은 사용자의 입력입니다. : "%s"
                    사용자에게 체계적으로 답변하기 위해 어떤 단계들이 필요할 지 정리해주세요.
                    """, userRequest);

            String establishingThoughtChainSystemPrompt = """
                    아래의 List<String> answerList 형태를 가지는 JSON FORMAT으로 응답하다.
                    <JSONSCHEMA>
                    {
                        "answerList" : ["", ...]
                    }
                    </JSONSCHEMA>
                    """;

            LlmChatRequestDto llmChatRequestDto =
                    new LlmChatRequestDto(establishingThoughtChainPrompt,
                            establishingThoughtChainSystemPrompt,
                            true,
                            requestModel);

            LlmWebClientService llmWebClientService = llmWebClientServiceMap.get(requestModel.getLlmType());

            Mono<AnswerListResponseDto> ctoStepListMono = llmWebClientService.getChatCompletion(llmChatRequestDto)
                    .map(response -> {
                        String llmResponse = response.getLlmResponse();
                        log.info("llmResponse => {}", llmResponse);
                        String extractJsonString = ChatUtils.extractJsonString(llmResponse);

                        try {
                            return objectMapper.readValue(extractJsonString, AnswerListResponseDto.class);
                        } catch (JsonProcessingException e) {
                            throw new ErrorTypeException("[JsonParseException] Json Parse Error. extractJsonString : " + extractJsonString, CustomErrorType.LLM_RESPONSE_JSON_PARSE_ERROR);
                        }
                    })
                    .doOnNext(publishedData ->
                            sink.next(new UserChatResponseDto(publishedData.toString(), "필요한 작업 단계 분석")));

            Flux<String> cotStepFlux = ctoStepListMono.flatMapMany(cotStepList ->
                    Flux.fromIterable(cotStepList.getAnswerList()));


            Flux<String> analyzedCotStep = cotStepFlux.flatMapSequential(cotStep -> {
                        String cotStepRequestPrompt = String.format("""
                                다음은 사용자의 입력입니다. : %s
                                                        
                                사용자의 요구를 다음 단계에 따라 분석해주세요. : %s
                                """, userRequest, cotStep);

                        return llmWebClientService
                                .getCharCompletionWithCatchException(
                                        new LlmChatRequestDto(cotStepRequestPrompt,
                                                "",
                                                false,
                                                requestModel))
                                .map(LlmChatResponseDto::getLlmResponse);
                    })
                    .doOnNext(publishedData -> sink.next(new UserChatResponseDto(publishedData, "단계별 분석")));


            Mono<String> finalAnswerMono = analyzedCotStep.collectList().flatMap(stepPromptList -> {
                String concatStepPrompt = String.join("\n", stepPromptList);

                String finalAnswerPrompt = String.format("""
                        다음은 사용자의 입력입니다. : %s
                        아래 사항들을 참고, 분석 하여 사용자의 입력에 대한 최종 답변을 해라.
                        """, userRequest, concatStepPrompt);

                return llmWebClientService
                        .getCharCompletionWithCatchException(
                                new LlmChatRequestDto(finalAnswerPrompt,
                                        "",
                                        false,
                                        requestModel))
                        .map(LlmChatResponseDto::getLlmResponse);
            });

            finalAnswerMono.subscribe(finalAnswer -> {
                sink.next(new UserChatResponseDto(finalAnswer, "최종 응답"));
                sink.complete();
            }, error -> {
                log.error("[COT] cot response error", error);
                sink.error(error);
            });
        });
    }
}
