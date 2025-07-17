package stellarburgers.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import stellarburgers.config.RequestSpec;

import static stellarburgers.config.RequestSpec.requestSpec;
import static stellarburgers.constantsApi.ApiEndPoints.*;

public class UserSteps extends RequestSpec {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api"; // Объявляем константу BASE_URL
    @Step("Регистрация нового пользователя /api/auth/register")
    public ValidatableResponse userCreate(UserCreate userCreate) {
        return requestSpec()
                .body(userCreate)
                .when()
                .post(USER_CREATE_POST)
                .then();
    }

    @Step("Логин пользователя /api/auth/login")
    public ValidatableResponse userLogin(UserLogin userLogin) {
        return requestSpec()
                .body(userLogin)
                .when()
                .post(USER_LOGIN_POST)
                .then();
    }

    @Step("Изменение данных пользователя с авторизацией /api/auth/user")
    public ValidatableResponse userAuthorizationUpdate(UserCreate userCreate, String accessToken) {
        return requestSpec()
                .header("Authorization", accessToken)
                .body(userCreate)
                .when()
                .patch(USER_UPDATES_DATA_PATCH)
                .then();
    }

    @Step("Изменение данных пользователя без авторизации /api/auth/user")
    public ValidatableResponse userWithOutAuthorizationUpdate(UserCreate userCreate) {
        return requestSpec()
                .body(userCreate)
                .when()
                .patch(USER_UPDATES_DATA_PATCH)
                .then();
    }

    @Step("Получение данных конкретного пользователя /api/auth/user")
    public ValidatableResponse userReceiving(UserCreate userCreate, String accessToken) {
        return requestSpec()
                .header("Authorization", accessToken)
                .body(userCreate)
                .when()
                .get(USER_RECEIVING_DATA_GET)
                .then();
    }

    @Step("Выход из системы /api/auth/logout")
    public ValidatableResponse userLogOut(String refreshToken){
        return requestSpec()
                .body(refreshToken)
                .when()
                .post(USER_LOGOUT_POST)
                .then();
    }

    // Метод извлекает токен из ответа создания/логина пользователя
    public String getAccessToken(ValidatableResponse response) {
        String token = response.extract().path("accessToken");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token;
    }

    // Метод для удаления пользователя по accessToken
    public ValidatableResponse userDelete(String accessToken) {
        return requestSpec()
                .header("Authorization", "Bearer " + accessToken) // Ensure "Bearer " prefix
                .when()
                .delete(BASE_URL + "/auth/user") // corrected the URL
                .then()
                .log().ifError();
    }
}