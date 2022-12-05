package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.requests.UserCreationRequestBody;
import ru.yandex.practicum.variables.BaseUri;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUri.BASE_URI;
    }

    public Response getUserCreationMethodResponse(String email, String password, String name) {
        UserCreationRequestBody requestBody = new UserCreationRequestBody(email, password, name);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/api/auth/register");
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    @Test
    @Description("Basic test for POST /api/auth/register")
    public void creatingNewUserReturnsSuccessResponse() {
        getUserCreationMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString)
                .then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @Description("Basic test for POST /api/auth/register")
    public void creatingDuplicateUserReturnsClientError() {
        getUserCreationMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString);
        getUserCreationMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, "tester_" + randomString)
                .then().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @Description("Basic test for POST /api/auth/register")
    public void creatingNewUserWithoutEmailReturnsClientError() {
        getUserCreationMethodResponse(null, "1234_" + randomString, "tester_" + randomString)
                .then().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Description("Basic test for POST /api/auth/register")
    public void creatingNewUserWithoutPasswordReturnsClientError() {
        getUserCreationMethodResponse("test_" + randomString + "@yandex.ru", null, "tester_" + randomString)
                .then().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Description("Basic test for POST /api/auth/register")
    public void creatingNewUserWithoutNameReturnsClientError() {
        getUserCreationMethodResponse("test_" + randomString + "@yandex.ru", "1234_" + randomString, null)
                .then().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}
