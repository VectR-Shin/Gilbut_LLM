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

해야 할 일

1. 복합 명령 처리
	1_1. (완료)LlmDTO 재정의
	1_2. (완료)프롬프트 재정의
	1_3. (완료)GeminiService 수정
	1_4. (완료)HinService 수정
	1_5. 오케스트레이션 계층에서 로그 출력 방식으로 테스트
2. 이전 명령 상태 저장 및 경로 재구성
	2_1. 경로 정보 세션 저장 구현
	2_2. 경로 수정 및 취소 구현
	2_2. 복합 명령 처리와의 통합
	2_3. 전체 테스트





 */