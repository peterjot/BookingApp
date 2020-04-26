package pl.deadwood.bookingapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;


@Configuration
public class BookingConfig {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

}
