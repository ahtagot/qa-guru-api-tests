import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ReqresinStatusTests {
    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    @DisplayName("Get user")
    void getSingleUserName() {
        given()
                .log().uri()
                .get("/users/2")
                .then()
                .log().all()
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"));

    }

    @Test
    @DisplayName("Get Single resource 404")
    void getSingleResource() {
        given()
                .log().uri()
                .get("/unknown/23")
                .then()
                .log().status()
                .statusCode(404);
    }

    @Test
    @DisplayName("Create user")
    void createNewUser() {
        String value = "{\"name\": \"NewUser\",\"job\": \"janitor\"}";

        given()
                .log().body()
                .body(value)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/users")
                .then()
                .log().body()
                .body("name", equalTo("NewUser"))
                .body("job", equalTo("janitor"));
    }

    @Test
    @DisplayName("Update user")
    void updateNewUser() {
        String newValue = "{\"name\": \"UpdatedNewUser\",\"job\": \"senior janitor\"}";

        given()
                .log().body()
                .body(newValue)
                .contentType(JSON)
                .log().uri()
                .when()
                .put("/users/2")
                .then()
                .log().body()
                .body("name", equalTo("UpdatedNewUser"))
                .body("job", equalTo("senior janitor"));
    }

    @Test
    @DisplayName("Delete user")
    void deleteUser() {
        given()
                .log().uri()
                .delete("/users/2")
                .then()
                .log().status()
                .statusCode(204);
    }

}


