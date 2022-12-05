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

public class ReceivingUserOrderTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUri.BASE_URI;
    }

    public Response getReceivingUserOrderWithAuthMethodResponse(String userEmail, String userPassword, String userName, String ingredients) {
        UserCreationTest userCreation = new UserCreationTest();
        String accessToken = userCreation
                .getUserCreationMethodResponse(userEmail, userPassword, userName)
                .jsonPath().getString("accessToken");

        OrderCreationRequestBody requestBody = new OrderCreationRequestBody(ingredients);
        given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(requestBody)
                .when()
                .post("/api/orders");

        return given()
                .header("Authorization", accessToken)
                .get("/api/orders");
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    @Test
    @Description("Basic test for GET /api/orders")
    public void receivingUserOrderWithAuthReturnsSuccessResponse() {
        getReceivingUserOrderWithAuthMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "61c0c5a71d1f82001bdaaa6d")
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @Description("Basic test for GET /api/orders")
    public void receivingUserOrderWithoutAuthReturnsClientError() {
        given()
                .get("/api/orders")
                .then().statusCode(401)
                .and()
                .assertThat().body("success", equalTo(false));
    }
}
