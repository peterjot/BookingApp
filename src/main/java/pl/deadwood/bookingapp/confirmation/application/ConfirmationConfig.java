package pl.deadwood.bookingapp.confirmation.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import pl.deadwood.bookingapp.confirmation.domain.ConfirmationTokenRepository;
import pl.deadwood.bookingapp.confirmation.domain.Confirmations;
import pl.deadwood.bookingapp.reservation.domain.Email;

import java.time.Clock;

@Configuration
public class ConfirmationConfig {


    @Bean
    Confirmations confirmations(Clock clock,
                                JavaMailSender javaMailSender,
                                @Value("${bookingapp.email.name}") String emailFrom,
                                @Value("${bookingapp.email.enabled}") boolean mailEnabled,
                                ApplicationEventPublisher applicationEventPublisher,
                                ConfirmationTokenRepository confirmationTokenRepository) {

        return new Confirmations(
                clock,
                new SpringEventPublisherAdapter(applicationEventPublisher),
                new JavaMailSenderAdapter(Email.of(emailFrom), mailEnabled, javaMailSender),
                confirmationTokenRepository);
    }
}
