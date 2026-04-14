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
}
