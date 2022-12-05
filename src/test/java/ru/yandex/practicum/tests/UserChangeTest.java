package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.requests.UserChangeRequestBody;
import ru.yandex.practicum.variables.BaseUri;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserChangeTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUri.BASE_URI;
    }

    public Response getUserChangeMethodResponse(String email, String password, String name,
                                                String newEmail, String newPassword, String newName) {
        UserCreationTest userCreation = new UserCreationTest();
        String accessToken = userCreation
                .getUserCreationMethodResponse(email, password, name)
                .jsonPath().getString("accessToken");

        UserChangeRequestBody requestBody = new UserChangeRequestBody(newEmail, newPassword, newName);
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(requestBody)
                .when()
                .patch("/api/auth/user");
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    @Test
    @Description("Basic test for PATCH /api/auth/user")
    public void changingUserEmailReturnsSuccessResponse() {
        getUserChangeMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "attempt_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString)
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @Description("Basic test for PATCH /api/auth/user")
    public void changingUserPasswordReturnsSuccessResponse() {
        getUserChangeMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "test_" + randomString + "@yandex.ru", "4321_" + randomString, "tester_" + randomString)
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @Description("Basic test for PATCH /api/auth/user")
    public void changingUserNameReturnsSuccessResponse() {
        getUserChangeMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString,
                "test_" + randomString + "@yandex.ru", "1234_" + randomString, "user_" + randomString)
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @Description("Basic test for PATCH /api/auth/user")
    public void changingUserWithoutAuthReturnsClientError() {
        UserChangeRequestBody requestBody = new UserChangeRequestBody("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString);
            given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .patch("/api/auth/user")
                .then().statusCode(401)
                .and()
                .assertThat().body("success", equalTo(false));
    }
}
