-- Criação do Keyspace
CREATE KEYSPACE IF NOT EXISTS cadastro_associados
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE cadastro_associados;

-- Tabela de Associados
CREATE TABLE IF NOT EXISTS associados (
    id UUID PRIMARY KEY,
    nome TEXT,
    cpf TEXT,
    email TEXT,
    telefone TEXT,
    logradouro TEXT,
    numero TEXT,
    complemento TEXT,
    bairro TEXT,
    cidade TEXT,
    estado TEXT,
    cep TEXT,
    data_nascimento DATE,
    data_cadastro TIMESTAMP,
    data_atualizacao TIMESTAMP,
    ativo BOOLEAN
);

-- Tabela de Usuários (autenticação)
CREATE TABLE IF NOT EXISTS usuarios (
    id UUID PRIMARY KEY,
    nome TEXT,
    email TEXT,
    senha TEXT,
    role TEXT,
    ativo BOOLEAN,
    data_criacao TIMESTAMP
);

-- Tabela de Usuários por E-mail (para busca por e-mail no login)
CREATE TABLE IF NOT EXISTS usuarios_por_email (
    email TEXT PRIMARY KEY,
    usuario_id UUID,
    senha TEXT,
    nome TEXT,
    role TEXT,
    ativo BOOLEAN
);

-- Índices secundários para consultas com filtros de busca
CREATE INDEX IF NOT EXISTS idx_associados_nome ON associados (nome);
CREATE INDEX IF NOT EXISTS idx_associados_cpf ON associados (cpf);
CREATE INDEX IF NOT EXISTS idx_associados_email ON associados (email);
CREATE INDEX IF NOT EXISTS idx_associados_cidade ON associados (cidade);
CREATE INDEX IF NOT EXISTS idx_associados_estado ON associados (estado);
CREATE INDEX IF NOT EXISTS idx_associados_ativo ON associados (ativo);
