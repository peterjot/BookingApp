package pl.deadwood.bookingapp.reservation.domain;

import lombok.NonNull;
import lombok.Value;

@Value
public class Surname {

    private static final String SINGLE_SURNAME_REGEXP = "\\p{IsUppercase}\\p{IsLowercase}{2,}";
    private static final String SURNAME_REGEXP = "^" + SINGLE_SURNAME_REGEXP + "(?:-" + SINGLE_SURNAME_REGEXP + ")?$";

    @NonNull
    String value;

    public Surname(@NonNull String value) {
        this.value = getValidName(value);
    }

    private String getValidName(String name) {
        if (!name.trim().matches(SURNAME_REGEXP)) {
            throw new ReservationException("Wrong surname format.");
        }
        return name.trim();
    }
}
