package pl.deadwood.bookingapp.screening.domain;

import lombok.NonNull;
import lombok.Value;

@Value
public class Movie {

    @NonNull
    String title;
}
