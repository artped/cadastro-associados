package com.associados.cadastro.service;

import com.associados.cadastro.dto.LoginRequest;
import com.associados.cadastro.dto.LoginResponse;
import com.associados.cadastro.dto.RegistroUsuarioDTO;
import com.associados.cadastro.exception.BusinessException;
import com.associados.cadastro.model.Usuario;
import com.associados.cadastro.model.UsuarioPorEmail;
import com.associados.cadastro.repository.UsuarioPorEmailRepository;
import com.associados.cadastro.repository.UsuarioRepository;
import com.associados.cadastro.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioPorEmailRepository usuarioPorEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        UsuarioPorEmail usuarioPorEmail = usuarioPorEmailRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        if (!Boolean.TRUE.equals(usuarioPorEmail.getAtivo())) {
            throw new BusinessException("Usuário inativo");
        }

        if (!passwordEncoder.matches(request.getSenha(), usuarioPorEmail.getSenha())) {
            throw new BusinessException("Credenciais inválidas");
        }

        String token = jwtTokenProvider.gerarToken(usuarioPorEmail.getEmail(), usuarioPorEmail.getRole());

        return LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .email(usuarioPorEmail.getEmail())
                .nome(usuarioPorEmail.getNome())
                .role(usuarioPorEmail.getRole())
                .build();
    }

    public void registrar(RegistroUsuarioDTO dto) {
        if (usuarioPorEmailRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessException("E-mail já cadastrado");
        }

        UUID userId = UUID.randomUUID();
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        Usuario usuario = Usuario.builder()
                .id(userId)
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(senhaCriptografada)
                .role("USER")
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .build();

        UsuarioPorEmail usuarioPorEmail = UsuarioPorEmail.builder()
                .email(dto.getEmail())
                .usuarioId(userId)
                .nome(dto.getNome())
                .senha(senhaCriptografada)
                .role("USER")
                .ativo(true)
                .build();

        usuarioRepository.save(usuario);
        usuarioPorEmailRepository.save(usuarioPorEmail);

        log.info("Usuário registrado com e-mail: {}", dto.getEmail());
    }
}
