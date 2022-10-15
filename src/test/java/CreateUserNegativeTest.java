import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreateUserNegativeTest {
    User user;
    UserClient userClient;
    private String userId;
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
       userClient.create(user)
                .statusCode(200);

        String newEmail = user.getEmail();
        user.setEmail(newEmail);

        boolean isCreateFalse = userClient.create(user)
                .extract().path("success");

        UserCredentials creds = UserCredentials.from(user);
        userId = userClient.login(creds)
                .statusCode(200)
                .extract().path("accessToken");

        boolean isDeleteTrue = userClient.delete(userId)
                .extract().path("success");

        assertFalse(isCreateFalse);
        assertTrue(isDeleteTrue);
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем Email")
    public void userEmptyEmailCreateTest() {
        user.setEmail(emptyField);

        boolean isFalse = userClient.create(user)
                .statusCode(403)
                .extract().path("success");

        assertFalse(isFalse);
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем Password")
    public void userEmptyPasswordCreateTest() {
        user.setPassword(emptyField);

        boolean isFalse = userClient.create(user)
                .statusCode(403)
                .extract().path("success");

        assertFalse(isFalse);
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем Name")
    public void userEmptyNameCreateTest() {
        user.setPassword(emptyField);

        boolean isFalse = userClient.create(user)
                .statusCode(403)
                .extract().path("success");

        assertFalse(isFalse);
    }
}
