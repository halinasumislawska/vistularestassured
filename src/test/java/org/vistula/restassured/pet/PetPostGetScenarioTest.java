package org.vistula.restassured.pet;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import org.vistula.restassured.RestAssuredTest;

import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;


public class PetPostGetScenarioTest extends RestAssuredTest {

    @Test
    public void shouldGetPet() {
        JSONObject requestParams = new JSONObject();
        int value = ThreadLocalRandom.current().nextInt(20, Integer.MAX_VALUE);//randomowy numer
        requestParams.put("id", value); //tu dodajemy id, bo jest put
        String randomName = RandomStringUtils.randomAlphabetic(7);
        requestParams.put("name", randomName);
        /*zamiast imienia jest randomowy ciąg znaków*/


        creatNewPet(requestParams);
        getPet(value, randomName);
        deletePet(value);
    }


    private void deletePet(int value) {
        given().delete("/pet/"+ value)
                .then()
                .log().all()
                .statusCode(204);
    }

    private void getPet(int value, String randomName) {
        given().get("/pet/" + value)//wysyłamy do servera get'a Ten test bierze randomowego wcześnie wygenerowanego peta
                .then()
                .log().all()// tu dostaniemy response
                .statusCode(200) // spodziewamy się odpowiedzi status 200
                .body("id", is(value)) // spodziewamy, że w body będzie id=1
                .body("name", equalTo(randomName));
    }


    private void creatNewPet(JSONObject requestParams) {
        given().header("Content-Type", "application/json")// CTRl+alt+m z tego co wcześniej zaznaczyłam stworzyło nowąmetodę
                //creatNewPet
                .body(requestParams.toString())
                .post("/pet")
                .then()
                .log().all()
                .statusCode(201);
    }
}




