package pl.deadwood.bookingapp.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class Name {

    private static final String NAME_REGEX = "\\p{IsUppercase}\\p{IsLowercase}{2,}";

    @NonNull
    String value;


    public static Name of(@NonNull String value) {
        return new Name(getValidName(value));
    }

    private static String getValidName(String name) {
        if (!name.trim().matches(NAME_REGEX)) {
            throw new ReservationException("Wrong name format.");
        }
        return name.trim();
    }
}
