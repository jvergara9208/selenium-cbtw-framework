package com.cbtw.automation.api.clients;

import com.cbtw.automation.api.models.BookingRequest;
import com.cbtw.automation.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class BookingClient {

    private RequestSpecification baseRequest() {
        RestAssuredConfig config = RestAssuredConfig.config()
                .encoderConfig(EncoderConfig.encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false));

        return RestAssured.given()
                .config(config)
                .baseUri(ConfigReader.get("api.base.url"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    public String generateAuthToken(String username, String password) {
        return baseRequest()
                .body(Map.of("username", username, "password", password))
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    public Response createBooking(BookingRequest payload) {
        return baseRequest()
                .body(payload)
                .when()
                .post("/booking");
    }

    public Response getBookingById(int bookingId) {
        return baseRequest()
                .pathParam("id", bookingId)
                .when()
                .get("/booking/{id}");
    }

    public Response updateBooking(int bookingId, BookingRequest payload, String token) {
        return baseRequest()
                .cookie("token", token)
                .pathParam("id", bookingId)
                .body(payload)
                .when()
                .put("/booking/{id}");
    }

    public Response deleteBooking(int bookingId, String token) {
        return baseRequest()
                .cookie("token", token)
                .pathParam("id", bookingId)
                .when()
                .delete("/booking/{id}");
    }
}