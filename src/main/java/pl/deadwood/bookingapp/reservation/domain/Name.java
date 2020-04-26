package pl.deadwood.bookingapp.reservation.domain;

import lombok.NonNull;
import lombok.Value;

@Value
public class Name {

    private static final String NAME_REGEX = "\\p{IsUppercase}\\p{IsLowercase}{2,}";

    @NonNull
    String value;


    public Name(@NonNull String value) {
        this.value = getValidName(value);
    }

    private String getValidName(String name) {
        if (!name.trim().matches(NAME_REGEX)) {
            throw new ReservationException("Wrong name format.");
        }
        return name.trim();
    }
}
