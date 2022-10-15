import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EditUserTest {
    User user;
    UserClient userClient;
    private String userId;
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
        userId = userClient.login(creds)
                .statusCode(200)
                .extract().path("accessToken");

        user.setEmail(newEmail);
        user.setPassword(newPass);

        UserCredentials creds1 = UserCredentials.from(user);
        boolean isSuccessEdit = userClient.edit(creds1, userId)
                .statusCode(200)
                .extract().path("success");

        userClient.delete(userId);

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
