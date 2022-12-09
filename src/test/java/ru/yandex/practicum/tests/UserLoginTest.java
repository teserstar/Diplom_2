package ru.yandex.practicum.tests;

import api.client.UserClient;
import api.util.variables.BaseUri;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUri.BASE_URI;
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    @Test
    @DisplayName("Check success response for user authorization")
    @Description("User authorization. Checking status code 200 and success response body")
    public void loginUserReturnsSuccessResponse() {
        UserClient userClient = new UserClient();
        userClient.getUserCreationMethodResponse("test_" + randomString + "@yandex.ru",
                "1234_" + randomString, "tester_" + randomString);
        Response successResponse = userClient.getUserLoginMethodResponse("test_" + randomString + "@yandex.ru",
                        "1234_" + randomString);
        successResponse
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check client error response for incorrect user authorization")
    @Description("Incorrect email and password user authorization. Checking status code 401 and client error response body")
    public void loginUserWithIncorrectEmailAndPasswordReturnsClientError() {
        UserClient userClient = new UserClient();
        userClient.getUserCreationMethodResponse("test_" + randomString + "@yandex.ru",
                "1234_" + randomString, "tester_" + randomString);
        Response clientErrorResponse = userClient.getUserLoginMethodResponse("attempt_" + randomString + "@yandex.ru",
                "4321_" + randomString);
        clientErrorResponse
                .then().statusCode(401)
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
