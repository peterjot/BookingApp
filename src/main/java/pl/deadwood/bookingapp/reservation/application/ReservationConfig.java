package pl.deadwood.bookingapp.reservation.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.deadwood.bookingapp.reservation.domain.ReservationRepository;
import pl.deadwood.bookingapp.reservation.domain.Reservations;
import pl.deadwood.bookingapp.reservation.infrastructure.InMemoryReservationRepository;
import pl.deadwood.bookingapp.screening.domain.Screenings;

import java.time.Clock;

@Configuration
public class ReservationConfig {

    @Bean
    ReservationRepository reservationRepository() {
        return new InMemoryReservationRepository();
    }

    @Bean
    Reservations reservationService(Clock clock,
                                    Screenings screenings,
                                    ReservationRepository reservationRepository,
                                    ApplicationEventPublisher applicationEventPublisher) {
        return new Reservations(
                clock,
                screenings,
                reservationRepository,
                new SpringEventPublisher(applicationEventPublisher));
    }

    @Bean("reservationEventHandler")
    EventHandler eventHandler(Reservations reservations) {
        return new EventHandler(reservations);
    }
}
