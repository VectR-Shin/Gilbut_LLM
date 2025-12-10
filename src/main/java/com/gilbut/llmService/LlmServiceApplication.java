package com.gilbut.llmService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LlmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlmServiceApplication.class, args);
	}

}

/*

할 일

완료. HintEntity 추가
완료. HintRepository 추가
완료. LlmMessageDTO 수정
완료. GeminiService 수정
완료. HintService 추가
6. SttMessageHandler 수정
완료. 프롬프트 수정
8. DBInit 추가


테스트 할 일
완료. HintRepository 테스트
완료. GeminiService 테스트
완료. HintService 테스트
4. 전체 통합 테스트


발표자료에 넣을 내용
1. 크게 2종류로 안내가 가능함
2. Describe 의 경우, Hint 를 사용
3. 이 과정에서 Levenshtein distance 사용
4. LLM 프롬프트
5. 모카비와 이 방식의 차이점?


 */