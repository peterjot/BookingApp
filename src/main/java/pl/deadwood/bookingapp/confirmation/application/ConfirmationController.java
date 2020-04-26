package pl.deadwood.bookingapp.confirmation.application;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.deadwood.bookingapp.confirmation.domain.ConfirmationToken;
import pl.deadwood.bookingapp.confirmation.domain.Confirmations;
import pl.deadwood.bookingapp.reservation.domain.OnReservationCreatedEvent;

import java.net.InetAddress;
import java.util.UUID;

import static java.lang.String.format;
import static pl.deadwood.bookingapp.ApiController.API_V1;

@Slf4j
@RestController
class ConfirmationController {

    static final String CONFIRM_RESERVATION_URI = "/confirmation/reservation/";


    @NonNull
    private final Confirmations confirmations;
    private final int port;

    ConfirmationController(@Value("${server.port}") int port,
                           @NonNull Confirmations confirmations) {
        this.port = port;
        this.confirmations = confirmations;
    }


    @PostMapping(path = CONFIRM_RESERVATION_URI, produces = API_V1)
    public ResponseEntity<ConfirmationToken> confirmReservation(@RequestParam("tokenId") UUID tokenId) {
        return confirmations
                .confirmReservation(tokenId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @EventListener
    public void handle(OnReservationCreatedEvent event) {
        var confirmationUrl = format("http://%s:%d" + CONFIRM_RESERVATION_URI,
                InetAddress.getLoopbackAddress().getHostName(),
                port);
        confirmations.create(event.getReservationId(), event.getEmail(), confirmationUrl);
    }
}
