package pl.deadwood.bookingapp.screening.domain.dto;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pl.deadwood.bookingapp.BookingFixture.anyUuid;
import static pl.deadwood.bookingapp.screening.domain.dto.AvailableScreening.ORDER_BY_TITLE_AND_START_TIME;

public class AvailableScreeningComparatorTest {

    private final Clock CLOCK = Clock.tickMinutes(ZoneId.of("Europe/Warsaw"));

    @Test
    public void shouldReturnZeroWhenTitlesAndStartTimesEqual() {
        AvailableScreening first = new AvailableScreening(anyUuid(), "Władca Pierścieni: Powrót króla", nowPlusMinutes(1));
        AvailableScreening second = new AvailableScreening(anyUuid(), "Władca Pierścieni: Powrót króla", nowPlusMinutes(1));

        int result = ORDER_BY_TITLE_AND_START_TIME.compare(first, second);
        assertEquals(0, result);
    }

    @Test
    public void shouldReturnPositiveNumberWhenTitleGreaterThan() {
        AvailableScreening first = new AvailableScreening(anyUuid(), "Zdrada Pierścieni: Powrót króla", nowPlusMinutes(1));
        AvailableScreening second = new AvailableScreening(anyUuid(), "Władca Pierścieni: Powrót króla", nowPlusMinutes(1));

        int result = ORDER_BY_TITLE_AND_START_TIME.compare(first, second);
        assertTrue(result > 0);
    }

    @Test
    public void shouldReturnPositiveNumberWhenTitlesEqualAndStartTimeGreaterThan() {
        AvailableScreening first = new AvailableScreening(anyUuid(), "Władca Pierścieni: Powrót króla", nowPlusMinutes(5));
        AvailableScreening second = new AvailableScreening(anyUuid(), "Władca Pierścieni: Powrót króla", nowPlusMinutes(1));

        int result = ORDER_BY_TITLE_AND_START_TIME.compare(first, second);
        assertTrue(result > 0);
    }

    @Test
    public void shouldReturnNegativeNumberWhenTitleLessThan() {
        AvailableScreening first = new AvailableScreening(anyUuid(), "Adrada Pierścieni: Powrót króla", nowPlusMinutes(5));
        AvailableScreening second = new AvailableScreening(anyUuid(), "Władca Pierścieni: Powrót króla", nowPlusMinutes(1));

        int result = ORDER_BY_TITLE_AND_START_TIME.compare(first, second);
        assertTrue(result < 0);
    }

    @Test
    public void shouldReturnNegativeNumberWhenTitlesEqualAndStartTimeLessThan() {
        AvailableScreening first = new AvailableScreening(anyUuid(), "Władca Pierścieni: Powrót króla", nowPlusMinutes(3));
        AvailableScreening second = new AvailableScreening(anyUuid(), "Władca Pierścieni: Powrót króla", nowPlusMinutes(5));

        int result = ORDER_BY_TITLE_AND_START_TIME.compare(first, second);
        assertTrue(result < 0);
    }

    private Instant nowPlusMinutes(long minutes) {
        return Instant.now(CLOCK).plus(minutes, ChronoUnit.MINUTES);
    }
}
