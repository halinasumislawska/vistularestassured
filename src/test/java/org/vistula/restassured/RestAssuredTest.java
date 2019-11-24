package org.vistula.restassured;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
//to jest super klasa do klas testowych do PetControllerTest i InformationConmtrollerTest
public class RestAssuredTest {

    @BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";//tu wpisujemy adres api
        RestAssured.port = 9999;// tu wpisujemy port
    }
}
