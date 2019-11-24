package org.vistula.restassured.zadDomHalinaSum;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.vistula.restassured.RestAssuredTest;

import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class ZadDomPatch1 extends RestAssuredTest {

        @Test
        public void shouldCreateNewPlayer() {

            JSONObject requestParams = new JSONObject();

            int randomSalary = ThreadLocalRandom.current().nextInt(200, 50000);
            String myName = RandomStringUtils.randomAlphabetic(7);
            String myNationality = "Polish";

            requestParams.put("name", myName);
            requestParams.put("nationality", myNationality);
            requestParams.put("salary", randomSalary);

            Object idNowyGracz = given().header("Content-Type", "application/json")
                    .body(requestParams.toString())
                    .post("/information")
                    .then()
                    .log().all()
                    .statusCode(201)
                    .body("nationality", equalTo(myNationality))
                    .body("name", equalTo(myName))
                    .body("salary", equalTo(randomSalary))
                    .body("id", greaterThan(0))
                    .extract().path("id");


            String newName = RandomStringUtils.randomAlphabetic(9); //to jest zmienione
            requestParams.put("name", newName);

            given().header("Content-Type", "application/json")
                    .body(requestParams.toString())
                    .patch("/information/" + idNowyGracz)
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("nationality", equalTo(myNationality))
                    .body("name", equalTo(newName))
                    .body("salary", equalTo(randomSalary))
                    .body("id", greaterThan(0));


            given().delete("/information/" +idNowyGracz)
                    .then()
                    .log().all()
                    .statusCode(204);
        }
    }


