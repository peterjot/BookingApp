package pl.deadwood.bookingapp.screening.infrastructure;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.deadwood.bookingapp.screening.domain.Movie;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
class MovieEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String title;

    MovieEntity(Movie movie) {
        this.title = movie.getTitle();
    }

    Movie toDomain() {
        return new Movie(title);
    }
}
