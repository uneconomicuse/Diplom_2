import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.junit.Assert.assertTrue;

public class CreateUserPositiveTest {
    User user;
    UserClient userClient;
    private String userId;

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
    @DisplayName("Создание пользователя")
    public void userCreateTest() throws InterruptedException {
       boolean isTrue = userClient.create(user)
                .statusCode(200)
                .extract().path("success");

        UserCredentials creds = UserCredentials.from(user);
        userId = userClient.login(creds)
                .extract().path("accessToken");

        assertTrue(isTrue);
        Thread.sleep(10000);
    }
}
