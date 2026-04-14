package com.associados.cadastro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("associados")
public class Associado {

    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID id;

    @Column("nome")
    private String nome;

    @Column("cpf")
    private String cpf;

    @Column("email")
    private String email;

    @Column("telefone")
    private String telefone;

    @Column("logradouro")
    private String logradouro;

    @Column("numero")
    private String numero;

    @Column("complemento")
    private String complemento;

    @Column("bairro")
    private String bairro;

    @Column("cidade")
    private String cidade;

    @Column("estado")
    private String estado;

    @Column("cep")
    private String cep;

    @Column("data_nascimento")
    private LocalDate dataNascimento;

    @Column("data_cadastro")
    private LocalDateTime dataCadastro;

    @Column("data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column("ativo")
    private Boolean ativo;
}
