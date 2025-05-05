package org.ru.dictionary.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mailing")
@Data
public class EmailConfig {
    private String activationUrl;
    private String emailAddressSender;
    private long activationTokenExpiration = 60;
}
