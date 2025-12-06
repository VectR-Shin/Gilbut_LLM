package com.gilbut.llmService.Log;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class LoggingTestExecutionOrderExtension implements BeforeTestExecutionCallback {
    private static int counter = 1;

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        String testName = context.getDisplayName();
        log.info("[TEST] {} 번째 실행: {}", counter++, testName);
    }
}
