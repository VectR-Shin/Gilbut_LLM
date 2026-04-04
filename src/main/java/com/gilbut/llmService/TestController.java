package com.gilbut.llmService;

import com.gilbut.llmService.Service.Navigation.RosService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * RosService - sendCancel, sendStop 테스트를 위한 엔드포인트
 */

@RestController
@RequiredArgsConstructor
public class TestController {
    private final RosService rosService;

    @GetMapping("/testCancel")
    public String testCancel() throws InterruptedException {
        rosService.sendCancel();
        Thread.sleep(100);

        for (int i = 0; i < 3; i++) {
            rosService.sendStop();
            Thread.sleep(50);
        }

        return "OK";
    }
}
