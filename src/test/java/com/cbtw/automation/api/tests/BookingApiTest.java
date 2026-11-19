package com.cbtw.automation.api.tests;

import com.cbtw.automation.api.clients.BookingClient;
import com.cbtw.automation.api.factories.BookingFactory;
import com.cbtw.automation.api.models.BookingRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("API")
public class BookingApiTest {

    private BookingClient bookingClient;

    @BeforeEach
    public void setUpClient() {
        bookingClient = new BookingClient();
    }

    @Test
    @Tag("Smoke")
    @DisplayName("POST & GET - Create a new booking and verify persisted details")
    public void testCreateAndRetrieveBooking() {
        BookingRequest newBooking = BookingFactory.createRandomBooking();

        Response createResponse = bookingClient.createBooking(newBooking);
        int bookingId = createResponse.then()
                .statusCode(200)
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo(newBooking.firstname()))
                .extract()
                .path("bookingid");

        bookingClient.getBookingById(bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo(newBooking.firstname()))
                .body("lastname", equalTo(newBooking.lastname()))
                .body("totalprice", equalTo(newBooking.totalprice()));
    }

    @Test
    @Tag("E2E")
    @DisplayName("Full CRUD Lifecycle - Create, Update, and Delete booking with Auth Token")
    public void testFullBookingCrudFlow() {
        String token = bookingClient.generateAuthToken("admin", "password123");
        assertNotNull(token, "Auth token should not be null");

        BookingRequest initialData = BookingFactory.createRandomBooking();
        int bookingId = bookingClient.createBooking(initialData)
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        BookingRequest updatedData = BookingFactory.createRandomBooking();
        bookingClient.updateBooking(bookingId, updatedData, token)
                .then()
                .statusCode(200)
                .body("firstname", equalTo(updatedData.firstname()));

        bookingClient.deleteBooking(bookingId, token)
                .then()
                .statusCode(201);

        bookingClient.getBookingById(bookingId)
                .then()
                .statusCode(404);
    }
}