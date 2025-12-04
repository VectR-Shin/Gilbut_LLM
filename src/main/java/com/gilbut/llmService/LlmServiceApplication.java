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
 * 일단은, LLM 에게서 GPS 좌표만을 받도록 했다.
 * 이후에 텍스트도 받는 경우를 상정하여, 미리 구현만 해 두었다.
 * DTO.LLMMessageDTO, DTO.ROSMessageDTO 및 DTO.DTOStatus 참조
 */