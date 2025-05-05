package org.ru.dictionary.service;

import org.ru.dictionary.entity.User;

public interface ActivationService {

    String generateAndSaveActivationToken(String email);

    void activateAccount(String token, String email);

    void sendActivationEmail(User user);
}