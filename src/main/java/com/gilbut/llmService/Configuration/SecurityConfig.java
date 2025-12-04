package com.gilbut.llmService.Configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/*
 * 1. 모든 HTTP 요청 O
 * 2. CSRF 비활성화 -> WebSocket handshake 차단 X
 * 3. 로그인 페이지 X
 * 4. 기본 인증 X
 * 5. H2 콘솔 (/h2-console) 허용
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))// H2 브라우저 콘솔 위해서
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()// H2 콘솔 허용
                        .anyRequest().permitAll())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}
