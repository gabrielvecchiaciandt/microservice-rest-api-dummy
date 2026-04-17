-- Migration V2: Criação da tabela PRODUTO
-- Oracle Database
-- Suporta apenas CNPJ numérico (14 dígitos) para fornecedor - formato legado

-- Criação da sequence para geração de IDs
CREATE SEQUENCE produto_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Criação da tabela PRODUTO
CREATE TABLE produto (
    id NUMBER(19) NOT NULL,
    nome VARCHAR2(100) NOT NULL,
    descricao VARCHAR2(500),
    preco NUMBER(10, 2) NOT NULL,
    categoria VARCHAR2(20) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL,
    cnpj_fornecedor VARCHAR2(14),
    CONSTRAINT pk_produto PRIMARY KEY (id),
    CONSTRAINT ck_produto_preco_positivo CHECK (preco >= 0),
    CONSTRAINT ck_produto_categoria CHECK (categoria IN ('INFORMATICA', 'ELETRONICOS', 'MOVEIS', 'LIVROS', 'OUTROS')),
    CONSTRAINT ck_produto_cnpj_numerico CHECK (cnpj_fornecedor IS NULL OR REGEXP_LIKE(cnpj_fornecedor, '^[0-9]{14}$'))
);

-- Criação de índice para busca por nome
CREATE INDEX idx_produto_nome ON produto(nome);

-- Criação de índice para busca por categoria
CREATE INDEX idx_produto_categoria ON produto(categoria);

-- Criação de índice para busca por CNPJ do fornecedor
CREATE INDEX idx_produto_cnpj_fornecedor ON produto(cnpj_fornecedor);

-- Comentários na tabela e colunas
COMMENT ON TABLE produto IS 'Tabela de produtos do catálogo';
COMMENT ON COLUMN produto.id IS 'Identificador único do produto';
COMMENT ON COLUMN produto.nome IS 'Nome do produto';
COMMENT ON COLUMN produto.descricao IS 'Descrição detalhada do produto';
COMMENT ON COLUMN produto.preco IS 'Preço do produto em reais';
COMMENT ON COLUMN produto.categoria IS 'Categoria do produto (INFORMATICA, ELETRONICOS, MOVEIS, LIVROS, OUTROS)';
COMMENT ON COLUMN produto.data_cadastro IS 'Data e hora do cadastro do produto';
COMMENT ON COLUMN produto.cnpj_fornecedor IS 'CNPJ do fornecedor - 14 dígitos numéricos sem formatação (formato legado)';