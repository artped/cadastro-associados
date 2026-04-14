package com.associados.cadastro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("usuarios_por_email")
public class UsuarioPorEmail {

    @PrimaryKeyColumn(name = "email", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String email;

    @Column("usuario_id")
    private UUID usuarioId;

    @Column("senha")
    private String senha;

    @Column("nome")
    private String nome;

    @Column("role")
    private String role;

    @Column("ativo")
    private Boolean ativo;
}
