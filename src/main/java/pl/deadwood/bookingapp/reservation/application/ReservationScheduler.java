package pl.deadwood.bookingapp.reservation.application;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pl.deadwood.bookingapp.reservation.domain.Reservations;

@Slf4j
@Configuration
@EnableScheduling
@AllArgsConstructor
class ReservationScheduler {

    private static final String EVERY_MINUTE = "0 * * * * *";

    @NonNull
    private final Reservations reservations;

    @Scheduled(cron = EVERY_MINUTE)
    void cancelExpiredReservations() {
        log.debug("Started canceling expired reservations...");
        reservations.cancelExpiredReservations();
    }
}
