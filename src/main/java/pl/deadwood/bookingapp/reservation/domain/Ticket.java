package pl.deadwood.bookingapp.reservation.domain;

import lombok.NonNull;

import java.math.BigDecimal;

public enum Ticket {

    ADULT(BigDecimal.valueOf(25.00)),
    STUDENT(BigDecimal.valueOf(18.00)),
    CHILD(BigDecimal.valueOf(12.50));

    @NonNull
    private final BigDecimal price;


    Ticket(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
