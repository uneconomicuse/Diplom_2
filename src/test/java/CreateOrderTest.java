import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.example.order.OrdersClient;
import org.junit.Before;
import org.junit.Test;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserCredentials;


import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.junit.Assert.*;

public class CreateOrderTest {
    User user;
    UserClient userClient;
    OrdersClient ordersClient;


    private String token;

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
        ordersClient = new OrdersClient();
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void checkCreateOrderAuth() throws InterruptedException {
        boolean isTrue = userClient.create(user)
                .statusCode(200)
                .extract().path("success");

        Thread.sleep(5000);
        UserCredentials creds = UserCredentials.from(user);
        token = userClient.login(creds)
                .statusCode(200)
                .extract().path("accessToken");

        assertTrue(isTrue);

        Thread.sleep(5000);
        Response responseIngredients = ordersClient.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        boolean response = ordersClient.createOrder(ingredients, token)
                .statusCode(200)
                .extract().path("success");

        assertTrue(response);

        Thread.sleep(10000);

        userClient.delete(token);
    }

    @Test
    @DisplayName("Создание заказа не авторизованным пользователем")
    public void checkCreateOrderNotAuth() throws InterruptedException {
        Response responseIngredients = ordersClient.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        boolean response = ordersClient.createOrder(ingredients, "")
                .statusCode(200)
                .extract().path("success");

        Thread.sleep(5000);
        assertTrue(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов авторизованным пользователем")
    public void checkCreateOrderAuthWithoutIngredients() throws InterruptedException {
        boolean isTrue = userClient.create(user)
                .statusCode(200)
                .extract().path("success");

        Thread.sleep(5000);
        UserCredentials creds = UserCredentials.from(user);
        token = userClient.login(creds)
                .statusCode(200)
                .extract().path("accessToken");

        Thread.sleep(5000);
        assertTrue(isTrue);

        boolean response = ordersClient.createOrder(null, token)
                .statusCode(400)
                .extract().path("success");

        assertFalse(response);

        Thread.sleep(5000);

        userClient.delete(token);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов не авторизованным пользователем")
    public void checkCreateOrderNotAuthWithoutIngredients() throws InterruptedException {
        boolean response = ordersClient.createOrder(null, "")
                .statusCode(400)
                .extract().path("success");

        assertFalse(response);

        Thread.sleep(5000);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов авторизированным пользователем")
    public void checkCreateOrderAuthInvalidHashId() throws InterruptedException {
        List<String> test = new ArrayList<>();
        test.add("1");
        test.add("2");

        boolean isTrue = userClient.create(user)
                .statusCode(200)
                .extract().path("success");

        Thread.sleep(5000);
        UserCredentials creds = UserCredentials.from(user);
        token = userClient.login(creds)
                .statusCode(200)
                .extract().path("accessToken");

        assertTrue(isTrue);

        Thread.sleep(5000);
        ValidatableResponse response = ordersClient.createOrder(test, token);
        int statusCode = response.extract().statusCode();

        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);

        Thread.sleep(5000);

        userClient.delete(token);
    }
}
