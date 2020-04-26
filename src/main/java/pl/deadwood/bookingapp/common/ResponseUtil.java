package pl.deadwood.bookingapp.common;

import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Optional;

public final class ResponseUtil {

    private ResponseUtil() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> ResponseEntity<T> okOrNotFound(Optional<T> optional) {
        return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public static <T, C extends Collection<T>> ResponseEntity<C> okOrNoContent(C collection) {
        return collection.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(collection);
    }
}
