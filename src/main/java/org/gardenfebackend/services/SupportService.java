package org.gardenfebackend.services;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.SupportMessageRequest;
import org.gardenfebackend.models.SupportMessage;
import org.gardenfebackend.models.User;
import org.gardenfebackend.repositories.SupportMessageRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportMessageRepository supportMessageRepository;
    private final JavaMailSender mailSender;

    @Transactional
    public void sendSupportMessage(SupportMessageRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SupportMessage message = SupportMessage.builder()
                .user(user)
                .subject(request.getSubject())
                .message(request.getMessage())
                .build();
        supportMessageRepository.save(message);

        String userName = user.getFullName() != null ? user.getFullName() : "пользователь";
        String mailText = String.format(
                "Добрый день, %s!\n\n" +
                        "Мы получили ваше обращение в службу поддержки:\n" +
                        "Тема: %s\n\n" +
                        "Текст сообщения:\n%s\n\n" +
                        "Наша команда поддержки скоро свяжется с вами.\n\n" +
                        "— GardenApp Support",
                userName,
                request.getSubject(),
                request.getMessage()
        );

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("artem.7ko52@gmail.com");
        mail.setTo(user.getEmail());
        mail.setSubject("GardenApp — ваше обращение в поддержку");
        mail.setText(mailText);

        mailSender.send(mail);
    }
}
