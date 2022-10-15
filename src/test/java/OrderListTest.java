import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.OrdersClient;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OrderListTest {
    User user;
    UserClient userClient;
    OrdersClient ordersClient;


    private String userId;

    @Before
    public void setup() throws InterruptedException {
        user = User.getRandomUser();
        userClient = new UserClient();
        ordersClient = new OrdersClient();
        Thread.sleep(10000);
    }

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    public void getOrderList() {
        boolean isTrue = userClient.create(user)
                .statusCode(200)
                .extract().path("success");

        UserCredentials creds = UserCredentials.from(user);
        userId = userClient.login(creds)
                .statusCode(200)
                .extract().path("accessToken");

        assertTrue(isTrue);

        Response responseIngredients = ordersClient.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        boolean response = ordersClient.createOrder(ingredients, userId)
                .statusCode(200)
                .extract().path("success");

        assertTrue(response);

        boolean orderListResponse = ordersClient.getOrderList(userId)
                .statusCode(200)
                .extract().path("success");

        assertTrue(orderListResponse);

        userClient.delete(userId);
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void getOrderListNotAuth() {
        boolean orderListResponse = ordersClient.getOrderList("")
                .statusCode(401)
                .extract().path("success");

        assertFalse(orderListResponse);
    }
}
