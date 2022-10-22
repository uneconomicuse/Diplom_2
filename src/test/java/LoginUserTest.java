import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserCredentials;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginUserTest {

    User user;
    UserClient userClient;
    private String token;
    private String newEmail = "email212121@coden.com";
    private String newPass = "991991";

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }

    @After
    public void teardown() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void checkLoginUser() throws InterruptedException {
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
        token = userClient.login(creds1)
                .statusCode(200)
                .extract().path("accessToken");

        assertFalse(isFalse);
        Thread.sleep(5000);
    }
}
