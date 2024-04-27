package tests;

import io.restassured.RestAssured;
import models.lombok.CreateNewUserResponseLombokModel;
import models.lombok.UpdateUserResponseLombokModel;
import models.lombok.UserRequestBodyLombokModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.Specs.*;

public class RequesinStatusSpecTest {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Get user")
    void getSingleUserName() {
        step("make request", () -> {
            given(basicRequestSpec)

                    .when()
                    .get("/users/2")

                    .then()
                    .spec(ResponseCode200Spec)
                    .body("data.first_name", is("Janet"))
                    .body("data.last_name", is("Weaver"));

        });
    }

    @Test
    @DisplayName("Get Single resource 404")
    void getSingleResource() {
        step("make request", () -> {
            given(basicRequestSpec)

                    .when()
                    .get("/unknown/23")

                    .then()
                    .spec(ResponseCode404Spec);
        });
    }


    @Test
    @DisplayName("Create user")
    void createNewUser() {

        UserRequestBodyLombokModel data = new UserRequestBodyLombokModel();
        data.setName("NewUser");
        data.setJob("Janitor");


        CreateNewUserResponseLombokModel response = step("make request", () ->
                given(createUserRequestSpec)

                        .body(data)

                        .when()
                        .post("/users")

                        .then()
                        .spec(ResponseCode201Spec)
                        .extract().as(CreateNewUserResponseLombokModel.class));

        step("check response", () -> {
            assertEquals("NewUser", response.getName());
            assertEquals("Janitor", response.getJob());
            assertNotNull(response.getId());
            assertNotNull(response.getCreatedAt());
        });
    }

    @Test
    @DisplayName("Update user")
    void updateNewUser() {

        UserRequestBodyLombokModel data = new UserRequestBodyLombokModel();
        data.setName("UpdatedNewUser");
        data.setJob("senior janitor");

        UpdateUserResponseLombokModel response = step("make request", () ->
                given(updateUserRequestSpec)
                        .body(data)

                        .when()
                        .put("/users/2")

                        .then()
                        .spec(ResponseCode200Spec)
                        .extract().as(UpdateUserResponseLombokModel.class));

        step("check response", () -> {
            assertEquals("UpdatedNewUser", response.getName());
            assertEquals("senior janitor", response.getJob());
            assertNotNull(response.getUpdatedAt());
        });
    }

    @Test
    @DisplayName("Delete user")
    void deleteUser() {
        step("make request", () -> {
            given(basicRequestSpec)

                    .when()
                    .delete("/users/2")

                    .then()
                    .spec(ResponseCode204Spec);
        });
    }
}
