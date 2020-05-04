package pl.deadwood.bookingapp.reservation.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static pl.deadwood.bookingapp.reservation.domain.ReservationExceptionMatcher.hasMessage;

public class NameTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();


    @Test
    public void shouldNotCreateName_WhenLessThanThreeLetters() {
        // then
        expectedException.expect(ReservationException.class);
        expectedException.expect(hasMessage("Wrong name format"));

        // when
        Name.of("Pi");
    }

    @Test
    public void shouldCreateNameWhenStartingWithCapitalLetter() {
        Name.of("Piotrek");
    }

    @Test
    public void shouldNotCreateNameWhenNotStartingWithCapitalLetter() {
        // then
        expectedException.expect(ReservationException.class);
        expectedException.expect(hasMessage("Wrong name format"));

        // when
        Name.of("piotrek");
    }

    @Test
    public void shouldCreateNameWhenHasMoreThanTwoLetters() {
        // when
        Surname.of("Piotrekkkkkkddddd");
    }
}
