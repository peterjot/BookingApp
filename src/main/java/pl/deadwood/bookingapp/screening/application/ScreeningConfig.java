package pl.deadwood.bookingapp.screening.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.deadwood.bookingapp.screening.domain.ScreeningRepository;
import pl.deadwood.bookingapp.screening.domain.Screenings;
import pl.deadwood.bookingapp.screening.infrastructure.InMemoryDevScreeningRepository;
import pl.deadwood.bookingapp.screening.infrastructure.JpaScreeningRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Configuration
public class ScreeningConfig {

    @Bean
    ScreeningRepository screeningRepository() {
        return new InMemoryDevScreeningRepository();
    }

    @Bean
    Screenings screeningService(ApplicationEventPublisher applicationEventPublisher,
                                ScreeningRepository screeningRepository) {
        SpringEventPublisher publisher = new SpringEventPublisher(applicationEventPublisher);
        return new Screenings(screeningRepository, publisher);
    }
}
