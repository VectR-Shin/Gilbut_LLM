package com.gilbut.llmService.Service.RosServiceTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.DTOStatus.RosStatusType;
import com.gilbut.llmService.DTO.RosMessageDTO;
import com.gilbut.llmService.Log.LoggingTestExecutionOrderExtension;
import com.gilbut.llmService.Properties.RosProperties;
import com.gilbut.llmService.Service.RosService;
import com.gilbut.llmService.WebSocketClient.RosClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/*
 * RosService 의 Mocking 기반 유닛 테스트
 */

@TestMethodOrder(MethodOrderer.Random.class)
@ActiveProfiles("test")
@ExtendWith({
        LoggingTestExecutionOrderExtension.class,
        MockitoExtension.class
})
@Transactional
public class RosServiceTest {
    @Mock
    private RosClient client;

    @Mock
    private RosProperties rosProperties;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private RosService rosService;

    @BeforeEach
    void setUp() throws Exception {
        when(rosProperties.getUri()).thenReturn("ws://localhost:9090");
        doNothing().when(client).connect();
        rosService.init();
    }

    private void rosServiceConnectedTrue() throws Exception {
        rosService.send(new RosMessageDTO());// connected 의 true 변경을 목적으로 실행
    }

    @Test
    void send_whenClientOpen_test() throws Exception {
        // rosService 의 connected 를 true 로 세팅
        rosService.setConnected(true);

        // given
        RosMessageDTO rosDTO = new RosMessageDTO();
        rosDTO.setStatus(RosStatusType.SUCCESS);

        // when
        rosService.send(rosDTO);

        // then
        Mockito.verify(client, times(1)).send(anyString());
        verify(client, never()).reconnect();
    }

    @Test
    void send_whenClientClose_test() throws Exception {
        // rosService 의 connected 를 false 로 세팅
        rosService.setConnected(false);

        // given
        RosMessageDTO rosDTO = new RosMessageDTO();
        rosDTO.setStatus(RosStatusType.SUCCESS);

        // when
        rosService.send(rosDTO);

        // then
        verify(client, times(1)).reconnect();
        verify(client, never()).send(anyString());
    }
}
