package stellarburgers.userTest;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.user.UserCreate;
import stellarburgers.user.UserRandom;
import stellarburgers.user.UserResult;
import stellarburgers.user.UserSteps;

public class UserTest {
    private UserSteps userSteps;
    private UserResult userResult;
    private ValidatableResponse response;
    private UserCreate userCreate;
    private String accessToken = new String();

    @Before
    @Step("Создание тестовых данных пользователя")
    public void setUp() {
        userSteps = new UserSteps();
        userResult = new UserResult();
        userCreate = UserRandom.userGetRandom();
    }

    @Test
    @DisplayName("Проверяем создание уникального пользователя")
    @Description("Проверяем, что уникальный пользователь создается")
    public void userUniqueCreate() {
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        userResult.userCreateSuccess(validatableResponse);
        String accessToken = validatableResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверяем повторное создание пользователя с теми же данными")
    @Description("Проверяем, что нельзя зарегистрироваться, если пользователь уже существует")
    public void userCreateExistingData() {
        ValidatableResponse userCreateFirst = userSteps.userCreate(userCreate);
        ValidatableResponse userCreateSecond = userSteps.userCreate(userCreate);
        userResult.userCreateExistingData(userCreateSecond);
        String accessToken = userCreateFirst.extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверяем создание пользователя при незаполненном обязательном поле email")
    @Description("Проверяем, что нельзя зарегистрироваться, если пользователь не указал email")
    public void userCreateWithOutEmailError() {
        userCreate.setEmail(null);
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        userResult.userCreateError(validatableResponse);
    }

    @Test
    @DisplayName("Проверяем создание пользователя при незаполненном обязательном поле password")
    @Description("Проверяем, что нельзя зарегистрироваться, если пользователь не указал password")
    public void userCreateWithOutPasswordError() {
        userCreate.setPassword(null);
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        userResult.userCreateError(validatableResponse);
    }

    @Test
    @DisplayName("Проверяем создание пользователя при незаполненном обязательном поле name")
    @Description("Проверяем, что нельзя зарегистрироваться, если пользователь не указал name")
    public void userCreateWithOutNameError() {
        userCreate.setName(null);
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        userResult.userCreateError(validatableResponse);
    }

    @After
    @Step("Удаление пользователя")
    public void userDelete() {
        userSteps.userDelete(StringUtils.substringAfter(accessToken, ""));
    }
}
