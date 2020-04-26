package pl.deadwood.bookingapp.reservation.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static pl.deadwood.bookingapp.reservation.domain.ReservationExceptionMatcher.hasMessage;

public class SurnameTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void shouldNotCreateSurnameWhenLessThanThreeLetters() {
        // then
        expectedException.expect(ReservationException.class);
        expectedException.expect(hasMessage("Wrong surname format."));

        // when
        new Surname("Ja");
    }

    @Test
    public void shouldCreateSurnameWhenTwoPartsStartingWithCapitalLetter() {
        new Surname("Jasina-Casio");
    }

    @Test
    public void shouldNotCreateSurnameWhenNotStartingWithCapitalLetter() {
        // then
        expectedException.expect(ReservationException.class);
        expectedException.expect(hasMessage("Wrong surname format."));

        // when
        new Surname("jasina-Casio");
    }

    @Test
    public void shouldNotCreateSurnameWhenSecondPartNotStartingWithCapitalLetter() {
        // then
        expectedException.expect(ReservationException.class);
        expectedException.expect(hasMessage("Wrong surname format."));

        // when
        new Surname("Jasina-casio");
    }

    @Test
    public void shouldCreateSurnameWhenGreaterThanTwoLetters() {
        new Surname("Jasinaaaa");
    }
}
