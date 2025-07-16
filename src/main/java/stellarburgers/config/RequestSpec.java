package stellarburgers.config;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import stellarburgers.constantsApi.ApiEndPoints;

import static io.restassured.RestAssured.given;

public class RequestSpec {
    public static RequestSpecification requestSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(ApiEndPoints.BASE_URL);
    }
}
