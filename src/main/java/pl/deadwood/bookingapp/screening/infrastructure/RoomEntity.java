package pl.deadwood.bookingapp.screening.infrastructure;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.deadwood.bookingapp.screening.domain.Room;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;


@Getter
@Setter
@Entity
@NoArgsConstructor
class RoomEntity {

    @Id
    UUID id;

    Room toDomain() {
        return new Room(id);
    }

    RoomEntity(Room room) {
        this.id = room.getId();
    }
}
