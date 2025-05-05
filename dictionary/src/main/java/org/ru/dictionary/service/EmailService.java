package org.ru.dictionary.service;

import org.ru.dictionary.enums.EmailTemplateName;

public interface EmailService {
    void sendEmail(String to,
                   String username,
                   EmailTemplateName emailTemplate,
                   String confirmationUrl,
                   String activationCode,
                   String subject);
}
