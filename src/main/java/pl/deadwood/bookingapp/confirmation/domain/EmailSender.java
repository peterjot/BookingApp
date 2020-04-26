package pl.deadwood.bookingapp.confirmation.domain;

import lombok.NonNull;
import pl.deadwood.bookingapp.reservation.domain.Email;

public interface EmailSender {

    void sendVerificationToken(@NonNull Email email, @NonNull String emailBody);
}
