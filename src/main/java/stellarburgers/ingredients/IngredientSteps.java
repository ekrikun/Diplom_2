package stellarburgers.ingredients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import stellarburgers.config.RequestSpec;

import static stellarburgers.constantsApi.ApiEndPoints.INGREDIENTS_GET;

public class IngredientSteps extends RequestSpec {

    @Step("Получение всего списка ингредиентов /api/ingredients")
    public ValidatableResponse ingredientsGet() {
        return requestSpec()
                .get(INGREDIENTS_GET)
                .then();
    }
}
