package stellarburgers.orderTest;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.order.OrderResult;
import stellarburgers.order.OrderSteps;
import stellarburgers.user.UserRandom;
import stellarburgers.user.UserSteps;

public class OrderCreateTest {
    private OrderSteps orderSteps;
    private OrderResult orderResult;
    private UserSteps userSteps;

    @Before
    @Step("Создание тестовых данных пользователя")
    public void setUp() {
        orderSteps = new OrderSteps();
        userSteps = new UserSteps();
        orderResult = new OrderResult();
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверяем, что заказ можно создать")
    public void orderCreateWithOutAuthorization() {
        ValidatableResponse validatableResponse = orderSteps.orderCreateWithOutAuthorization();
        orderResult.orderCreateSuccess(validatableResponse);
    }

    @Test
    @DisplayName("Создание заказа c авторизацией и с ингредиентами")
    @Description("Проверяем, что заказ можно создать")
    public void orderCreateWithAuthorization() {
        String accessToken = UserRandom.userGetAccessToken();

        ValidatableResponse validatableResponse = orderSteps.orderCreateWithAuthorization(accessToken);
        orderResult.orderCreateSuccess(validatableResponse);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    @Description("Проверяем, что заказ нельзя создать, вернётся ошибка 400")
    public void orderCreateWithOutIngredient() {
        ValidatableResponse validatableResponse = orderSteps.orderCreateWithOutIngredients();
        orderResult.orderCreateWithOutIngredients(validatableResponse);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с неверным хэшом ингредиента")
    @Description("Проверяем, что заказ нельзя создать, вернётся ошибка 500")
    public void orderCreateInvalidHashIngredients() {
        ValidatableResponse validatableResponse = orderSteps.orderCreateWithOutAuthorizationInvalidHash();
        orderResult.orderCreateInvalidHashIngredients(validatableResponse);
    }
}
