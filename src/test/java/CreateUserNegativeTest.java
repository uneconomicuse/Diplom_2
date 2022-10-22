import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.example.user.User;
import org.example.user.UserClient;

import static org.junit.Assert.*;

public class CreateUserNegativeTest {
    User user;
    UserClient userClient;
    private String token;
    private String emptyField = "";

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }

    @After
    public void teardown() throws InterruptedException {
        Thread.sleep(10000);
    }

    @Test
    @DisplayName("Создание пользователя с используемыми учетными данными")
    public void userDuplicateCreateTest() {
        String token = userClient.create(user)
                .statusCode(200)
                .extract().path("accessToken");

        String newEmail = user.getEmail();
        user.setEmail(newEmail);

        Response response =  userClient.create(user)
                .extract().response();

        String duplicateUserToken = response.path("accessToken");

        if (duplicateUserToken == null) {
            assertEquals(403, response.statusCode());
        } else {
            userClient.delete(duplicateUserToken);
        }

        userClient.delete(token);
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем Email")
    public void userEmptyEmailCreateTest() {
        user.setEmail(emptyField);

        boolean isSuccess = userClient.create(user)
                .statusCode(403)
                .extract().path("success");

        assertFalse(isSuccess);
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем Password")
    public void userEmptyPasswordCreateTest() {
        user.setPassword(emptyField);

        boolean isSuccess = userClient.create(user)
                .statusCode(403)
                .extract().path("success");

        assertFalse(isSuccess);
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем Name")
    public void userEmptyNameCreateTest() {
        user.setPassword(emptyField);

        boolean isSuccess = userClient.create(user)
                .statusCode(403)
                .extract().path("success");

        assertFalse(isSuccess);
    }
}
