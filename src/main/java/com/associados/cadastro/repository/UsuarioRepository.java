package com.associados.cadastro.repository;

import com.associados.cadastro.model.Usuario;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepository extends CassandraRepository<Usuario, UUID> {
}
