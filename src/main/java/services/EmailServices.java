package services;

import enums.StatusEmail;
import models.EmailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import repositories.EmailRepository;
import sun.security.krb5.KrbException;
import sun.security.krb5.internal.ccache.MemoryCredentialsCache;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class EmailServices {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private EmailRepository emailRepository;

    public EmailModel sendEmail(EmailModel emailModel) throws KrbException, IOException {
        emailModel.setSendDateEmail(LocalDateTime.now());
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            emailSender.send(message);

            emailModel.setStatusEmail(StatusEmail.SENT);
        } catch (MailException e){
            emailModel.setStatusEmail(StatusEmail.ERROR);
        } finally {
            return emailRepository.save(emailModel);
        }
    }

    public Page<EmailModel> findAll(Pageable pageable) {
        return  emailRepository.findAll(pageable);
    }
}
