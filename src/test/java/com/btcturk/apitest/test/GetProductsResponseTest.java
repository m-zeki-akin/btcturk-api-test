package com.btcturk.apitest.test;

import com.btcturk.apitest.controller.ProductApplicationController;
import com.btcturk.apitest.controller.ProductDataController;
import com.btcturk.apitest.model.response.ErrorResponse;
import com.btcturk.apitest.model.response.GetProductsResponse;
import com.btcturk.apitest.infrastructure.service.MapperService;
import com.btcturk.apitest.infrastructure.user.User;
import com.btcturk.apitest.infrastructure.user.UserPool;
import com.btcturk.apitest.model.dto.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.*;

@Slf4j
public class GetProductsResponseTest extends BaseTest {

    @Test
    public void should_return_error_when_get_products_response_with_invalid_token() throws JsonProcessingException {
        // given
        User user = UserPool.getInstance().retriever().one();
        ProductApplicationController productApplicationController = new ProductApplicationController(user);
        // when
        ErrorResponse response = given().baseUri(productApplicationController.getBaseUrl())
                .auth().oauth2("fail")
                .when()
                .get("/products")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract()
                .response()
                .as(ErrorResponse.class);
        // then
        assertEquals(response.getMessage(), "Invalid Token");
    }

    @Test
    public void should_return_success_when_get_products_response_and_products_are_same() throws JsonProcessingException {
        // given
        User user = UserPool.getInstance().retriever().one();
        ProductDataController productDataController = new ProductDataController(user);
        String productsString = given().baseUri(productDataController.getBaseUrl())
                .contentType("text/html")
                .when()
                .get("/example_products.json")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .prettyPrint();
        List<Product> products = MapperService.getInstance().getObjectMapper()
                .readValue(productsString, new TypeReference<List<Product>>() {
                });
        // when
        ProductApplicationController productApplicationController = new ProductApplicationController(user);
        List<GetProductsResponse> response = given().baseUri(productApplicationController.getBaseUrl())
                .auth().oauth2(user.getAccessToken())
                .when()
                .get("/products")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .prettyPeek()
                .as(new TypeRef<List<GetProductsResponse>>() {
                });
        // then
        assertEquals(response.size(), products.size());
        for (int i = 0; i < response.size(); i++) {
            log.info("Comparing -> response: {} - data: {}", response.get(i), products.get(i));
            assertEquals(response.get(i).getId(), products.get(i).getId());
            assertEquals(response.get(i).getTitle(), products.get(i).getTitle());
            assertEquals(response.get(i).getDescription(), products.get(i).getDescription());
            assertEquals(response.get(i).getPrice(), products.get(i).getPrice());
            assertEquals(response.get(i).getIsBasketDiscount(), products.get(i).getIsBasketDiscount());
            assertEquals(response.get(i).getBasketDiscountPercentage(), products.get(i).getBasketDiscountPercentage());
            assertEquals(response.get(i).getRating(), products.get(i).getRating());
            assertEquals(response.get(i).getStock(), products.get(i).getStock());
            assertEquals(response.get(i).getIsActive(), products.get(i).getIsActive());
            assertEquals(response.get(i).getBrand(), products.get(i).getBrand());
            assertEquals(response.get(i).getCategory(), products.get(i).getCategory());
            assertEquals(response.get(i).getImages(), products.get(i).getImages());
        }
    }

    @Test
    public void should_return_success_when_get_products_response_has_no_duplicate_ids() {
        // given
        User user = UserPool.getInstance().retriever().one();
        // when
        ProductApplicationController productApplicationController = new ProductApplicationController(user);
        List<GetProductsResponse> response = given().baseUri(productApplicationController.getBaseUrl())
                .auth().oauth2(user.getAccessToken())
                .when()
                .get("/products")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .prettyPeek()
                .as(new TypeRef<List<GetProductsResponse>>() {
                });
        // then
        List<Integer> ids = new ArrayList<>();
        response.stream().iterator().forEachRemaining(o -> ids.add(o.getId()));
        Set<Integer> idSet = new HashSet<>(ids);
        log.info("ID List -> {}", ids);
        log.info("ID Set -> {}", idSet);
        assertEquals(idSet.size(), ids.size());
    }

    @Test
    public void should_return_success_when_get_products_response_images_are_shown() {
        // given
        User user = UserPool.getInstance().retriever().one();
        // when
        ProductApplicationController productApplicationController = new ProductApplicationController(user);
        List<GetProductsResponse> response = given().baseUri(productApplicationController.getBaseUrl())
                .auth().oauth2(user.getAccessToken())
                .when()
                .get("/products")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .prettyPeek()
                .as(new TypeRef<List<GetProductsResponse>>() {
                });
        // then
        for (GetProductsResponse product : response) {
            product.getImages().stream().iterator().forEachRemaining(imageUrl ->
            {
                Response imageResponse = given().baseUri(productApplicationController.getBaseUrl())
                        .noContentType()
                        .accept("image/avif,image/webp,image/apng,*/*")
                        .auth().oauth2(user.getAccessToken())
                        .when()
                        .get(imageUrl)
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .extract()
                        .response();
                log.info("\n{}", imageResponse.getHeaders());
                assertNotNull(imageResponse.getHeader("content-length"));
                assertTrue(imageResponse.getHeader("content-type").startsWith("image/webp"));
            });
        }
    }

    @Test(description = "When product has basket discount then basket discount percentage is exist and not exceeds 50.")
    public void should_return_success_when_get_products_response_if_is_basket_discount_then_basket_discount_percentage_is_exist_and_not_exceeds_50() {
        // given
        User user = UserPool.getInstance().retriever().one();
        // when
        ProductApplicationController productApplicationController = new ProductApplicationController(user);
        List<GetProductsResponse> response = given().baseUri(productApplicationController.getBaseUrl())
                .auth().oauth2(user.getAccessToken())
                .when()
                .get("/products")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .prettyPeek()
                .as(new TypeRef<List<GetProductsResponse>>() {
                });
        // then
        response.stream().iterator().forEachRemaining(product -> {
                    if (product.getIsBasketDiscount()) {
                        assertTrue(product.getBasketDiscountPercentage() < 51);
                        assertTrue(product.getBasketDiscountPercentage() > 0);
                    }
                }
        );
    }

    @Ignore // TODO: we have no logic attached to isActive. After the implementation update this test.
    @Test
    public void should_return_success_when_get_products_response_if_not_is_active_then_do() {
        // given
        User user = UserPool.getInstance().retriever().one();
        // when
        ProductApplicationController productApplicationController = new ProductApplicationController(user);
        List<GetProductsResponse> response = given().baseUri(productApplicationController.getBaseUrl())
                .auth().oauth2(user.getAccessToken())
                .when()
                .get("/products")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .prettyPeek()
                .as(new TypeRef<List<GetProductsResponse>>() {
                });
        // then
        response.stream().iterator().forEachRemaining(product -> {
                    if (product.getIsActive()) {
                        // do something
                        }
                }
        );
    }

}
