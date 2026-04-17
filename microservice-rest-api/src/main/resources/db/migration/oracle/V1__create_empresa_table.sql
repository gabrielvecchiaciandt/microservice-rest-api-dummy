-- Migration V1: Criação da tabela EMPRESA
-- Oracle Database
-- Suporta apenas CNPJ numérico (14 dígitos) - formato legado

-- Criação da sequence para geração de IDs
CREATE SEQUENCE empresa_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Criação da tabela EMPRESA
CREATE TABLE empresa (
    id NUMBER(19) NOT NULL,
    cnpj VARCHAR2(14) NOT NULL,
    razao_social VARCHAR2(150) NOT NULL,
    nome_fantasia VARCHAR2(100),
    email VARCHAR2(150),
    telefone VARCHAR2(20),
    ativa NUMBER(1) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL,
    CONSTRAINT pk_empresa PRIMARY KEY (id),
    CONSTRAINT uk_empresa_cnpj UNIQUE (cnpj),
    CONSTRAINT ck_empresa_cnpj_numerico CHECK (REGEXP_LIKE(cnpj, '^[0-9]{14}$')),
    CONSTRAINT ck_empresa_ativa CHECK (ativa IN (0, 1))
);

-- Criação de índice para busca por CNPJ
CREATE INDEX idx_empresa_cnpj ON empresa(cnpj);

-- Criação de índice para busca por razão social
CREATE INDEX idx_empresa_razao_social ON empresa(razao_social);

-- Comentários na tabela e colunas
COMMENT ON TABLE empresa IS 'Tabela de empresas cadastradas no sistema';
COMMENT ON COLUMN empresa.id IS 'Identificador único da empresa';
COMMENT ON COLUMN empresa.cnpj IS 'CNPJ da empresa - 14 dígitos numéricos sem formatação (formato legado)';
COMMENT ON COLUMN empresa.razao_social IS 'Razão social da empresa';
COMMENT ON COLUMN empresa.nome_fantasia IS 'Nome fantasia da empresa';
COMMENT ON COLUMN empresa.email IS 'Email de contato da empresa';
COMMENT ON COLUMN empresa.telefone IS 'Telefone de contato da empresa';
COMMENT ON COLUMN empresa.ativa IS 'Indica se a empresa está ativa (1) ou inativa (0)';
COMMENT ON COLUMN empresa.data_cadastro IS 'Data e hora do cadastro da empresa';