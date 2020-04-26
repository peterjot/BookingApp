package pl.deadwood.bookingapp.reservation.domain;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


abstract class ReservationExceptionMatcher extends TypeSafeMatcher<ReservationException> {

    static ReservationExceptionMatcher hasMessage(String message) {
        return new ReservationExceptionMessageMatcher(message);
    }

    private static class ReservationExceptionMessageMatcher extends ReservationExceptionMatcher {

        private String foundErrorMessage;
        private final String expectedErrorMessage;


        private ReservationExceptionMessageMatcher(String message) {
            this.expectedErrorMessage = message;
        }

        @Override
        protected boolean matchesSafely(final ReservationException exception) {
            this.foundErrorMessage = exception.getMessage();
            return foundErrorMessage.contains(expectedErrorMessage);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendValue(foundErrorMessage)
                    .appendText(" was not found instead of ")
                    .appendValue(expectedErrorMessage);
        }
    }
}

