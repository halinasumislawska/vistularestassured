package org.vistula.restassured.information;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.vistula.restassured.RestAssuredTest;

import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

public class InformationControllerTest extends RestAssuredTest {

    @Test
    public void shouldGetAll() {
        given().get("/information")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    public void shouldCreateNewPlayer() {

        JSONObject requestParams = new JSONObject();

        int randomSalary = ThreadLocalRandom.current().nextInt(200, 50000);
        String myName = RandomStringUtils.randomAlphabetic(7);
        String myNationality = "Polish";
        requestParams.put("name", myName);
        requestParams.put("nationality", myNationality);
        requestParams.put("salary", randomSalary); //mam losowe salary, wyżej jest zdefiniowane jako zmienna

        given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/information")
                .then()

                .log().all()
                .statusCode(201)
                .body("nationality", equalTo(myNationality))
                .body("name", equalTo(myName))
                .body("salary", equalTo(randomSalary))
                .body("id", greaterThan(0));// sprawdza, czy wartość jest dodatnia

    }

    @Test

    public void shouldDeletePlayer() {

        given().delete("/information/12")
                .then()
                .log().all()
                .statusCode(204);
    }
}
