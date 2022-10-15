package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.json.JSONObject;
import user.BaseClient;


import java.util.List;

import static io.restassured.RestAssured.given;

public class OrdersClient extends BaseClient {
    private final String ROOT = "/orders";
    private final String ALL = ROOT + "/all";
    private final String INGREDIENTS = "/ingredients";

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(getSpec())
                .get(INGREDIENTS);
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(List<String> ingredients, String token) {
        JSONObject requestBodyJson = new JSONObject();
        String requestBody = requestBodyJson
                .put("ingredients", ingredients)
                .toString();
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .body(requestBody)
                .when()
                .post(ROOT)
                .then().log().all()
                .assertThat();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrderList(String userId) {
        return getSpec()
                .header("Authorization", userId)
                .get(ROOT)
                .then().log().all()
                .assertThat();
    }
}
