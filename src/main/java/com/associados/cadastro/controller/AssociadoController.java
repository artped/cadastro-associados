package com.associados.cadastro.controller;

import com.associados.cadastro.dto.AssociadoDTO;
import com.associados.cadastro.service.AssociadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/associados")
@RequiredArgsConstructor
@Tag(name = "Associados", description = "Operações de CRUD de Associados")
public class AssociadoController {

    private final AssociadoService associadoService;

    @PostMapping
    @Operation(summary = "Criar novo associado")
    public ResponseEntity<AssociadoDTO> criar(@Valid @RequestBody AssociadoDTO dto) {
        AssociadoDTO criado = associadoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar associado por ID")
    public ResponseEntity<AssociadoDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(associadoService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos os associados")
    public ResponseEntity<List<AssociadoDTO>> listarTodos() {
        return ResponseEntity.ok(associadoService.listarTodos());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar associados com filtros")
    public ResponseEntity<List<AssociadoDTO>> buscarPorFiltro(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Boolean ativo) {
        return ResponseEntity.ok(associadoService.buscarPorFiltro(nome, cpf, email, cidade, estado, ativo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar associado")
    public ResponseEntity<AssociadoDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody AssociadoDTO dto) {
        return ResponseEntity.ok(associadoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar associado")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        associadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar associado (soft delete)")
    public ResponseEntity<Void> desativar(@PathVariable UUID id) {
        associadoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
