package com.associados.cadastro.repository;

import com.associados.cadastro.model.UsuarioPorEmail;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioPorEmailRepository extends CassandraRepository<UsuarioPorEmail, String> {

    Optional<UsuarioPorEmail> findByEmail(String email);
}
