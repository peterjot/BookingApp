package pl.deadwood.bookingapp.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class Surname {

    private static final String SINGLE_SURNAME_REGEXP = "\\p{IsUppercase}\\p{IsLowercase}{2,}";
    private static final String SURNAME_REGEXP = "^" + SINGLE_SURNAME_REGEXP + "(?:-" + SINGLE_SURNAME_REGEXP + ")?$";

    @NonNull
    String value;

    public static Surname of(@NonNull String value) {
        return new Surname(getValidName(value));
    }

    private static String getValidName(String name) {
        if (!name.trim().matches(SURNAME_REGEXP)) {
            throw new ReservationException("Wrong surname format.");
        }
        return name.trim();
    }
}
