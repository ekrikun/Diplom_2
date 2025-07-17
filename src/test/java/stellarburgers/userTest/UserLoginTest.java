package stellarburgers.userTest;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.user.*;

public class UserLoginTest {
    private UserSteps userSteps;
    private UserResult userResult;
    private UserCreate userCreate;
    private UserLogin userLogin;
    private String accessToken; //  Поле для хранения access токена

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

        ValidatableResponse userLoginResponse = userSteps.userLogin(userLogin); // Сохраняем ответ в userLoginResponse
        userResult.userLoginSuccess(userLoginResponse);

        //  Извлекаем access токен из ответа для удаления пользователя после теста
        accessToken = userSteps.getAccessToken(userLoginResponse);


    }

    @Test
    @DisplayName("Проверяем вход с неверным логином (Email)")
    @Description("Проверяем, что пользователь не может залогиниться с неверным логином (Email)")
    public void userLoginIncorrectEmail() {
        userSteps.userCreate(userCreate);
        userLogin = UserLogin.from(userCreate);
        userLogin.setEmail("1");

        ValidatableResponse loginUser = userSteps.userLogin(userLogin);
        userResult.userLoginIncorrectData(loginUser);

        //  Не удалось залогиниться, поэтому токен не получаем, но нужно удалить созданного пользователя
        ValidatableResponse createResponse = userSteps.userCreate(userCreate); //  Повторно создаем, чтобы получить токен
        accessToken = userSteps.getAccessToken(createResponse);

    }

    @Test
    @DisplayName("Проверяем вход с неверным паролем")
    @Description("Проверяем, что пользователь не может залогиниться с неверным паролем")
    public void userLoginIncorrectPassword() {
        userSteps.userCreate(userCreate);
        userLogin = UserLogin.from(userCreate);
        userLogin.setPassword("1");

        ValidatableResponse loginUser = userSteps.userLogin(userLogin);
        userResult.userLoginIncorrectData(loginUser);

        //  Не удалось залогиниться, поэтому токен не получаем, но нужно удалить созданного пользователя
        ValidatableResponse createResponse = userSteps.userCreate(userCreate); //  Повторно создаем, чтобы получить токен
        accessToken = userSteps.getAccessToken(createResponse);

    }

    @Test
    @DisplayName("Проверяем вход с пустыми данными")
    @Description("Проверяем, что пользователь не может залогиниться не заполняя обязательные поля")
    public void userLoginWithoutData() {
        userSteps.userCreate(userCreate);
        userLogin = UserLogin.from(userCreate);
        userLogin.setEmail(null);
        userLogin.setPassword(null);

        ValidatableResponse loginUser = userSteps.userLogin(userLogin);
        userResult.userLoginIncorrectData(loginUser);

        //  Не удалось залогиниться, поэтому токен не получаем, но нужно удалить созданного пользователя
        ValidatableResponse createResponse = userSteps.userCreate(userCreate); //  Повторно создаем, чтобы получить токен
        accessToken = userSteps.getAccessToken(createResponse);

    }

    @After
    @Step("Удаление пользователя")
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.userDelete(accessToken);
            accessToken = null; // Сбрасываем токен после удаления
        }
    }
}