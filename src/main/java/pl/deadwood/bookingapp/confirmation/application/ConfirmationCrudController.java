package pl.deadwood.bookingapp.confirmation.application;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.deadwood.bookingapp.common.ResponseUtil;
import pl.deadwood.bookingapp.confirmation.domain.ConfirmationToken;
import pl.deadwood.bookingapp.confirmation.domain.ConfirmationTokenRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static pl.deadwood.bookingapp.ApiController.API_V1;

@RestController
@RequiredArgsConstructor
public class ConfirmationCrudController {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @GetMapping(path = "confirmations/{tokenId}", produces = API_V1)
    public ResponseEntity<ConfirmationToken> confirmationByTokenId(@PathVariable("tokenId") UUID tokenId) {
        return ResponseUtil.okOrNotFound(confirmationTokenRepository.find(tokenId));
    }

    @PutMapping(path = "confirmations", produces = API_V1)
    public ResponseEntity<ConfirmationToken> update(@Valid @RequestBody ConfirmationTokenUpdateDto updatedConfirmationToken) {
        Optional<ConfirmationToken> result = confirmationTokenRepository
                .find(updatedConfirmationToken.getToken())
                .map(actualToken -> confirmationTokenRepository.save(updatedConfirmationToken.toDomain()));
        return ResponseUtil.okOrNotFound(result);
    }

    @DeleteMapping(path = "confirmations/{tokenId}", produces = API_V1)
    public ResponseEntity<ConfirmationToken> deleteConfirmation(@PathVariable("tokenId") UUID tokenId) {
        Optional<ConfirmationToken> result = confirmationTokenRepository.find(tokenId).map(token -> {
            confirmationTokenRepository.delete(token);
            return token;
        });
        return ResponseUtil.okOrNotFound(result);
    }


    @Value
    static class ConfirmationTokenUpdateDto {
        @NotNull
        UUID token;
        @NotNull
        UUID reservationID;
        @NotNull
        Instant expireTime;

        ConfirmationToken toDomain() {
            return new ConfirmationToken(token, reservationID, expireTime);
        }
    }
}
