# Cadastro de Associados

Sistema de Cadastro de Associados desenvolvido com **Spring Boot 3** e **Apache Cassandra**.

## Funcionalidades

- **CRUD completo** de Associados (Criar, Ler, Atualizar, Deletar)
- **Autenticação e Autorização** com JWT (JSON Web Token)
- **Busca com filtros** (nome, CPF, e-mail, cidade, estado, status)
- **Relatórios** (geral, por estado, por cidade)
- **Importação/Exportação** de dados via CSV
- **Soft delete** (desativação de associados)
- **Documentação da API** com Swagger/OpenAPI

## Campos do Associado

| Campo | Tipo | Obrigatório |
|-------|------|-------------|
| Nome | String | Sim |
| CPF | String (11 dígitos) | Sim |
| E-mail | String | Sim |
| Telefone | String | Não |
| Logradouro | String | Não |
| Número | String | Não |
| Complemento | String | Não |
| Bairro | String | Não |
| Cidade | String | Não |
| Estado | String | Não |
| CEP | String (8 dígitos) | Não |
| Data de Nascimento | Date | Não |

## Pré-requisitos

- **Java 17+**
- **Maven 3.8+**
- **Apache Cassandra 4.x** (rodando na porta 9042)

## Configuração do Cassandra

1. Inicie o Cassandra:
```bash
cassandra -f
```

2. Crie o keyspace (o schema das tabelas é criado automaticamente pela aplicação):
```cql
CREATE KEYSPACE IF NOT EXISTS cadastro_associados
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
```

## Como Executar

```bash
# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn spring-boot:run

# Ou gerar o JAR e executar
mvn clean package -DskipTests
java -jar target/cadastro-associados-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: `http://localhost:8080`

## Documentação da API (Swagger)

Acesse: `http://localhost:8080/swagger-ui.html`

## Endpoints da API

### Autenticação
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/registrar` | Registrar novo usuário |
| POST | `/api/auth/login` | Realizar login (retorna token JWT) |

### Associados (requer autenticação)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/associados` | Criar novo associado |
| GET | `/api/associados` | Listar todos os associados |
| GET | `/api/associados/{id}` | Buscar associado por ID |
| GET | `/api/associados/buscar` | Buscar com filtros |
| PUT | `/api/associados/{id}` | Atualizar associado |
| DELETE | `/api/associados/{id}` | Deletar associado |
| PATCH | `/api/associados/{id}/desativar` | Desativar associado |

### Relatórios (requer autenticação)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/relatorios/geral` | Relatório geral |
| GET | `/api/relatorios/por-estado` | Relatório por estado |
| GET | `/api/relatorios/por-cidade` | Relatório por cidade |

### Importação/Exportação (requer autenticação)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/csv/exportar` | Exportar associados para CSV |
| POST | `/api/csv/importar` | Importar associados de CSV |

## Exemplo de Uso

### 1. Registrar usuário
```bash
curl -X POST http://localhost:8080/api/auth/registrar \
  -H "Content-Type: application/json" \
  -d '{"nome": "Admin", "email": "admin@example.com", "senha": "123456"}'
```

### 2. Fazer login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@example.com", "senha": "123456"}'
```

### 3. Criar associado (usar o token retornado no login)
```bash
curl -X POST http://localhost:8080/api/associados \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678901",
    "email": "joao@example.com",
    "telefone": "11999999999",
    "logradouro": "Rua das Flores",
    "numero": "100",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01001000",
    "dataNascimento": "1990-01-15"
  }'
```

## Estrutura do Projeto

```
src/main/java/com/associados/cadastro/
├── CadastroAssociadosApplication.java
├── config/
│   ├── CassandraConfig.java
│   └── SecurityConfig.java
├── controller/
│   ├── AssociadoController.java
│   ├── AuthController.java
│   ├── CsvController.java
│   └── ReportController.java
├── dto/
│   ├── AssociadoDTO.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   └── RegistroUsuarioDTO.java
├── exception/
│   ├── BusinessException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model/
│   ├── Associado.java
│   ├── Usuario.java
│   └── UsuarioPorEmail.java
├── repository/
│   ├── AssociadoRepository.java
│   ├── UsuarioPorEmailRepository.java
│   └── UsuarioRepository.java
├── security/
│   ├── JwtAuthenticationFilter.java
│   └── JwtTokenProvider.java
└── service/
    ├── AssociadoService.java
    ├── CsvService.java
    ├── ReportService.java
    └── UsuarioService.java
```

## Configuração

As configurações podem ser alteradas no arquivo `src/main/resources/application.properties`:

| Propriedade | Descrição | Padrão |
|-------------|-----------|--------|
| `server.port` | Porta do servidor | 8080 |
| `spring.cassandra.contact-points` | Host do Cassandra | localhost |
| `spring.cassandra.port` | Porta do Cassandra | 9042 |
| `spring.cassandra.keyspace-name` | Nome do keyspace | cadastro_associados |
| `jwt.secret` | Chave secreta do JWT | (alterar em produção) |
| `jwt.expiracao` | Tempo de expiração do token (ms) | 86400000 (24h) |

## Tecnologias

- Java 17
- Spring Boot 3.2.5
- Spring Data Cassandra
- Spring Security
- JWT (jjwt 0.12.5)
- OpenCSV 5.9
- SpringDoc OpenAPI (Swagger)
- Lombok
- Maven
