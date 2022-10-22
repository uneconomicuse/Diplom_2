package org.example.user;

import io.qameta.allure.Step;

public class UserCredentials {
    private String email;
    private String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Step("Авторизоваться в системе")
    public static UserCredentials from(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }
}
