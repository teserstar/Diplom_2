package api.client;

import api.model.order.requests.OrderCreationRequestBody;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrdersClient {

    @Step("Get response for order creation with authorization")
    public Response getOrderCreationWithAuthMethodResponse(String userEmail, String userPassword, String userName, String ingredients) {
        UserClient userClient = new UserClient();
        String accessToken = userClient
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

    @Step("Get response for order creation without authorization")
    public Response getOrderCreationWithoutAuthMethodResponse(String ingredients) {
        OrderCreationRequestBody requestBody = new OrderCreationRequestBody(ingredients);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/api/orders");
    }

    @Step("Get response for receiving user's order with authorization")
    public Response getReceivingUserOrderWithAuthMethodResponse(String userEmail, String userPassword, String userName, String ingredients) {
        UserClient userClient = new UserClient();
        String accessToken = userClient
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

    @Step("Get response for receiving user's order without authorization")
    public Response getReceivingUserOrderWithoutAuthMethodResponse() {
        return given()
                .get("/api/orders");
    }
}
