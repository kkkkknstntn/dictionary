package org.ru.dictionary.service.impl;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.config.EmailConfig;
import org.ru.dictionary.entity.MailToken;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.enums.BusinessErrorCodes;
import org.ru.dictionary.enums.EmailTemplateName;
import org.ru.dictionary.enums.MailTokenType;
import org.ru.dictionary.exception.ApiException;
import org.ru.dictionary.repository.MailTokenRepository;
import org.ru.dictionary.repository.UserRepository;
import org.ru.dictionary.service.ActivationService;
import org.ru.dictionary.service.EmailService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivationServiceImpl implements ActivationService {
    private final MailTokenRepository mailTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailConfig emailConfig;

    @Override
    public String generateAndSaveActivationToken(String email) {
        deactivateExistingTokens(email, MailTokenType.CONFIRM);

        String generatedToken = generateActivationCode(6);
        MailToken mailToken = new MailToken();
        mailToken.setToken(generatedToken);
        mailToken.setExpiresAt(LocalDateTime.now().plusSeconds(
                emailConfig.getActivationTokenExpiration()
        ));
        mailToken.setEmail(email);
        mailToken.setTokenType(MailTokenType.CONFIRM);
        mailTokenRepository.save(mailToken);
        return generatedToken;
    }

    @Override
    public void activateAccount(String token, String email) {
        MailToken savedMailToken = mailTokenRepository.findByEmailAndEnabledAndToken(email, true, token)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.INVALID_ACTIVATION_TOKEN));

        if (LocalDateTime.now().isAfter(savedMailToken.getExpiresAt())) {
            throw new ApiException(BusinessErrorCodes.EXPIRED_ACTIVATION_TOKEN);
        }
        userRepository.findByUsername(savedMailToken.getEmail()).ifPresent(this::enableUser);
        mailTokenRepository.save(savedMailToken);
    }

    private void enableUser(User user) {
        user.setIsEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void sendActivationEmail(User user) {
        try {
            String token = generateAndSaveActivationToken(user.getUsername());
            emailService.sendEmail(
                    user.getUsername(),
                    user.getUsername(),
                    EmailTemplateName.ACTIVATE_ACCOUNT,
                    emailConfig.getActivationUrl() + "?email=" + user.getUsername() + "&token=" + token,
                    token,
                    "Account Activation"
            );
        } catch (Exception e) {
            enableUser(user);
        }
    }

    private void deactivateExistingTokens(String email, MailTokenType tokenType) {
        List<MailToken> activeTokens = mailTokenRepository.findByEmailAndEnabledAndTokenType(
                email,
                true,
                tokenType
        );
        activeTokens.forEach(token -> token.setEnabled(false));
        mailTokenRepository.saveAll(activeTokens);
    }

    private String generateActivationCode(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(secureRandom.nextInt(10));
        }
        return code.toString();
    }
}
