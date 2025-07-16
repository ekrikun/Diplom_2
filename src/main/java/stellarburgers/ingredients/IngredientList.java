package stellarburgers.ingredients;


import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.List;

public class IngredientList {
    public static final String invalidKey = "11111111111111";
    private static IngredientSteps ingredientSteps;

    public static List<String> ingredients(){
        List<String> ingredients = new ArrayList<>();
        ingredients.add(IngredientList.ingredientsList(1));
        ingredients.add(IngredientList.ingredientsList(2));
        return ingredients;
    }

    public static String ingredientsList(int index) {
        ingredientSteps = new IngredientSteps();
        ValidatableResponse response = ingredientSteps.ingredientsGet();
        String ingredient = response.extract().path(String.format("data._id[%s]", index));
        return ingredient;
    }
}
