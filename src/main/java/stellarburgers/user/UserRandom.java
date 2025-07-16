package stellarburgers.user;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.qameta.allure.Allure;
import io.restassured.response.ValidatableResponse;

import java.util.Locale;

public class UserRandom {
    public static UserCreate userGetRandom(){
        Faker faker = new Faker();
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        String email = fakeValuesService.bothify("????###@gmail.com");
        String password = fakeValuesService.bothify("?.??##");
        String name = faker.name().firstName();

        Allure.addAttachment("Email : ", email);
        Allure.addAttachment("Password : ", password);
        Allure.addAttachment("Name : ", name);

        return new UserCreate(email,password,name);
    }

    public static String userGetAccessToken(){
        UserCreate userCreate = UserRandom.userGetRandom();
        UserSteps userSteps = new UserSteps();
        ValidatableResponse response = userSteps.userCreate(userCreate);
        String accessToken = response.extract().path("accessToken");
        return accessToken;
    }
}