import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginUserTest {

    User user;
    UserClient userClient;
    private String userId;
    private String newEmail = "email212121@coden.com";
    private String newPass = "991991";

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }

    @After
    public void teardown() {
        userClient.delete(userId);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void checkLoginUser() throws InterruptedException {
        boolean isTrue = userClient.create(user)
                .statusCode(200)
                .extract().path("success");

        Thread.sleep(5000);
        UserCredentials creds = UserCredentials.from(user);
        userId = userClient.login(creds)
                .statusCode(200)
                .extract().path("accessToken");

        assertTrue(isTrue);
        Thread.sleep(5000);
    }

    @Test
    @DisplayName("Логин под неверным логином пользователя")
    public void checkInvalidLoginUser() throws InterruptedException {
        userClient.create(user)
                .statusCode(200);

        String oldEmail = user.getEmail();

        user.setEmail(newEmail);
        UserCredentials creds = UserCredentials.from(user);
        boolean isFalse = userClient.login(creds)
                .statusCode(401)
                .extract().path("success");

        Thread.sleep(5000);
        user.setEmail(oldEmail);
        UserCredentials creds1 = UserCredentials.from(user);
        userId = userClient.login(creds1)
                .statusCode(200)
                .extract().path("accessToken");

        assertFalse(isFalse);
        Thread.sleep(5000);
    }
}
