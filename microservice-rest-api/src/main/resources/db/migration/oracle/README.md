# Migrations Oracle Database

Este diretório contém as migrations específicas para Oracle Database do projeto de Gerenciamento de Catálogo de Produtos.

## Estrutura

As migrations seguem o padrão Flyway de versionamento:

- `V1__create_empresa_table.sql` - Criação da tabela de empresas
- `V2__create_produto_table.sql` - Criação da tabela de produtos
- `V3__create_historico_alteracao_table.sql` - Criação da tabela de histórico de alterações

## Características Específicas do Oracle

### Tipos de Dados

- **NUMBER(19)**: IDs e valores numéricos inteiros
- **NUMBER(10,2)**: Valores decimais (preço)
- **VARCHAR2**: Strings de tamanho variável
- **TIMESTAMP**: Data e hora
- **CLOB**: Campos de texto grandes (TEXT)

### Sequences

Cada tabela possui sua própria sequence para geração de IDs:
- `empresa_seq`
- `produto_seq`
- `historico_alteracao_seq`

### CNPJ Numérico

**IMPORTANTE**: As migrations foram criadas para suportar **APENAS CNPJ NUMÉRICO** (formato legado pré-2026).

#### Constraints de Validação

Todas as colunas de CNPJ possuem constraints CHECK utilizando REGEXP_LIKE:

```sql
CONSTRAINT ck_empresa_cnpj_numerico CHECK (REGEXP_LIKE(cnpj, '^[0-9]{14}$'))
```

Esta constraint garante que:
- O CNPJ contenha exatamente 14 caracteres
- Todos os caracteres sejam dígitos numéricos (0-9)
- **CNPJ alfanumérico será REJEITADO pelo banco de dados**

#### Tabelas Afetadas

1. **EMPRESA**: 
   - Coluna `cnpj` (NOT NULL)
   - Constraint: `ck_empresa_cnpj_numerico`

2. **PRODUTO**: 
   - Coluna `cnpj_fornecedor` (NULLABLE)
   - Constraint: `ck_produto_cnpj_numerico` (valida apenas se não for NULL)

### Índices

Índices foram criados para otimizar consultas frequentes:

#### Tabela EMPRESA
- `idx_empresa_cnpj` - Busca por CNPJ
- `idx_empresa_razao_social` - Busca por razão social

#### Tabela PRODUTO
- `idx_produto_nome` - Busca por nome
- `idx_produto_categoria` - Busca por categoria
- `idx_produto_cnpj_fornecedor` - Busca por CNPJ do fornecedor

#### Tabela HISTORICO_ALTERACAO
- `idx_historico_produto_id` - Busca por produto
- `idx_historico_tipo_operacao` - Busca por tipo de operação
- `idx_historico_data_hora` - Busca por data/hora
- `idx_historico_produto_data` - Índice composto para consultas combinadas

## Executando as Migrations

### Pré-requisitos

1. Oracle Database instalado e configurado
2. Flyway configurado no projeto
3. Credenciais de acesso ao banco

### Configuração

No arquivo `application.yml`, configure:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@//localhost:1521/XEPDB1
    username: seu_usuario
    password: sua_senha
    driver-class-name: oracle.jdbc.OracleDriver
  
  flyway:
    enabled: true
    locations: classpath:db/migration/oracle
    baseline-on-migrate: true
```

### Execução

As migrations serão executadas automaticamente na inicialização da aplicação Spring Boot.

Para executar manualmente via Flyway CLI:

```bash
flyway -url=jdbc:oracle:thin:@//localhost:1521/XEPDB1 \
       -user=seu_usuario \
       -password=sua_senha \
       -locations=filesystem:src/main/resources/db/migration/oracle \
       migrate
```

## Validação

Após executar as migrations, valide:

1. **Tabelas criadas**:
   ```sql
   SELECT table_name FROM user_tables 
   WHERE table_name IN ('EMPRESA', 'PRODUTO', 'HISTORICO_ALTERACAO');
   ```

2. **Sequences criadas**:
   ```sql
   SELECT sequence_name FROM user_sequences 
   WHERE sequence_name LIKE '%_SEQ';
   ```

3. **Constraints de CNPJ**:
   ```sql
   SELECT constraint_name, search_condition 
   FROM user_constraints 
   WHERE constraint_name LIKE '%CNPJ%';
   ```

4. **Teste de inserção com CNPJ inválido** (deve falhar):
   ```sql
   INSERT INTO empresa (id, cnpj, razao_social, ativa, data_cadastro)
   VALUES (1, '12ABC678901234', 'Teste', 1, SYSTIMESTAMP);
   ```

## Rollback

Para reverter as migrations em caso de necessidade:

```bash
flyway -url=jdbc:oracle:thin:@//localhost:1521/XEPDB1 \
       -user=seu_usuario \
       -password=sua_senha \
       undo
```

Ou manualmente:

```sql
DROP TABLE historico_alteracao CASCADE CONSTRAINTS;
DROP TABLE produto CASCADE CONSTRAINTS;
DROP TABLE empresa CASCADE CONSTRAINTS;
DROP SEQUENCE historico_alteracao_seq;
DROP SEQUENCE produto_seq;
DROP SEQUENCE empresa_seq;
```

## Observações

- As migrations foram otimizadas para Oracle Database
- Todos os nomes de tabelas e colunas estão em snake_case
- Comentários foram adicionados em todas as tabelas e colunas
- Foreign keys e índices foram criados para garantir integridade e performance
- A constraint de CNPJ numérico impede a inserção de dados alfanuméricos