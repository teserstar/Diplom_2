package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.requests.UserLoginRequestBody;
import ru.yandex.practicum.variables.BaseUri;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUri.BASE_URI;
    }

    public Response getUserLoginMethodResponse(String email, String password) {
        UserLoginRequestBody requestBody = new UserLoginRequestBody(email, password);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/api/auth/login");
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    @Test
    @Description("Basic test for POST /api/auth/login")
    public void loginUserReturnsSuccessResponse() {
        UserCreationTest userCreation = new UserCreationTest();
        userCreation.getUserCreationMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString);
        getUserLoginMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString)
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @Description("Basic test for POST /api/auth/login")
    public void loginUserWithIncorrectEmailAndPasswordReturnsClientError() {
        UserCreationTest userCreation = new UserCreationTest();
        userCreation.getUserCreationMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString);
        getUserLoginMethodResponse("attempt_" + randomString + "@yandex.ru", "4321_" + randomString)
                .then().statusCode(401)
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
