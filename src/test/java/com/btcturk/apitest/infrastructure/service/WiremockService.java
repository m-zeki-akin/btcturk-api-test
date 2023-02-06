package com.btcturk.apitest.infrastructure.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Slf4j
public class WiremockService {
    private WireMockServer wireMockServer;
    private final int PORT;

    public WiremockService(int port){
        this.PORT = port;
        this.wireMockServer = new WireMockServer(options().port(port));
    }

    public void start(){
        wireMockServer.start();
        log.info("Wiremock has started on port {}.", PORT);
    }

    public void stop(){
        wireMockServer.start();
        log.info("Wiremock has been shutdown.");
    }

}
