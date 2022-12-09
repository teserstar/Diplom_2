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

public class UserChangeTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUri.BASE_URI;
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    @Test
    @DisplayName("Check success response for changing user email")
    @Description("Changing user email. Checking status code 200 and success response body")
    public void changingUserEmailReturnsSuccessResponse() {
        UserClient userClient = new UserClient();
        Response emailSuccessResponse = userClient.getAuthUserChangeMethodResponse(
                "test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "attempt_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString);
        emailSuccessResponse
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check success response for changing user password")
    @Description("Changing user password. Checking status code 200 and success response body")
    public void changingUserPasswordReturnsSuccessResponse() {
        UserClient userClient = new UserClient();
        Response passwordSuccessResponse = userClient.getAuthUserChangeMethodResponse(
                "test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "test_" + randomString + "@yandex.ru", "4321_" + randomString, "tester_" + randomString);
        passwordSuccessResponse
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check success response for changing user name")
    @Description("Changing user name. Checking status code 200 and success response body")
    public void changingUserNameReturnsSuccessResponse() {
        UserClient userClient = new UserClient();
        Response nameSuccessResponse = userClient.getAuthUserChangeMethodResponse(
                "test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "test_" + randomString + "@yandex.ru", "1234_" + randomString, "user_" + randomString);
        nameSuccessResponse
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check client error response for changing user without authorization")
    @Description("Changing user without authorization. Checking status code 401 and client error response body")
    public void changingUserWithoutAuthReturnsClientError() {
        UserClient userClient = new UserClient();
        Response clientErrorResponse = userClient.getNoAuthUserChangeMethodResponse(
                "test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString);
        clientErrorResponse
                .then().statusCode(401)
                .and()
                .assertThat().body("success", equalTo(false));
    }
}
