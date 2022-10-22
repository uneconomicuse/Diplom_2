import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserCredentials;

import static org.junit.Assert.assertTrue;

public class CreateUserPositiveTest {
    User user;
    UserClient userClient;
    private String token;

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
    @DisplayName("Создание пользователя")
    public void userCreateTest() throws InterruptedException {
       boolean isTrue = userClient.create(user)
                .statusCode(200)
                .extract().path("success");

        UserCredentials creds = UserCredentials.from(user);
        token = userClient.login(creds)
                .extract().path("accessToken");

        assertTrue(isTrue);
        Thread.sleep(10000);
    }
}
