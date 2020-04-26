package pl.deadwood.bookingapp;


import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.deadwood.bookingapp.reservation.domain.ReservationException;


@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<SimpleMessageResponse> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        String msg = illegalArgumentException.getMessage();
        log.error("Occurred illegalArgumentException: [{}]", msg, illegalArgumentException);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SimpleMessageResponse.of(msg));
    }

    @ExceptionHandler(ReservationException.class)
    public final ResponseEntity<SimpleMessageResponse> handleReservationException(ReservationException reservationException) {
        log.error("Occurred reservationException: [{}]", reservationException.getMessage(), reservationException);
        String msg = reservationException.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SimpleMessageResponse.of(msg));
    }

    @Value(staticConstructor = "of")
    private static class SimpleMessageResponse {
        String msg;
    }
}
