package pl.deadwood.bookingapp.screening.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import pl.deadwood.bookingapp.screening.domain.dto.AvailableScreening;
import pl.deadwood.bookingapp.screening.domain.dto.ScreeningInfo;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Getter
@ToString
@AllArgsConstructor
public class Screening {

    @NonNull
    private final UUID id;

    @NonNull
    private final Movie movie;

    @NonNull
    private final Instant start;

    @NonNull
    private final Instant end;

    @NonNull
    private final Room room;

    @NonNull
    private final Set<Seat> seats;


    void reserveSeats(Set<SeatId> seatIds) {
        seats
                .stream()
                .filter(seat -> seatIds.contains(seat.getSeatId()))
                .forEach(Seat::reserveSeat);
    }

    AvailableScreening toAvailableScreeningDto() {
        return new AvailableScreening(id, movie, start);
    }


    ScreeningInfo toDto() {
        return new ScreeningInfo(id, room, getSeatDtos(), start);
    }

    private Set<pl.deadwood.bookingapp.screening.domain.dto.Seat> getSeatDtos() {
        return seats
                .stream()
                .map(Seat::toDto)
                .collect(toSet());
    }
}
