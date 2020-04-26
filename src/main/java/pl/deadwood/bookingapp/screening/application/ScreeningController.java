package pl.deadwood.bookingapp.screening.application;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.deadwood.bookingapp.screening.domain.Screenings;
import pl.deadwood.bookingapp.screening.domain.dto.AvailableScreening;
import pl.deadwood.bookingapp.screening.domain.dto.ScreeningInfo;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toCollection;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static pl.deadwood.bookingapp.ApiController.API_V1;


@RestController
@AllArgsConstructor
public class ScreeningController {

    @NonNull
    private final Screenings screenings;


    @GetMapping(path = "/screenings", produces = API_V1)
    public ResponseEntity<CollectionModel<EntityModel<AvailableScreening>>> findAvailableScreenings(
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to) {

        Set<EntityModel<AvailableScreening>> availableScreenings = (from == null || to == null)
                ? findAllScreenings()
                : findScreeningsByPeriod(from, to);

        return ResponseEntity.ok(
                CollectionModel.of(availableScreenings,
                        linkTo(methodOn(ScreeningController.class).findAvailableScreenings(from, to))
                                .withSelfRel())
        );
    }

    @GetMapping("/screenings/{id}")
    public ResponseEntity<EntityModel<ScreeningInfo>> findById(@PathVariable UUID id) {
        return screenings
                .find(id)
                .map(screeningInfo -> ok(EntityModel.of(screeningInfo, linkTo(methodOn(ScreeningController.class)
                        .findById(id))
                        .withSelfRel())))
                .orElse(notFound().build());
    }

    private Set<EntityModel<AvailableScreening>> findScreeningsByPeriod(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(value = "from", required = false) ZonedDateTime from, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(value = "to", required = false) ZonedDateTime to) {
        Set<EntityModel<AvailableScreening>> availableScreenings;
        availableScreenings =
                screenings
                        .find(from.toInstant(), to.toInstant())
                        .stream()
                        .map(availableScreening -> resourceWithLinkToScreeningSelf(
                                availableScreening.getId(),
                                availableScreening))
                        .collect(toSortedByTitleAndStartTimeSet());
        return availableScreenings;
    }

    private Set<EntityModel<AvailableScreening>> findAllScreenings() {
        Set<EntityModel<AvailableScreening>> availableScreenings;
        availableScreenings = screenings.findAll().stream()
                .map(availableScreening -> resourceWithLinkToScreeningSelf(
                        availableScreening.getId(),
                        availableScreening))
                .collect(toSortedByTitleAndStartTimeSet());
        return availableScreenings;
    }

    private EntityModel<AvailableScreening> resourceWithLinkToScreeningSelf(UUID screeningId, AvailableScreening availableScreening) {
        return EntityModel.of(
                availableScreening,
                linkTo(methodOn(ScreeningController.class).findById(screeningId))
                        .withSelfRel());
    }


    private Collector<EntityModel<AvailableScreening>, ?, TreeSet<EntityModel<AvailableScreening>>> toSortedByTitleAndStartTimeSet() {
        return toCollection(() -> new TreeSet<>(ORDER_BY_TITLE_AND_START_TIME));
    }

    private static final Comparator<EntityModel<AvailableScreening>> ORDER_BY_TITLE_AND_START_TIME =
            (o1, o2) -> AvailableScreening.ORDER_BY_TITLE_AND_START_TIME.compare(o1.getContent(), o2.getContent());
}
