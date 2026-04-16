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
     * Minimum size (in bytes) required for the HMAC-SHA256 signing key.
     * 32 bytes = 256 bits, which matches the HS256 block size recommended by RFC 7518.
     */
    static final int MIN_SECRET_BYTES = 32;

    private final SecretKey key;
    private final long expiracaoMs;

    public JwtTokenProvider(
            @Value("${jwt.secret:}") String secret,
            @Value("${jwt.expiracao:86400000}") long expiracaoMs) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "jwt.secret (env var JWT_SECRET) não configurado. Configure uma chave com " +
                            "no mínimo " + MIN_SECRET_BYTES + " bytes antes de iniciar a aplicação."
            );
        }
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "jwt.secret (env var JWT_SECRET) é muito curto: " + secretBytes.length +
                            " bytes. Mínimo exigido: " + MIN_SECRET_BYTES + " bytes (256 bits)."
            );
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
