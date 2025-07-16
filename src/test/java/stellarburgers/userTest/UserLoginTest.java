package stellarburgers.userTest;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.user.*;

public class UserLoginTest {
    private UserSteps userSteps;
    private UserResult userResult;
    private ValidatableResponse response;
    private UserCreate userCreate;
    private UserLogin userLogin;
    private String accessToken = new String();

    @Before
    @Step("Создание тестовых данных пользователя")
    public void setUp() {
        userSteps = new UserSteps();
        userResult = new UserResult();
        userCreate = UserRandom.userGetRandom();
    }

    @Test
    @DisplayName("Проверяем вход под существующим пользователем")
    @Description("Проверяем, что существующий пользователь может залогиниться")
    public void userLogin() {
        userSteps.userCreate(userCreate);
        userLogin = UserLogin.from(userCreate);

        ValidatableResponse validatableResponse = userSteps.userLogin(userLogin);
        userResult.userLoginSuccess(validatableResponse);

        String accessToken = validatableResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверяем вход с неверным логином (Email)")
    @Description("Проверяем, что пользователь не может залогиниться с неверным логином (Email)")
    public void userLoginIncorrectEmail() {
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        userLogin = UserLogin.from(userCreate);
        userLogin.setEmail("1");

        ValidatableResponse loginUser = userSteps.userLogin(userLogin);
        userResult.userLoginIncorrectData(loginUser);

        String accessToken = validatableResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверяем вход с неверным паролем")
    @Description("Проверяем, что пользователь не может залогиниться с неверным паролем")
    public void userLoginIncorrectPassword() {
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        userLogin = UserLogin.from(userCreate);
        userLogin.setPassword("1");

        ValidatableResponse loginUser = userSteps.userLogin(userLogin);
        userResult.userLoginIncorrectData(loginUser);

        String accessToken = validatableResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверяем вход с пустыми данными")
    @Description("Проверяем, что пользователь не может залогиниться не заполняя обязательные поля")
    public void userLoginWithoutData() {
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        userLogin = UserLogin.from(userCreate);
        userLogin.setEmail(null);
        userLogin.setPassword(null);

        ValidatableResponse loginUser = userSteps.userLogin(userLogin);
        userResult.userLoginIncorrectData(loginUser);

        String accessToken = validatableResponse.extract().path("accessToken");
    }

    @After
    @Step("Удаление пользователя")
    public void userDelete() {
        userSteps.userDelete(StringUtils.substringAfter(accessToken, ""));
    }
}
