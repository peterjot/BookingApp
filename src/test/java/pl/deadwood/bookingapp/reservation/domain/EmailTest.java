package pl.deadwood.bookingapp.reservation.domain;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static pl.deadwood.bookingapp.reservation.domain.ReservationExceptionMatcher.hasMessage;

public class EmailTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();


    @Test
    public void shouldThrowEmailExceptionWhenEmailFormatIsWrong() {
        // then
        expectedException.expect(ReservationException.class);
        expectedException.expect(hasMessage("Wrong email format"));

        // when
        Email.of("dsadsa");
    }

    @Test
    public void shouldNotThrowExceptionWhenEmailFormatIsCorrect() {
        // when
        Email.of("pdsad@wo.pl");
    }
}
