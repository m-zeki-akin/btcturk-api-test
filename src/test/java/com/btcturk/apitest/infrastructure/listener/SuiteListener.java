package com.btcturk.apitest.infrastructure.listener;

import com.btcturk.apitest.infrastructure.handler.SuiteHandler;
import com.btcturk.apitest.infrastructure.service.WiremockService;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class SuiteListener implements ISuiteListener {

    private static final Integer WIREMOCK_PORT = 8089;
    private final WiremockService wiremockService = new WiremockService(WIREMOCK_PORT);

    @Override
    public void onStart(ISuite suite) {
        SuiteHandler.getInstance(); // instantiate SuiteHandler
        wiremockService.start();
    }

    @Override
    public void onFinish(ISuite suite) {
        wiremockService.stop();
    }

}
