package com.cbtw.automation.api.factories;

import com.cbtw.automation.api.models.BookingRequest;
import net.datafaker.Faker;

public class BookingFactory {
    private static final Faker faker = new Faker();

    public static BookingRequest createRandomBooking() {
        return new BookingRequest(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.number().numberBetween(100, 1500),
                true,
                new BookingRequest.BookingDates("2026-12-01", "2026-12-10"),
                "Breakfast"
        );
    }
}