package org.acme.counter;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class CounterResourceTest {

    @BeforeEach
    void reset() {
        given().when().post("/api/counter/reset").then().statusCode(200);
    }

    @Test
    void testGetReturnsInteger() {
        given()
            .when().get("/api/counter")
            .then()
                .statusCode(200)
                .body(is("0"));
    }

    @Test
    void testIncrement() {
        given()
            .when().post("/api/counter/increment")
            .then()
                .statusCode(200)
                .body(is("1"));
    }

    @Test
    void testDecrement() {
        given()
            .when().post("/api/counter/decrement")
            .then()
                .statusCode(200)
                .body(is("-1"));
    }

    @Test
    void testReset() {
        given().when().post("/api/counter/increment");
        given().when().post("/api/counter/increment");

        given()
            .when().post("/api/counter/reset")
            .then()
                .statusCode(200)
                .body(is("0"));
    }
}
