package pl.deadwood.bookingapp.reservation.application;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.deadwood.bookingapp.common.ResponseUtil;
import pl.deadwood.bookingapp.reservation.domain.Reservation;
import pl.deadwood.bookingapp.reservation.domain.ReservationRepository;
import pl.deadwood.bookingapp.reservation.domain.ReservationSeat;
import pl.deadwood.bookingapp.reservation.domain.Status;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static pl.deadwood.bookingapp.ApiController.API_V1;

@RestController
@AllArgsConstructor
public class ReservationCrudController {

    private final ReservationRepository reservationRepository;

    @GetMapping(path = "/reservations", produces = API_V1)
    public ResponseEntity<Set<ReservationDto>> findByScreeningId(@RequestParam UUID screeningId) {
        Set<Reservation> reservations = reservationRepository.findByScreeningId(screeningId);
        return ResponseUtil.okOrNoContent(ReservationDto.from(reservations));
    }

    @GetMapping(path = "/reservations/{tokenId}", produces = API_V1)
    public ResponseEntity<ReservationDto> findById(@PathVariable UUID tokenId) {
        Optional<Reservation> reservation = reservationRepository.findById(tokenId);
        return ResponseUtil.okOrNotFound(reservation.map(ReservationDto::new));
    }

    @GetMapping(path = "/reservations/{tokenId}/email", produces = API_V1)
    public ResponseEntity<Map<String, String>> findEmailById(@PathVariable UUID tokenId) {
        return find(tokenId, reservation -> Collections.singletonMap("email", reservation.getEmail().getValue()));
    }

    @GetMapping(path = "/reservations/{tokenId}/name", produces = API_V1)
    public ResponseEntity<Map<String, String>> findNameById(@PathVariable UUID tokenId) {
        return find(tokenId, reservation -> Collections.singletonMap("name", reservation.getName().getValue()));
    }

    @GetMapping(path = "/reservations/{tokenId}/status", produces = API_V1)
    public ResponseEntity<Map<String, Status>> findStatusById(@PathVariable UUID tokenId) {
        return find(tokenId, reservation -> Collections.singletonMap("status", reservation.getStatus()));
    }

    private <T> ResponseEntity<T> find(UUID tokenId, Function<Reservation, T> mapper) {
        Optional<Reservation> reservation = reservationRepository.findById(tokenId);
        return ResponseUtil.okOrNotFound(reservation.map(mapper));
    }

    @Value
    private static class ReservationDto {
        UUID id;
        String email;
        String name;
        String surname;
        Status status;
        Instant expireTime;
        UUID screeningId;
        Set<ReservationSeat> reservationSeats;

        private ReservationDto(Reservation reservation) {
            this.id = reservation.getId();
            this.email = reservation.getEmail().getValue();
            this.name = reservation.getName().getValue();
            this.surname = reservation.getSurname().getValue();
            this.status = reservation.getStatus();
            this.expireTime = reservation.getExpireTime();
            this.screeningId = reservation.getScreeningId();
            this.reservationSeats = reservation.getReservationSeats();
        }

        private static Set<ReservationDto> from(Collection<Reservation> reservations) {
            return reservations.stream().map(ReservationDto::new).collect(Collectors.toSet());
        }
    }
}
