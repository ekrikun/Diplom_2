package stellarburgers.userTest;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
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

import java.util.Locale;

import static org.junit.Assert.assertNotEquals;

public class UserEditeDataTest {
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
    @DisplayName("Проверяем изменение данных пользователем без авторизации (Email)")
    @Description("Изменение данных пользователя: без авторизации, должно выдавать ошибку")
    public void userWithOutAuthorizationUpdateEmail() {
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        String accessToken = validatableResponse.extract().path("accessToken");
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        userCreate.setEmail(fakeValuesService.bothify("????###@gmail.com"));

        ValidatableResponse userWithOutAuthorizationUpdateEmail = userSteps.userWithOutAuthorizationUpdate(userCreate);
        userResult.userUpdateWithOutAuthorization(userWithOutAuthorizationUpdateEmail);
    }

    @Test
    @DisplayName("Проверяем изменение данных пользователем без авторизации (Password)")
    @Description("Изменение данных пользователя: без авторизации, должно выдавать ошибку")
    public void userWithOutAuthorizationUpdatePassword() {
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        String accessToken = validatableResponse.extract().path("accessToken");
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        userCreate.setPassword(fakeValuesService.bothify("????###"));

        ValidatableResponse userWithOutAuthorizationUpdatePassword = userSteps.userWithOutAuthorizationUpdate(userCreate);
        userResult.userUpdateWithOutAuthorization(userWithOutAuthorizationUpdatePassword);
    }

    @Test
    @DisplayName("Проверяем изменение данных пользователем без авторизации (Name)")
    @Description("Изменение данных пользователя: без авторизации, должно выдавать ошибку")
    public void userWithOutAuthorizationUpdateName() {
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        String accessToken = validatableResponse.extract().path("accessToken");
        Faker faker = new Faker();
        userCreate.setName(faker.name().firstName());

        ValidatableResponse userWithOutAuthorizationUpdateName = userSteps.userWithOutAuthorizationUpdate(userCreate);
        userResult.userUpdateWithOutAuthorization(userWithOutAuthorizationUpdateName);

    }

    @Test
    @DisplayName("Проверяем изменение данных пользователем с авторизацией (Email)")
    @Description("Изменение email пользователя: с авторизацией")
    public void userAuthorizationUpdateEmail() {
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        String accessToken = validatableResponse.extract().path("accessToken");
        String emailExpected = validatableResponse.extract().path("user.email");
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        userCreate.setEmail(fakeValuesService.bothify("????###@gmail.com"));
        ValidatableResponse userAuthorizationUpdateEmail = userSteps.userAuthorizationUpdate(userCreate, accessToken);
        String emailActual = userAuthorizationUpdateEmail.extract().path("user.email");

        userResult.userUpdateAuthorization(userAuthorizationUpdateEmail);
        assertNotEquals("Email не изменен", emailExpected, emailActual);
    }

    @Test
    @DisplayName("Проверяем изменение данных пользователем с авторизацией (Password)")
    @Description("Изменение passwor пользователя: с авторизацией")
    public void userAuthorizationUpdatePassword() {
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        String accessToken = validatableResponse.extract().path("accessToken");
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        userCreate.setPassword(fakeValuesService.bothify("????###"));

        ValidatableResponse userAuthorizationUpdatePassword = userSteps.userAuthorizationUpdate(userCreate, accessToken);
        userResult.userUpdateAuthorization(userAuthorizationUpdatePassword);
    }

    @Test
    @DisplayName("Проверяем изменение данных пользователем с авторизацией (Email)")
    @Description("Изменение email пользователя: с авторизацией")
    public void userAuthorizationUpdateName() {
        ValidatableResponse validatableResponse = userSteps.userCreate(userCreate);
        String accessToken = validatableResponse.extract().path("accessToken");
        String nameExpected = validatableResponse.extract().path("user.name");
        Faker faker = new Faker();
        userCreate.setName(faker.name().firstName());
        ValidatableResponse userAuthorizationUpdateName = userSteps.userAuthorizationUpdate(userCreate, accessToken);
        String nameActual = userAuthorizationUpdateName.extract().path("user.name");

        userResult.userUpdateAuthorization(userAuthorizationUpdateName);
        assertNotEquals("Name не изменен", nameExpected, nameActual);
    }

    @After
    @Step("Удаление пользователя")
    public void userDelete() {
        userSteps.userDelete(StringUtils.substringAfter(accessToken, ""));
    }
}
