package com.associados.cadastro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    /**
     * Tamanho mínimo da chave HMAC-SHA256, em bytes (256 bits).
     * JWTs assinados com chaves menores são inseguros.
     */
    private static final int MIN_SECRET_LENGTH_BYTES = 32;

    private final SecretKey key;
    private final long expiracaoMs;

    public JwtTokenProvider(
            @Value("${jwt.secret:}") String secret,
            @Value("${jwt.expiracao:86400000}") long expiracaoMs) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "A propriedade 'jwt.secret' (variável de ambiente JWT_SECRET) é obrigatória. " +
                            "Defina um valor com no mínimo " + MIN_SECRET_LENGTH_BYTES + " caracteres.");
        }
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < MIN_SECRET_LENGTH_BYTES) {
            throw new IllegalStateException(
                    "A propriedade 'jwt.secret' deve ter no mínimo " + MIN_SECRET_LENGTH_BYTES +
                            " bytes (caracteres ASCII) para uso com HMAC-SHA256.");
        }
        this.key = Keys.hmacShaKeyFor(secretBytes);
        this.expiracaoMs = expiracaoMs;
    }

    public String gerarToken(String email, String role) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expiracaoMs);

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(key)
                .compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("role", String.class);
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
