package org.vistula.restassured.pet;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.vistula.restassured.RestAssuredTest;

import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class PetControllerTest extends RestAssuredTest {

    @Test
    public void shouldGetAll() {
        given().get("/pet")//tu wystarczy pet bo reszta jest w restAssureTest
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @Test
    public void shouldGetFirstPet() {
        given().get("/pet/1")//wysyłamy do servera get'a Ten test zakłada, że istnieje zasób pet 1
                .then()
                .log().all()// tu dostaniemy response
                .statusCode(200) // spodziewamy się odpowiedzi status 200
                .body("id", is(1)) // spodziewamy, że w body będzie id=1
                .body("name", equalTo("Cow")); // spodziewamy się napisu Cow, ten test jest zależny od danych w bazie
    }

    @Test
    public void shouldGetSecondPet() {
        Object name = given().get("/pet/2")//wysyłamy do servera get'a Ten test zakłada, że istnieje zasób pet 1
                /*aby to co wyżej dostać, zaznaczyłam całość od .given do do konca testu i klikamy ctrl+alt+v*/

                .then()
                .log().all()// tu dostaniemy response
                .statusCode(200) // spodziewamy się odpowiedzi status 200
                .body("id", is(2)) // spodziewamy, że w body będzie id=1
                .body("name", equalTo("Dog")) // spodziewamy się napisu Dog
                .extract().path("name");

        assertThat(name).isEqualTo("Dog");
    }

    @Test
    public void shouldGetSecondPet2() {
        Pet pet = given().get("/pet/2")
                .then()
                .log().all()// tu dostaniemy response
                .statusCode(200) // spodziewamy się odpowiedzi status 200
                .body("id", is(2)) // spodziewamy, że w body będzie id=1
                .body("name", equalTo("Dog")) // spodziewamy się napisu Dog
                .extract().body().as(Pet.class);

        assertThat(pet.getId()).isEqualTo(2);
        assertThat(pet.getName()).isEqualTo("Dog");
    }

    @Test
    public void shouldCreateNewPet() {//ten test testuje post i delete w jednym scenariuszu

        JSONObject requestParams = new JSONObject();
        int value = ThreadLocalRandom.current().nextInt(20, Integer.MAX_VALUE);//randomowy numer
        requestParams.put("id", value); //tu dodajemy id, bo jest put
        requestParams.put("name", RandomStringUtils.randomAlphabetic(7)); //tu dodajemy name, bo jest put
        /*zamiast imienia jest randomowy ciąg znaków*/

        given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/pet")
                .then()
                .log().all()
                .statusCode(201);

        given().delete("/pet/"+value)
                .then()
                .log().all()
                .statusCode(204);

    }

    @Test
    public void shouldDeletePet() {

     given().delete("/pet/4")
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    public void shouldGetNoPet100() {//test sprawdzający, że nie ma pet 100
        given().get("/pet/100")// po given definiujemy request
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    public void shouldGetNoPet100PlusBody() {//sprawdzam czy jest ten tekst
        int statusCode = given().get("/pet/100")
                /*tu się pojawił int,  bo zaznaczyłam od .given do końca
                testu, ale bez ; i kliknęłam alt+ctrl+v i linijka .given().get
                zmieniła się na taką jaka jest tutaj*/

                .then()
                .log().all()
                .statusCode(404)
                .body(equalTo("There is not Pet with such id"))
                .extract().statusCode();/*pozwala nam wyciągnąć coś z odpowiedzi do dalszej części testu
                tu nie musi byś statusCode ale np contentType*/

        assertThat(statusCode).isEqualTo(404); //tą metodę trzeba było zaimportować
    }

}
