# Cliente API - NeoApp 🏢

API REST desenvolvida em Spring Boot para cadastro e gestão de clientes pessoa física, incluindo validação de CPF, endereços e filtros de busca avançados.

## 🚀 Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.5** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança (configurada para permitir todas as requisições)
- **PostgreSQL 15** - Banco de dados
- **Docker & Docker Compose** - Containerização
- **Lombok** - Redução de código boilerplate
- **SpringDoc OpenAPI** - Documentação da API
- **Maven** - Gerenciamento de dependências


## 🏗️ Arquitetura do Projeto

```
src/main/java/com/neoapp/cliente_api/
├── 📁 controller/
│   ├── clientController.java              # Endpoints REST
│   ├── 📁 dto/
│   │   ├── ClienteFiltersDto.java         # Filtros de busca
│   │   ├── clientResponseDto.java         # Resposta da API
│   │   └── clientWithAddressDto.java      # Entrada de dados
│   └── 📁 commonExceptions/
│       ├── globalExceptionsHandler.java   # Tratamento global de erros
│       ├── clientNotFoundExceptions.java  # Cliente não encontrado
│       └── cpfAlreadyExistsException.java # CPF duplicado
├── 📁 model/
│   ├── Client.java                        # Entidade Cliente
│   └── Address.java                       # Entidade Endereço
├── 📁 repository/
│   ├── ClientRepository.java              # Repositório JPA
│   └── AddressRepository.java             # Repositório de Endereços
├── 📁 service/
│   └── clientService.java                 # Lógica de negócio
├── 📁 Specification/
│   └── SpecificationClient.java           # Consultas dinâmicas
├── 📁 validator/
│   ├── StateValidator.java                # Anotação customizada
│   └── StateValidatorClass.java           # Validador de UF
├── ClienteApiApplication.java             # Classe principal
├── OpenApiConfig.java                     # Configuração Swagger
└── SecurityConfig.java                    # Configuração de segurança
```

## 📋 Funcionalidades

### ✅ CRUD Completo
- **POST** `/client` - Criar cliente com endereço
- **GET** `/client/{cpf}` - Buscar cliente por CPF
- **PUT** `/client/{cpf}` - Atualizar cliente por CPF
- **DELETE** `/client/{cpf}` - Excluir cliente por CPF

### 🔍 Busca e Filtros
- **GET** `/client` - Listar todos os clientes (paginado)
- **GET** `/client/search` - Busca com filtros avançados:
  - Nome (busca parcial, case-insensitive)
  - CPF, Email, Telefone
  - Data de nascimento
  - Endereço (rua, cidade, estado, CEP)

### 🛡️ Validações
- **CPF**: Validação completa usando Caelum Stella
- **Email**: Formato válido
- **UF**: Validação customizada dos 27 estados brasileiros
- **Data de nascimento**: Deve ser no passado
- **Campos obrigatórios**: Nome, CPF, email, telefone, endereço completo

### 📊 Recursos Especiais
- **Cálculo automático da idade**
- **Auditoria**: Data de registro e última atualização
- **Paginação** em todas as listagens
- **Documentação Swagger** automática
- **Tratamento de exceções** centralizado

## 🐳 Como Executar com Docker

### Pré-requisitos
- Docker
- Docker Compose

### 1. Clone o Repositório
```bash
git clone <seu-repositorio>
cd cliente-api
```

### 2. Execute com Docker Compose
```bash
# Construir e iniciar os serviços
docker compose up --build -d

# Verificar se os containers estão rodando
docker compose ps
```

### 3. Serviços Disponíveis
- **API**: http://localhost:8081
- **PostgreSQL**: localhost:5433
- **Swagger UI**: http://localhost:8081/swagger-ui.html

### 4. Logs e Monitoramento
```bash
# Ver logs da aplicação
docker compose logs -f cliente-api

# Ver logs do banco
docker compose logs -f postgres
```

### 5. Parar os Serviços
```bash
# Parar containers
docker compose down

# Parar e remover volumes
docker compose down -v
```

## 🗄️ Banco de Dados

### Estrutura das Tabelas

**Tabela `clients`:**
- `id` (SERIAL PRIMARY KEY)
- `cpf` (VARCHAR NOT NULL)
- `name` (VARCHAR NOT NULL)  
- `birth_date` (DATE NOT NULL)
- `email` (VARCHAR NOT NULL)
- `phone_number` (VARCHAR NOT NULL)
- `address_id` (INT FOREIGN KEY)
- `date_register` (TIMESTAMP)
- `date_update` (TIMESTAMP)

**Tabela `address`:**
- `id` (SERIAL PRIMARY KEY)
- `street` (VARCHAR NOT NULL)
- `city` (VARCHAR NOT NULL)
- `state` (VARCHAR NOT NULL)
- `zip_code` (VARCHAR NOT NULL)

### Dados de Teste
O banco é inicializado automaticamente com 10 clientes de exemplo através do script `docker/postgres/init/01-init-database.sql`.

## 🔧 Configuração

### Profiles do Spring
- **Local**: `application.yml` (PostgreSQL local)
- **Docker**: `application-docker.yml` (Container PostgreSQL)

### Variáveis de Ambiente (Docker)
```yaml
SPRING_PROFILES_ACTIVE: docker
SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/cliente_api
SPRING_DATASOURCE_USERNAME: postgres
SPRING_DATASOURCE_PASSWORD: postgres123
SPRING_JPA_HIBERNATE_DDL_AUTO: update
```

## 📖 Documentação da API

### Swagger UI
Acesse: http://localhost:8081/swagger-ui.html

## 🧪 Testes

O projeto inclui testes unitários para:
- **Service Layer**: `ClientServiceTest.java`
- **Specifications**: `SpecificationClientTest.java`  
- **Validators**: `StateValidatorClassTest.java`

```bash
# Executar testes
mvn test
```