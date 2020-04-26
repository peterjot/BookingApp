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
        new Name("Pi");
    }

    @Test
    public void shouldCreateNameWhenStartingWithCapitalLetter() {
        new Name("Piotrek");
    }

    @Test
    public void shouldNotCreateNameWhenNotStartingWithCapitalLetter() {
        // then
        expectedException.expect(ReservationException.class);
        expectedException.expect(hasMessage("Wrong name format"));

        // when
        new Name("piotrek");
    }

    @Test
    public void shouldCreateNameWhenHasMoreThanTwoLetters() {
        // when
        new Surname("Piotrekkkkkkddddd");
    }
}
