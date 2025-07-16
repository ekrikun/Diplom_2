package stellarburgers.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

public class UserResult {

    @Step("Регистрация нового пользователя с валидными данными")
    public void userCreateSuccess(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Step("Регистрация пользователя с уже существующими данными")
    public void userCreateExistingData(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

    @Step("Регистрация пользователя без заполнения обязательных полей")
    public void userCreateError(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Логин под существующим пользователем")
    public void userLoginSuccess(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Step("Логин или пароль неверные или нет одного из полей")
    public void userLoginIncorrectData(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Изменение данных пользователя: с авторизацией")
    public void userUpdateAuthorization(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Step("Изменение данных пользователя: без авторизации")
    public void userUpdateWithOutAuthorization(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("You should be authorised"));
    }
}

