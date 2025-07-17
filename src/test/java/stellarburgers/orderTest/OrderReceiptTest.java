package stellarburgers.orderTest;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.order.OrderResult;
import stellarburgers.order.OrderSteps;
import stellarburgers.user.UserRandom;
import stellarburgers.user.UserSteps;

import static org.junit.Assert.assertEquals;

public class OrderReceiptTest {
    private OrderSteps orderSteps;
    private OrderResult orderResult;
    private UserSteps userSteps;
    private ValidatableResponse response;

    @Before
    @Step("Создание тестовых данных пользователя")
    public void setUp() {
        orderSteps = new OrderSteps();
        userSteps = new UserSteps();
        orderResult = new OrderResult();

    }

    @Test
    @DisplayName("Проверяем получение заказа без авторизации")
    @Description("Проверяем, что заказ нельзя получить без авторизации")
    public void orderReceiptWithOutAuthorization() {
        ValidatableResponse validatableResponse = orderSteps.orderReceiptWithOutAuthorization();
        orderResult.orderReceiptWithOutAuthorization(validatableResponse);
    }

    @Test
    @DisplayName("Проверяем получение заказа c авторизацией")
    @Description("Проверяем, что авторизированный пользователь может получить заказ")
    public void orderReceiptWithAuthorization() {
        String accessToken = UserRandom.userGetAccessToken();

        ValidatableResponse orderNumber = orderSteps.orderCreateWithAuthorization(accessToken);
        int orderNumberExpected = orderNumber.extract().path("order.number");
        ValidatableResponse orderReceiptWithAuthorization = orderSteps.orderReceiptWithAuthorization(accessToken);
        int orderNumberActual = orderReceiptWithAuthorization.extract().path("orders[0].number");

        orderResult.orderReceiptWithAuthorization(orderReceiptWithAuthorization);
        assertEquals(orderNumberExpected, orderNumberActual);

        response = userSteps.userDelete(StringUtils.substringAfter(accessToken, ""));
    }
}

