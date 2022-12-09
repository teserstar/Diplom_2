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

public class UserCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUri.BASE_URI;
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    @Test
    @DisplayName("Check success response for user creation")
    @Description("Creating a user. Checking status code 200 and success response body")
    public void creatingNewUserReturnsSuccessResponse() {
        UserClient userClient = new UserClient();
        Response successResponse = userClient.getUserCreationMethodResponse("test_" + randomString + "@yandex.ru",
                "1234_" + randomString, "tester_" + randomString);
        successResponse
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check client error response for duplicate user creation")
    @Description("Creating a duplicate user. Checking status code 403 and client error response body")
    public void creatingDuplicateUserReturnsClientError() {
        UserClient userClient = new UserClient();
        userClient.getUserCreationMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString);
        Response clientErrorResponse = userClient.getUserCreationMethodResponse("test_" + randomString + "@yandex.ru",
                        "1234_" + randomString, "tester_" + randomString);
        clientErrorResponse
                .then().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Check client error response for user creation without email")
    @Description("Creating a user without email. Checking status code 403 and client error response body")
    public void creatingNewUserWithoutEmailReturnsClientError() {
        UserClient userClient = new UserClient();
        Response emailClientErrorResponse = userClient.getUserCreationMethodResponse(null,
                        "1234_" + randomString, "tester_" + randomString);
        emailClientErrorResponse
                .then().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check client error response for user creation without password")
    @Description("Creating a user without password. Checking status code 403 and client error response body")
    public void creatingNewUserWithoutPasswordReturnsClientError() {
        UserClient userClient = new UserClient();
        Response passwordClientErrorResponse = userClient.getUserCreationMethodResponse("test_" + randomString + "@yandex.ru",
                        null, "tester_" + randomString);
        passwordClientErrorResponse
                .then().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check client error response for user creation without name")
    @Description("Creating a user without name. Checking status code 403 and client error response body")
    public void creatingNewUserWithoutNameReturnsClientError() {
        UserClient userClient = new UserClient();
        Response nameClientErrorResponse = userClient.getUserCreationMethodResponse("test_" + randomString + "@yandex.ru",
                        "1234_" + randomString, null);
        nameClientErrorResponse
                .then().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}
