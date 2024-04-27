package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import models.lombok.*;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RequestStatusLombokModelTest {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Get user")
    void getSingleUserName() {
        given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()

                .when()
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
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()

                .when()
                .get("/unknown/23")

                .then()
                .log().status()
                .statusCode(404);
    }

    @Test
    @DisplayName("Create user")
    void createNewUser() {

        UserRequestBodyLombokModel data = new UserRequestBodyLombokModel();
        data.setName("NewUser");
        data.setJob("Janitor");


        CreateNewUserResponseLombokModel response = given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()
                .body(data)
                .contentType(JSON)

                .when()
                .post("/users")

                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(CreateNewUserResponseLombokModel.class);

        assertEquals("NewUser", response.getName());
        assertEquals("Janitor", response.getJob());
        assertNotNull(response.getId());
        assertNotNull(response.getCreatedAt());


    }

    @Test
    @DisplayName("Update user")
    void updateNewUser() {

        UserRequestBodyLombokModel data = new UserRequestBodyLombokModel();
        data.setName("UpdatedNewUser");
        data.setJob("senior janitor");

        UpdateUserResponseLombokModel response = given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()
                .body(data)
                .contentType(JSON)

                .when()
                .put("/users/2")

                .then()
                .log().body()
                .statusCode(200)
                .extract().as(UpdateUserResponseLombokModel.class);

        assertEquals("UpdatedNewUser", response.getName());
        assertEquals("senior janitor", response.getJob());
        assertNotNull(response.getUpdatedAt());

    }

    @Test
    @DisplayName("Delete user")
    void deleteUser() {
        given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()

                .when()
                .delete("/users/2")

                .then()
                .log().status()
                .statusCode(204);
    }

}

