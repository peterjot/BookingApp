package pl.deadwood.bookingapp.screening.domain.dto;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import pl.deadwood.bookingapp.screening.domain.Movie;

import java.time.Instant;
import java.util.Comparator;
import java.util.UUID;

@Value
@AllArgsConstructor
public class AvailableScreening {

    @NonNull
    UUID id;

    @NonNull
    Movie movie;

    @NonNull
    Instant start;


    public AvailableScreening(@NonNull UUID id, String movieName, @NonNull Instant start) {
        this.id = id;
        this.movie = Movie.of(movieName);
        this.start = start;
    }


    public static final Comparator<AvailableScreening> ORDER_BY_TITLE_AND_START_TIME = new TitleAndTimeOrder();

    private static class TitleAndTimeOrder implements Comparator<AvailableScreening> {

        @Override
        public int compare(AvailableScreening o1, AvailableScreening o2) {
            int result = o1.movie.getTitle().compareTo(o2.movie.getTitle());
            if (result != 0) {
                return result;
            }

            return o1.start.compareTo(o2.start);
        }
    }
}
