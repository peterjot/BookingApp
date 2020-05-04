package pl.deadwood.bookingapp.screening.infrastructure;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.deadwood.bookingapp.screening.domain.Screening;
import pl.deadwood.bookingapp.screening.domain.dto.AvailableScreening;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Getter
@Setter
@Entity
@NoArgsConstructor
class ScreeningEntity {

    @Id
    private UUID id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private MovieEntity movie;

    @NotNull
    private Instant start;

    @NotNull
    private Instant end;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private RoomEntity room;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<SeatEntity> seats;

    ScreeningEntity(Screening screening) {
        this.id = screening.getId();
        this.movie = new MovieEntity(screening.getMovie());
        this.start = screening.getStart();
        this.end = screening.getEnd();
        this.seats = screening.getSeats().stream().map(SeatEntity::new).collect(toSet());
        this.room = new RoomEntity(screening.getRoom());
    }


    Screening toDomain() {
        return new Screening(id, movie.toDomain(), start, end, room.toDomain(), SeatEntity.toDomain(this.seats));
    }

    AvailableScreening toAvailableDomain() {
        return new AvailableScreening(id, movie.toDomain(), start);
    }
}

