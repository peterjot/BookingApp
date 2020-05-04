package pl.deadwood.bookingapp.screening.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.deadwood.bookingapp.screening.domain.ScreeningRepository;
import pl.deadwood.bookingapp.screening.domain.Screenings;

@Configuration
public class ScreeningConfig {


    @Bean
    Screenings screeningService(ApplicationEventPublisher applicationEventPublisher,
                                ScreeningRepository screeningRepository) {
        SpringEventPublisher publisher = new SpringEventPublisher(applicationEventPublisher);
        return new Screenings(screeningRepository, publisher);
    }
}
