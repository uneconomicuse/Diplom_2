import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserCredentials;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EditUserTest {
    User user;
    UserClient userClient;
    private String token;
    private String newEmail = "expertozium@jojo.vom";
    private String newPass = "990099";

    @Before
    public void setup() throws InterruptedException {
        user = User.getRandomUser();
        userClient = new UserClient();
        Thread.sleep(10000);
    }

    @Test
    @DisplayName("Редактирование данных пользователя после авторизации")
    public void checkEditUserInfo() {
        boolean isTrue = userClient.create(user)
                .statusCode(200)
                .extract().path("success");

        UserCredentials creds = UserCredentials.from(user);
        token = userClient.login(creds)
                .statusCode(200)
                .extract().path("accessToken");

        user.setEmail(newEmail);
        user.setPassword(newPass);

        UserCredentials creds1 = UserCredentials.from(user);
        boolean isSuccessEdit = userClient.edit(creds1, token)
                .statusCode(200)
                .extract().path("success");

        userClient.delete(token);

        assertTrue(isTrue);
        assertTrue(isSuccessEdit);
    }

    @Test
    @DisplayName("Редактирование данных пользователя без авторизации")
    public void checkEditUserInfoNotAuth() {
        boolean isSuccessEdit = userClient.editWithoutAuth()
                .statusCode(401)
                .extract().path("success");

        assertFalse(isSuccessEdit);
    }
}
