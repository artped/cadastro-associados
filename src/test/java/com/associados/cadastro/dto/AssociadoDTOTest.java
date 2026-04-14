package com.associados.cadastro.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssociadoDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveValidarDTOValido() {
        AssociadoDTO dto = AssociadoDTO.builder()
                .nome("João Silva")
                .cpf("12345678901")
                .email("joao@example.com")
                .build();

        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void deveRejeitarNomeVazio() {
        AssociadoDTO dto = AssociadoDTO.builder()
                .nome("")
                .cpf("12345678901")
                .email("joao@example.com")
                .build();

        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void deveRejeitarCpfInvalido() {
        AssociadoDTO dto = AssociadoDTO.builder()
                .nome("João Silva")
                .cpf("123")
                .email("joao@example.com")
                .build();

        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void deveRejeitarEmailInvalido() {
        AssociadoDTO dto = AssociadoDTO.builder()
                .nome("João Silva")
                .cpf("12345678901")
                .email("email-invalido")
                .build();

        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
