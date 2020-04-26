package pl.deadwood.bookingapp.reservation.domain;

import lombok.NonNull;
import lombok.Value;

@Value
public class Email {

    private static final String SIMPLE_EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    String value;

    public Email(@NonNull String value) {
        this.value = getValidEmail(value);
    }

    private String getValidEmail(String email) {
        if (!email.trim().matches(SIMPLE_EMAIL_REGEX)) {
            throw new ReservationException("Wrong email format");
        }
        return email.trim();
    }

}
