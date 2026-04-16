package com.associados.cadastro.repository;

import com.associados.cadastro.model.Associado;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssociadoRepository extends CassandraRepository<Associado, UUID> {

    @Query("SELECT * FROM associados WHERE id = ?0")
    Associado findAssociadoById(UUID id);

    @Query("SELECT * FROM associados ALLOW FILTERING")
    List<Associado> findAllAssociados();

    @Query("SELECT * FROM associados WHERE nome = ?0")
    List<Associado> findByNome(String nome);

    @Query("SELECT * FROM associados WHERE cpf = ?0")
    List<Associado> findByCpf(String cpf);

    @Query("SELECT * FROM associados WHERE email = ?0")
    List<Associado> findByEmail(String email);

    @Query("SELECT * FROM associados WHERE cidade = ?0")
    List<Associado> findByCidade(String cidade);

    @Query("SELECT * FROM associados WHERE estado = ?0")
    List<Associado> findByEstado(String estado);

    @Query("SELECT * FROM associados WHERE ativo = ?0")
    List<Associado> findByAtivo(Boolean ativo);

    @Query("SELECT * FROM associados WHERE estado = ?0 AND ativo = ?1 ALLOW FILTERING")
    List<Associado> findByEstadoAndAtivo(String estado, Boolean ativo);

    @Query("SELECT * FROM associados WHERE cidade = ?0 AND ativo = ?1 ALLOW FILTERING")
    List<Associado> findByCidadeAndAtivo(String cidade, Boolean ativo);
}
