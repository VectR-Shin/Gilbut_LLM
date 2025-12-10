package com.gilbut.llmService.Service.RosServiceTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbut.llmService.DTO.RosMessageDTO.RosMessageDTO;
import com.gilbut.llmService.DTO.RosMessageDTO.RosStatusType;
import com.gilbut.llmService.Log.LoggingTestExecutionOrderExtension;
import com.gilbut.llmService.Properties.RosProperties;
import com.gilbut.llmService.Service.RosService;
import com.gilbut.llmService.WebSocketClient.RosClient;
import com.gilbut.llmService.WebSocketClient.RosWebSocketClient;
import org.junit.jupiter.api.*;
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
        lenient().when(rosProperties.getUri()).thenReturn("ws://localhost:9090");
    }

    @Test
    void send_whenClientOpen_test() throws Exception {
        // given
        doNothing().when(client).connect();

        // 초기 연결
        rosService.init();
        rosService.setConnected(true);

        RosMessageDTO rosDTO = new RosMessageDTO();
        rosDTO.setStatus(RosStatusType.SUCCESS);

        // when
        rosService.send(rosDTO);

        // then
        Mockito.verify(client, times(1)).send(anyString());
        verify(client, never()).reconnect();
    }

    @Test
    void send_initConnectFail_test() throws Exception {
        // given
        // 초기 connect 실패
        doThrow(new RuntimeException()).when(client).connect();

        // 이후의 reconnect 성공
        doNothing().when(client).reconnect();

        // when
        rosService.init();

        // 비동기 재연결 대기
        Thread.sleep(1200);

        // then
        verify(client, times(1)).connect();
    }

    @Test
    void send_connectFail_test() throws Exception{
        // given
        doNothing().when(client).connect();
        doNothing().when(client).reconnect();

        rosService.init();
        rosService.setConnected(true);

        // send 중 오류 발생 -> 연결 끊김
        doThrow(new RuntimeException("[MOCK] send() 실패")).when(client).send(anyString());

        RosMessageDTO rosDTO = new RosMessageDTO();
        rosDTO.setStatus(RosStatusType.SUCCESS);

        // when
        rosService.send(rosDTO);
        rosService.onConnectionLost(1006);

        // 비동기 재연결 대기 (실제 코드에서는 1000부터 시작)
        Thread.sleep(1200);

        // then
        verify(client, times(1)).send(anyString());
        verify(client, atLeastOnce()).reconnect();
    }
}
