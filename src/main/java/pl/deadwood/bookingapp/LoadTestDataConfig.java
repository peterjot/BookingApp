package pl.deadwood.bookingapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.deadwood.bookingapp.reservation.domain.Email;
import pl.deadwood.bookingapp.reservation.domain.Name;
import pl.deadwood.bookingapp.reservation.domain.NewReservation;
import pl.deadwood.bookingapp.reservation.domain.ReservationRepository;
import pl.deadwood.bookingapp.reservation.domain.ReservationSeat;
import pl.deadwood.bookingapp.reservation.domain.Surname;
import pl.deadwood.bookingapp.reservation.domain.Ticket;
import pl.deadwood.bookingapp.screening.domain.Movie;
import pl.deadwood.bookingapp.screening.domain.Room;
import pl.deadwood.bookingapp.screening.domain.Screening;
import pl.deadwood.bookingapp.screening.domain.ScreeningRepository;
import pl.deadwood.bookingapp.screening.domain.Seat;
import pl.deadwood.bookingapp.screening.domain.SeatId;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.singletonList;


@Slf4j
@Configuration
public class LoadTestDataConfig {

    private static final Instant BASE_SCREENING_TIME = LocalDateTime.of(2030, 3, 5, 12, 0)
            .toInstant(ZoneOffset.UTC);

    @Bean
    @Profile("test_data")
    CommandLineRunner loadData(ScreeningRepository screeningRepository, ReservationRepository reservationRepository) {
        return args -> {
            log.debug("Loaded findAvailableScreenings data");

            // room 1
            Screening screening = screening(121231, movie1(), 80, room1());
            screeningRepository.save(screening);
            screeningRepository.save(screening(3232, movie2(), 80, room1()));

            // room 2
            screeningRepository.save(screening(143211, movie2(), 15, room2()));
            screeningRepository.save(screening(35, movie3(), 15, room2()));

            // room 3
            screeningRepository.save(screening(22313, movie3(), 15, room3()));
            screeningRepository.save(screening(11231, movie1(), 15, room3()));


            Instant screeningStart = LocalDateTime.of(1996, 3, 5, 12, 0).toInstant(ZoneOffset.UTC);
            screeningRepository.save(new Screening(anyId(), movie1(), screeningStart, screeningStart.plus(2, ChronoUnit.HOURS), room3(), seats()));

            reservationRepository.save(
                    new NewReservation(
                            screening.getId(),
                            new HashSet<>(singletonList(new ReservationSeat(SeatId.of(1, 2), Ticket.ADULT))),
                            new Name("Piotrek"),
                            new Surname("Jot"),
                            new Email("piotrekjasina@gmail.com"),
                            Instant.now().minus(5, ChronoUnit.MINUTES)));
        };
    }

    private Screening screening(int hoursAfterBaseTime, Movie movie, long durationInMinutes, Room room) {
        Instant screeningStart = BASE_SCREENING_TIME.plus(hoursAfterBaseTime, ChronoUnit.HOURS);
        Instant screeningEnd = screeningStart.plus(durationInMinutes, ChronoUnit.MINUTES);
        return new Screening(anyId(), movie, screeningStart, screeningEnd, room, seats());
    }

    private Room room1() {
        return new Room(UUID.fromString("bd4a5354-bd3e-11e9-9cb5-2a2ae2dbcce4"));
    }

    private Room room2() {
        return new Room(UUID.fromString("bd4a5638-bd3e-11e9-9cb5-2a2ae2dbcce4"));
    }

    private Room room3() {
        return new Room(UUID.fromString("bd4a57e6-bd3e-11e9-9cb5-2a2ae2dbcce4"));
    }

    private static Movie movie1() {
        return new Movie("Ąbćdę");
    }

    private static Movie movie2() {
        return new Movie("Ącćdęćźż");
    }

    private static Movie movie3() {
        return new Movie("ĆaćaćĘę");
    }

    private UUID anyId() {
        return UUID.randomUUID();
    }

    private static Set<Seat> seats() {
        var seats = new HashSet<Seat>();
        seats.add(new Seat(SeatId.of(0, 0)));
        seats.add(new Seat(SeatId.of(0, 1)));
        seats.add(new Seat(SeatId.of(0, 2)));
        seats.add(new Seat(SeatId.of(0, 3)));
        seats.add(new Seat(SeatId.of(1, 0)));
        seats.add(new Seat(SeatId.of(1, 1)));
        seats.add(new Seat(SeatId.of(1, 2)));
        seats.add(new Seat(SeatId.of(1, 3)));
        seats.add(new Seat(SeatId.of(2, 0)));
        seats.add(new Seat(SeatId.of(2, 1)));
        seats.add(new Seat(SeatId.of(2, 2)));
        seats.add(new Seat(SeatId.of(2, 3)));
        return seats;
    }
}
