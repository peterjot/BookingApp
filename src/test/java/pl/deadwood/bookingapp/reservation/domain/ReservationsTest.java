package pl.deadwood.bookingapp.reservation.domain;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.deadwood.bookingapp.reservation.domain.dto.CreatedReservation;
import pl.deadwood.bookingapp.reservation.domain.dto.ReservationCommand;
import pl.deadwood.bookingapp.screening.domain.Screenings;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.deadwood.bookingapp.BookingFixture.anyUuid;
import static pl.deadwood.bookingapp.BookingFixture.email;
import static pl.deadwood.bookingapp.BookingFixture.name;
import static pl.deadwood.bookingapp.BookingFixture.reservationCommand;
import static pl.deadwood.bookingapp.BookingFixture.screening;
import static pl.deadwood.bookingapp.BookingFixture.surname;
import static pl.deadwood.bookingapp.reservation.domain.ReservationExceptionMatcher.hasMessage;

@RunWith(MockitoJUnitRunner.class)
public class ReservationsTest {

    private static final Clock CLOCK = Clock.system(ZoneId.of("Europe/Warsaw"));

    @Mock
    private Screenings screenings;

    @Mock
    private ReservationRepository reservationRepository;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private Reservations reservations;

    @Mock
    private EventPublisher eventPublisher;


    @Before
    public void setUp() {
        reservations = new Reservations(CLOCK, screenings, reservationRepository, eventPublisher);
    }

    @Test
    public void shouldThrowExceptionWhenReservationTimeHasLessThan15MinutesBeforeTheScreening() {
        // given
        Instant screeningStart = minutesEarlier(14);
        UUID screeningUuid = anyUuid();
        when(screenings.find(screeningUuid)).thenReturn(screening(screeningUuid, screeningStart));

        // then
        expectedException.expect(ReservationException.class);
        expectedException.expect(hasMessage("Seats can be booked at latest [15] minutes before the screening begins"));

        // when
        reservations.book(reservationCommand(screeningUuid));
    }

    @Test
    public void shouldNotThrowExceptionWhenReservationTimeHasMoreEqualThan15MinutesBeforeScreeningStart() {
        // given
        UUID screeningUuid = anyUuid();
        ReservationCommand reservationCommand = reservationCommand(screeningUuid);

        NewReservation expectedNewReservation = NewReservation.of(reservationCommand, minutesLater(15));

        when(screenings.find(screeningUuid)).thenReturn(screening(screeningUuid, minutesLater(16)));
        when(reservationRepository.save(expectedNewReservation)).thenReturn(Reservation.of(expectedNewReservation));

        // when
        reservations.book(reservationCommand);
    }

    @Test
    public void shouldPublishEventWhenReservationTimeHasMoreEqualThan15MinutesBeforeScreeningStart() {
        // given
        UUID screeningUuid = anyUuid();
        ReservationCommand reservationCommand = reservationCommand(screeningUuid);

        NewReservation expectedNewReservation = NewReservation.of(reservationCommand, minutesLater(15));

        when(screenings.find(screeningUuid)).thenReturn(screening(screeningUuid, minutesLater(16)));
        when(reservationRepository.save(expectedNewReservation)).thenReturn(Reservation.of(expectedNewReservation));

        // when
        CreatedReservation createdReservation = reservations.book(reservationCommand);

        verify(eventPublisher, times(1)).publish(
                new OnReservationCreatedEvent(createdReservation.getReservationId(), reservationCommand.getEmail()));

    }

    @Test
    public void shouldCalculateCorrectAmountToPay() {
        // given
        UUID screeningUuid = anyUuid();
        ReservationCommand reservationCommand = reservationCommand(screeningUuid);

        var expectedNewReservation = NewReservation.of(reservationCommand, minutesLater(15));

        when(screenings.find(screeningUuid)).thenReturn(screening(screeningUuid, minutesLater(16)));
        when(reservationRepository.save(expectedNewReservation)).thenReturn(Reservation.of(expectedNewReservation));

        // when
        CreatedReservation book = reservations.book(reservationCommand);
        assertEquals(BigDecimal.valueOf(55.50), book.getAmountToPay());
    }

    @Test
    public void shouldThrowExceptionWhenSeatsListIsEmpty() {
        //given
        var newReservation = new NewReservation(anyUuid(), new HashSet<>(), name("Piotrek"), surname("Jot"), email("rogue@wow.com"), Instant.now());

        //then
        expectedException.expect(ReservationException.class);
        expectedException.expect(hasMessage("You have to book at least one seat, but got"));

        //when
        Reservation.of(newReservation);
    }

    private Instant minutesLater(long minutes) {
        return Instant.now(CLOCK).plus(minutes, ChronoUnit.MINUTES);
    }

    private Instant minutesEarlier(long minutes) {
        return Instant.now(CLOCK).minus(minutes, ChronoUnit.MINUTES);
    }
}
