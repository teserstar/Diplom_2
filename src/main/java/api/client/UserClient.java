package api.client;

import api.model.user.requests.UserChangeRequestBody;
import api.model.user.requests.UserCreationRequestBody;
import api.model.user.requests.UserLoginRequestBody;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    @Step("Get response for user creation")
    public Response getUserCreationMethodResponse(String email, String password, String name) {
        UserCreationRequestBody requestBody = new UserCreationRequestBody(email, password, name);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/api/auth/register");
    }

    @Step("Get response for user login")
    public Response getUserLoginMethodResponse(String email, String password) {
        UserLoginRequestBody requestBody = new UserLoginRequestBody(email, password);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/api/auth/login");
    }

    @Step("Get response for change authorized user")
    public Response getAuthUserChangeMethodResponse(String email, String password, String name,
                                                String newEmail, String newPassword, String newName) {
        String accessToken = getUserCreationMethodResponse(email, password, name)
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

    @Step("Get response for change unauthorized user")
    public Response getNoAuthUserChangeMethodResponse(String newEmail, String newPassword, String newName) {
        UserChangeRequestBody requestBody = new UserChangeRequestBody(newEmail, newPassword, newName);
        return  given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .patch("/api/auth/user");
    }
}
