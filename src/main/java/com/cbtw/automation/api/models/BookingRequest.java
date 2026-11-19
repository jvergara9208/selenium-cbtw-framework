package com.cbtw.automation.api.models;

public record BookingRequest(
        String firstname,
        String lastname,
        int totalprice,
        boolean depositpaid,
        BookingDates bookingdates,
        String additionalneeds
) {
    public record BookingDates(String checkin, String checkout) {}
}