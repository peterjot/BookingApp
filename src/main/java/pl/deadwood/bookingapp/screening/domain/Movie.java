package pl.deadwood.bookingapp.screening.domain;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class Movie {

    @NonNull
    String title;
}
