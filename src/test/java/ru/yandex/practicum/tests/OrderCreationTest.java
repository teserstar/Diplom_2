package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.requests.OrderCreationRequestBody;
import ru.yandex.practicum.variables.BaseUri;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class OrderCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUri.BASE_URI;
    }

    public Response getOrderCreationWithAuthMethodResponse(String userEmail, String userPassword, String userName, String ingredients) {
        UserCreationTest userCreation = new UserCreationTest();
        String accessToken = userCreation
                .getUserCreationMethodResponse(userEmail, userPassword, userName)
                .jsonPath().getString("accessToken");

        OrderCreationRequestBody requestBody = new OrderCreationRequestBody(ingredients);
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(requestBody)
                .when()
                .post("/api/orders");
    }

    public Response getOrderCreationWithoutAuthMethodResponse(String ingredients) {
        OrderCreationRequestBody requestBody = new OrderCreationRequestBody(ingredients);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/api/orders");
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    @Test
    @Description("Basic test for POST /api/orders")
    public void creatingOrderWithAuthReturnsSuccessResponse() {
        getOrderCreationWithAuthMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "61c0c5a71d1f82001bdaaa6d")
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @Description("Basic test for POST /api/orders")
    public void creatingOrderWithoutAuthReturnsSuccessResponse() {
        getOrderCreationWithoutAuthMethodResponse("61c0c5a71d1f82001bdaaa6d")
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @Description("Basic test for POST /api/orders")
    public void creatingAuthOrderWithoutIngredientsReturnsClientError() {
        getOrderCreationWithAuthMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                null)
                .then().statusCode(400)
                .and()
                .assertThat().body("success", equalTo(false));
    }

    @Test
    @Description("Basic test for POST /api/orders")
    public void creatingNotAuthOrderWithoutIngredientsReturnsClientError() {
        getOrderCreationWithoutAuthMethodResponse(null)
                .then().statusCode(400)
                .and()
                .assertThat().body("success", equalTo(false));
    }

    @Test
    @Description("Basic test for POST /api/orders")
    public void creatingAuthOrderWithInvalidIngredientsReturnsServerError() {
        getOrderCreationWithAuthMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "61c0c5a71d1f82001bdaaa6d_test")
                .then().statusCode(500);
    }

    @Test
    @Description("Basic test for POST /api/orders")
    public void creatingNotAuthOrderWithInvalidIngredientsReturnsServerError() {
        getOrderCreationWithoutAuthMethodResponse("61c0c5a71d1f82001bdaaa6d_test")
                .then().statusCode(500);
    }
}
