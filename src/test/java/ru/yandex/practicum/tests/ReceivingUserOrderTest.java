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

public class ReceivingUserOrderTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUri.BASE_URI;
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    @Test
    @DisplayName("Check success response for receiving user order with authorization")
    @Description("Receiving user order with authorization. Checking status code 200 and success response body")
    public void receivingUserOrderWithAuthReturnsSuccessResponse() {
        OrdersClient ordersClient = new OrdersClient();
        Response successResponse = ordersClient.getReceivingUserOrderWithAuthMethodResponse(
                "test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "61c0c5a71d1f82001bdaaa6d");
        successResponse
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check client error response for receiving user order without authorization")
    @Description("Receiving user order without authorization. Checking status code 401 and client error response body")
    public void receivingUserOrderWithoutAuthReturnsClientError() {
        OrdersClient ordersClient = new OrdersClient();
        Response clientErrorResponse = ordersClient.getReceivingUserOrderWithoutAuthMethodResponse();
        clientErrorResponse
                .then().statusCode(401)
                .and()
                .assertThat().body("success", equalTo(false));
    }
}
