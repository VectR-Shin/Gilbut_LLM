package com.gilbut.llmService.Service.RosServiceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.gilbut.llmService.DTO.DTOStatus.RosStatusType;
import com.gilbut.llmService.DTO.RosMessageDTO;
import com.gilbut.llmService.Service.RosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/*
 * RosService 의 실제 서버 기반의 통합 테스트
 *
 * !!!!! 반드시 아래의 방법을 준수 !!!!!
 *
 * 테스트 방법
 * 1. SimpleRosServer 를 따로 실행한다.
 * 2. RosIntegrationTest 를 테스트한다.
 * 3. SimpleRosServer 에 전송한 DTO 관련 정보가 출력되는 것을 확인한다.
 * 4. 만약, 테스트가 실패한다면, Thread.sleep(2000) 으로 변경
 */
@SpringBootTest
@ActiveProfiles("test")
public class RosServiceIntegrationTest {
    @Autowired
    private RosService rosService;

    @Test
    void send_realWebSocketServer_test() throws Exception {

        RosMessageDTO rosDTO =
                new RosMessageDTO(RosStatusType.SUCCESS, 1.1, 2.4, 5.1, 0.3, 9.0, 0.0, 2.9);

        Thread.sleep(1000);

        rosService.setConnected(true);// 테스트의 편의를 위해서
        rosService.send(rosDTO);

        assertTrue(true);
    }
}
