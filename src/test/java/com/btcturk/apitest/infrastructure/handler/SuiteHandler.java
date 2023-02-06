package com.btcturk.apitest.infrastructure.handler;

import com.btcturk.apitest.infrastructure.service.MapperService;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;

import java.util.Locale;

public class SuiteHandler {

    private final ThreadLocal<TestHandler> testHandlerThread = ThreadLocal.withInitial(TestHandler::new);
    private static SuiteHandler suiteListener;

    public static SuiteHandler getInstance() {
        if (suiteListener == null) {
            suiteListener = new SuiteHandler();
        }
        return suiteListener;
    }

    private SuiteHandler() {
        Locale.setDefault(Locale.US);
        JsonPath.config = JsonPathConfig.jsonPathConfig()
                .jackson2ObjectMapperFactory((type, s) -> MapperService.getInstance().getObjectMapper());
    }

    public TestHandler getTestMethod() {
        return testHandlerThread.get();
    }

    public void setTestMethod(TestHandler testHandler) {
        testHandlerThread.set(testHandler);
    }

}
