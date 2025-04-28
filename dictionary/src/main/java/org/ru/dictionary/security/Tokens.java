package org.ru.dictionary.security;

public record Tokens(String accessToken, String accessTokenExpiry,
                     String refreshToken, String refreshTokenExpiry) {
}