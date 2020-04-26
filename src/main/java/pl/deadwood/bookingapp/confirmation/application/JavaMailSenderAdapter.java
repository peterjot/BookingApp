package pl.deadwood.bookingapp.confirmation.application;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import pl.deadwood.bookingapp.confirmation.domain.EmailSender;
import pl.deadwood.bookingapp.reservation.domain.Email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
@AllArgsConstructor
class JavaMailSenderAdapter implements EmailSender {

    @NonNull
    private final Email from;

    private final boolean enabled;

    @NonNull
    private final JavaMailSender javaMailSender;

    @Override
    public void sendVerificationToken(@NonNull Email to, @NonNull String emailBody) {
        if (!enabled) {
            log.debug("Mail sender disabled.");
            return;
        }

        try {
            MimeMessage mimeMessage = createMessage(to, emailBody);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            log.error("Cannot send email", exception);
        }
    }

    private MimeMessage createMessage(Email to, String emailBody) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom(new InternetAddress(from.getValue()));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to.getValue()));
        mimeMessage.setSubject("Reservation confirmation");
        mimeMessage.setContent(emailBody, "text/html");
        return mimeMessage;
    }
}
