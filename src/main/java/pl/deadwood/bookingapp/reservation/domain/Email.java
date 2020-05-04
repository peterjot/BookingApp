package pl.deadwood.bookingapp.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class Email {

    private static final String SIMPLE_EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    String value;

    public static Email of(@NonNull String value) {
        return new Email(getValidEmail(value));
    }

    private static String getValidEmail(String email) {
        if (!email.trim().matches(SIMPLE_EMAIL_REGEX)) {
            throw new ReservationException("Wrong email format");
        }
        return email.trim();
    }

}
