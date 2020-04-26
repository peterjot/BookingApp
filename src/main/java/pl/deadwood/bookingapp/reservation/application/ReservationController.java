package pl.deadwood.bookingapp.reservation.application;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.deadwood.bookingapp.reservation.domain.ReservationSeat;
import pl.deadwood.bookingapp.reservation.domain.Reservations;
import pl.deadwood.bookingapp.reservation.domain.Ticket;
import pl.deadwood.bookingapp.reservation.domain.dto.CreatedReservation;
import pl.deadwood.bookingapp.reservation.domain.dto.ReservationCommand;
import pl.deadwood.bookingapp.screening.domain.SeatId;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;
import static pl.deadwood.bookingapp.ApiController.API_V1;

@RestController
@AllArgsConstructor
class ReservationController {

    @NonNull
    private final Reservations reservations;

    @PostMapping(path = "/reservations", produces = API_V1)
    public ResponseEntity<EntityModel<CreatedReservation>> book(@Valid @RequestBody ReservationDto reservationDto) {
        CreatedReservation book = reservations.book(reservationDto.toReservationCommand());

        return ok(EntityModel.of(book, linkTo(methodOn(ReservationController.class)
                .book(reservationDto))
                .withSelfRel()));
    }

    @Value
    static class ReservationDto {
        @NotNull
        UUID screeningId;
        @NotEmpty
        Set<ReservationSeatDto> reservationSeats;
        @NotBlank
        String name;
        @NotBlank
        String surname;
        @Email
        @NotNull
        String email;

        private ReservationCommand toReservationCommand() {
            return new ReservationCommand(screeningId, domainSeats(), name, surname, email);
        }

        private Set<ReservationSeat> domainSeats() {
            return reservationSeats
                    .stream()
                    .map(ReservationSeatDto::toDomain)
                    .collect(toSet());
        }
    }

    @Value
    static class ReservationSeatDto {
        int row;
        int column;
        @NotNull
        Ticket ticket;

        private ReservationSeat toDomain() {
            return new ReservationSeat(SeatId.of(row, column), ticket);
        }
    }
}
