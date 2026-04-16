package com.associados.cadastro.service;

import com.associados.cadastro.dto.AssociadoDTO;
import com.associados.cadastro.exception.BusinessException;
import com.associados.cadastro.exception.ResourceNotFoundException;
import com.associados.cadastro.model.Associado;
import com.associados.cadastro.repository.AssociadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssociadoService {

    private final AssociadoRepository associadoRepository;

    public AssociadoDTO criar(AssociadoDTO dto) {
        validarCpfUnico(dto.getCpf(), null);

        Associado associado = toEntity(dto);
        associado.setId(UUID.randomUUID());
        associado.setDataCadastro(LocalDateTime.now());
        associado.setDataAtualizacao(LocalDateTime.now());
        associado.setAtivo(true);

        Associado salvo = associadoRepository.save(associado);
        log.info("Associado criado com ID: {}", salvo.getId());
        return toDTO(salvo);
    }

    public AssociadoDTO buscarPorId(UUID id) {
        Associado associado = associadoRepository.findAssociadoById(id);
        if (associado == null) {
            throw new ResourceNotFoundException("Associado não encontrado com ID: " + id);
        }
        return toDTO(associado);
    }

    public List<AssociadoDTO> listarTodos() {
        return associadoRepository.findAllAssociados()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<AssociadoDTO> buscarPorFiltro(String nome, String cpf, String email, String cidade, String estado, Boolean ativo) {
        List<Associado> todos = associadoRepository.findAllAssociados();

        return todos.stream()
                .filter(a -> nome == null || a.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(a -> cpf == null || a.getCpf().equals(cpf))
                .filter(a -> email == null || a.getEmail().toLowerCase().contains(email.toLowerCase()))
                .filter(a -> cidade == null || (a.getCidade() != null && a.getCidade().toLowerCase().contains(cidade.toLowerCase())))
                .filter(a -> estado == null || (a.getEstado() != null && a.getEstado().equalsIgnoreCase(estado)))
                .filter(a -> ativo == null || ativo.equals(a.getAtivo()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AssociadoDTO atualizar(UUID id, AssociadoDTO dto) {
        Associado existente = associadoRepository.findAssociadoById(id);
        if (existente == null) {
            throw new ResourceNotFoundException("Associado não encontrado com ID: " + id);
        }

        validarCpfUnico(dto.getCpf(), id);

        existente.setNome(dto.getNome());
        existente.setCpf(dto.getCpf());
        existente.setEmail(dto.getEmail());
        existente.setTelefone(dto.getTelefone());
        existente.setLogradouro(dto.getLogradouro());
        existente.setNumero(dto.getNumero());
        existente.setComplemento(dto.getComplemento());
        existente.setBairro(dto.getBairro());
        existente.setCidade(dto.getCidade());
        existente.setEstado(dto.getEstado());
        existente.setCep(dto.getCep());
        existente.setDataNascimento(dto.getDataNascimento());
        existente.setDataAtualizacao(LocalDateTime.now());

        if (dto.getAtivo() != null) {
            existente.setAtivo(dto.getAtivo());
        }

        Associado atualizado = associadoRepository.save(existente);
        log.info("Associado atualizado com ID: {}", atualizado.getId());
        return toDTO(atualizado);
    }

    public void deletar(UUID id) {
        Associado existente = associadoRepository.findAssociadoById(id);
        if (existente == null) {
            throw new ResourceNotFoundException("Associado não encontrado com ID: " + id);
        }
        associadoRepository.deleteById(id);
        log.info("Associado deletado com ID: {}", id);
    }

    public void desativar(UUID id) {
        Associado existente = associadoRepository.findAssociadoById(id);
        if (existente == null) {
            throw new ResourceNotFoundException("Associado não encontrado com ID: " + id);
        }
        existente.setAtivo(false);
        existente.setDataAtualizacao(LocalDateTime.now());
        associadoRepository.save(existente);
        log.info("Associado desativado com ID: {}", id);
    }

    public long contarAssociados() {
        return associadoRepository.count();
    }

    public long contarAssociadosAtivos() {
        return associadoRepository.findAllAssociados()
                .stream()
                .filter(a -> Boolean.TRUE.equals(a.getAtivo()))
                .count();
    }

    private void validarCpfUnico(String cpf, UUID idExcluir) {
        List<Associado> todos = associadoRepository.findAllAssociados();
        boolean cpfExiste = todos.stream()
                .filter(a -> !a.getId().equals(idExcluir))
                .anyMatch(a -> a.getCpf().equals(cpf));

        if (cpfExiste) {
            throw new BusinessException("Já existe um associado cadastrado com o CPF: " + cpf);
        }
    }

    private Associado toEntity(AssociadoDTO dto) {
        return Associado.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .logradouro(dto.getLogradouro())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .cep(dto.getCep())
                .dataNascimento(dto.getDataNascimento())
                .build();
    }

    private AssociadoDTO toDTO(Associado associado) {
        return AssociadoDTO.builder()
                .id(associado.getId().toString())
                .nome(associado.getNome())
                .cpf(associado.getCpf())
                .email(associado.getEmail())
                .telefone(associado.getTelefone())
                .logradouro(associado.getLogradouro())
                .numero(associado.getNumero())
                .complemento(associado.getComplemento())
                .bairro(associado.getBairro())
                .cidade(associado.getCidade())
                .estado(associado.getEstado())
                .cep(associado.getCep())
                .dataNascimento(associado.getDataNascimento())
                .ativo(associado.getAtivo())
                .build();
    }
}
