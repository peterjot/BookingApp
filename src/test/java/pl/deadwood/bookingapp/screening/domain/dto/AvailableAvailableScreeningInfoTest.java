package pl.deadwood.bookingapp.screening.domain.dto;

import org.junit.Test;
import pl.deadwood.bookingapp.screening.domain.Movie;

import static pl.deadwood.bookingapp.BookingFixture.anyUuid;
import static pl.deadwood.bookingapp.BookingFixture.maxDuration;

public class AvailableAvailableScreeningInfoTest {

    @Test(expected = NullPointerException.class)
    public void shouldNotCreateMovieDtoWhenAnyParameterIsNull() {
        new AvailableScreening(null, "Title", maxDuration());
        new AvailableScreening(anyUuid(), (Movie) null, maxDuration());
        new AvailableScreening(anyUuid(), "Title", null);
    }

    @Test
    public void shouldCreateMovieDtoWhenAllParametersAreNotNull() {
        new AvailableScreening(anyUuid(), "Title", maxDuration());
    }
}
