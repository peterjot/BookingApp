package pl.deadwood.bookingapp.confirmation.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pl.deadwood.bookingapp.reservation.domain.Email;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;


@Slf4j
@AllArgsConstructor
public class Confirmations {

    @NonNull
    private final Clock clock;

    @NonNull
    private final EventPublisher eventPublisher;

    @NonNull
    private final EmailSender emailSender;

    @NonNull
    private final ConfirmationTokenRepository confirmationTokenRepository;


    public void create(@NonNull UUID reservationId, @NonNull Email email, @NonNull String confirmationBaseUrl) {
        var verificationToken = ConfirmationToken.of(reservationId, Instant.now(clock));
        confirmationTokenRepository.save(verificationToken);

        log.debug("Sending confirmation token([{}]) to: [{}]", verificationToken.getToken(), email);
        emailSender.sendVerificationToken(email, createEmailBody(confirmationBaseUrl, verificationToken.getToken()));
    }


    public Optional<ConfirmationToken> confirmReservation(@NonNull UUID tokenId) {
        return confirmationTokenRepository
                .find(tokenId)
                .filter(token -> token.notExpired(Instant.now(clock)))
                .map(this::confirmReservation);
    }

    private ConfirmationToken confirmReservation(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
        eventPublisher.publish(confirmationToken::getReservationID);
        log.debug("Reservation confirmed. Token=[{}]", confirmationToken);
        return confirmationToken;
    }


    private static String createEmailBody(String confirmationBaseUrl, UUID confirmationToken) {
        String confirmationUrl = format("%s?token=%s", confirmationBaseUrl, confirmationToken);

        return "<!DOCTYPE html>\n" +
                "<head>\n" +
                "<title>Form submission</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<a href=\"" + confirmationUrl + "\">Click to verify reservation</a>" +
                "\n" +
                "</body>\n" +
                "</html>";
    }
}
