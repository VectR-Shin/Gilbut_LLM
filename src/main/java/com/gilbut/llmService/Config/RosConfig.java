package com.gilbut.llmService.Config;

import com.gilbut.llmService.Properties.RosProperties;
import com.gilbut.llmService.WebSocketClient.RosWebSocketClient;
import com.gilbut.llmService.WebSocketClient.RosClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class RosConfig {
    private final RosProperties rosproperties;

    @Bean
    public RosClient rosWebSocketClient(RosProperties rosProperties) throws Exception {
        return new RosWebSocketClient(new URI(rosProperties.getUri()));
    }
}
