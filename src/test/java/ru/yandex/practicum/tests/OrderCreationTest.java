package ru.yandex.practicum.tests;

import api.client.OrdersClient;
import api.util.variables.BaseUri;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class OrderCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUri.BASE_URI;
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    @Test
    @DisplayName("Check success response for order creation with authorization")
    @Description("Order creation with authorization. Checking status code 200 and success response body")
    public void creatingOrderWithAuthReturnsSuccessResponse() {
        OrdersClient ordersClient = new OrdersClient();
        Response successResponse = ordersClient.getOrderCreationWithAuthMethodResponse(
                "test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "61c0c5a71d1f82001bdaaa6d");
        successResponse
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check success response for order creation without authorization")
    @Description("Order creation without authorization. Checking status code 200 and success response body")
    public void creatingOrderWithoutAuthReturnsSuccessResponse() {
        OrdersClient ordersClient = new OrdersClient();
        Response successResponse = ordersClient.getOrderCreationWithoutAuthMethodResponse(
                "61c0c5a71d1f82001bdaaa6d");
        successResponse
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check client error response for authorized order creation without ingredients")
    @Description("Authorized order creation without ingredients. Checking status code 400 and client error response body")
    public void creatingAuthOrderWithoutIngredientsReturnsClientError() {
        OrdersClient ordersClient = new OrdersClient();
        Response clientErrorResponse = ordersClient.getOrderCreationWithAuthMethodResponse(
                "test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                null);
        clientErrorResponse
                .then().statusCode(400)
                .and()
                .assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Check client error response for unauthorized order creation without ingredients")
    @Description("Unauthorized order creation without ingredients. Checking status code 400 and client error response body")
    public void creatingNotAuthOrderWithoutIngredientsReturnsClientError() {
        OrdersClient ordersClient = new OrdersClient();
        Response clientErrorResponse = ordersClient.getOrderCreationWithoutAuthMethodResponse(null);
        clientErrorResponse
                .then().statusCode(400)
                .and()
                .assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Check server error response for authorized order creation with invalid ingredients")
    @Description("Authorized order creation with invalid ingredients. Checking status code 500 and server error response body")
    public void creatingAuthOrderWithInvalidIngredientsReturnsServerError() {
        OrdersClient ordersClient = new OrdersClient();
        Response serverErrorResponse = ordersClient.getOrderCreationWithAuthMethodResponse(
                "test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "61c0c5a71d1f82001bdaaa6d_test");
        serverErrorResponse
                .then().statusCode(500);
    }

    @Test
    @DisplayName("Check server error response for unauthorized order creation with invalid ingredients")
    @Description("Unauthorized order creation with invalid ingredients. Checking status code 500 and server error response body")
    public void creatingNotAuthOrderWithInvalidIngredientsReturnsServerError() {
        OrdersClient ordersClient = new OrdersClient();
        Response serverErrorResponse = ordersClient.getOrderCreationWithoutAuthMethodResponse("61c0c5a71d1f82001bdaaa6d_test");
        serverErrorResponse
                .then().statusCode(500);
    }
}
