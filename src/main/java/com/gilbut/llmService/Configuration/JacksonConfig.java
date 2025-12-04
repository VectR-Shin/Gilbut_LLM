package com.gilbut.llmService.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * JSON 과 DTO 파싱할 때 enum(status)의 대/소문자 구분을 없애기 위한 설정 << deprecated 되었음.. 기능 생략
 */
@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }
}
