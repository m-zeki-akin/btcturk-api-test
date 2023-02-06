package com.btcturk.apitest.test;

import com.btcturk.apitest.infrastructure.service.MapperService;
import com.btcturk.apitest.util.ReaderUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;

@Slf4j
@Listeners({
        com.btcturk.apitest.infrastructure.listener.SuiteListener.class,
        com.btcturk.apitest.infrastructure.listener.TestListener.class
})
public class BaseTest {

    protected RequestSpecification given() {
        RestAssured.reset();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config.objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                .jackson2ObjectMapperFactory((type, s) -> MapperService.getInstance().getObjectMapper()));
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .relaxedHTTPSValidation()
                .log().all();

    }

    @SneakyThrows
    protected <T> T getJson(Class<T> tClass, String path) {
        return MapperService.getInstance().getObjectMapper().readValue(ReaderUtils.getJsonFile(path), tClass);
    }

    @SneakyThrows
    protected <T> T getJson(TypeReference<T> tClass, String path) {
        return MapperService.getInstance().getObjectMapper().readValue(ReaderUtils.getJsonFile(path), tClass);
    }
}
