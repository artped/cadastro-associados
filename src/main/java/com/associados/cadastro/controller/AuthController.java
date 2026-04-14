package com.associados.cadastro.controller;

import com.associados.cadastro.dto.LoginRequest;
import com.associados.cadastro.dto.LoginResponse;
import com.associados.cadastro.dto.RegistroUsuarioDTO;
import com.associados.cadastro.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Login e Registro de usuários")
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Realizar login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registrar")
    @Operation(summary = "Registrar novo usuário")
    public ResponseEntity<Map<String, String>> registrar(@Valid @RequestBody RegistroUsuarioDTO dto) {
        usuarioService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensagem", "Usuário registrado com sucesso"));
    }
}
