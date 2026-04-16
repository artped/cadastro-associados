package com.associados.cadastro.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private static final String TEST_SECRET =
            "test-only-secret-do-not-use-in-any-deployed-environment-0000";

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(TEST_SECRET, 86400000L);
    }

    @Test
    void deveRejeitarSegredoVazio() {
        assertThrows(IllegalStateException.class,
                () -> new JwtTokenProvider("", 86400000L));
        assertThrows(IllegalStateException.class,
                () -> new JwtTokenProvider("   ", 86400000L));
    }

    @Test
    void deveRejeitarSegredoCurto() {
        assertThrows(IllegalStateException.class,
                () -> new JwtTokenProvider("too-short", 86400000L));
    }

    @Test
    void deveGerarToken() {
        String token = jwtTokenProvider.gerarToken("test@example.com", "USER");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void deveExtrairEmailDoToken() {
        String token = jwtTokenProvider.gerarToken("test@example.com", "USER");
        String email = jwtTokenProvider.getEmailFromToken(token);
        assertEquals("test@example.com", email);
    }

    @Test
    void deveExtrairRoleDoToken() {
        String token = jwtTokenProvider.gerarToken("test@example.com", "ADMIN");
        String role = jwtTokenProvider.getRoleFromToken(token);
        assertEquals("ADMIN", role);
    }

    @Test
    void deveValidarTokenValido() {
        String token = jwtTokenProvider.gerarToken("test@example.com", "USER");
        assertTrue(jwtTokenProvider.validarToken(token));
    }

    @Test
    void deveRejeitarTokenInvalido() {
        assertFalse(jwtTokenProvider.validarToken("token-invalido"));
    }

    @Test
    void deveRejeitarTokenNulo() {
        assertFalse(jwtTokenProvider.validarToken(null));
    }
}
