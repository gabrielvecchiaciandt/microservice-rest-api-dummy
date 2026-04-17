-- Migration V3: Criação da tabela HISTORICO_ALTERACAO
-- Oracle Database
-- Tabela para auditoria de alterações em produtos

-- Criação da sequence para geração de IDs
CREATE SEQUENCE historico_alteracao_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Criação da tabela HISTORICO_ALTERACAO
CREATE TABLE historico_alteracao (
    id NUMBER(19) NOT NULL,
    produto_id NUMBER(19) NOT NULL,
    tipo_operacao VARCHAR2(20) NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    dados_anteriores CLOB,
    dados_novos CLOB,
    CONSTRAINT pk_historico_alteracao PRIMARY KEY (id),
    CONSTRAINT fk_historico_produto FOREIGN KEY (produto_id) REFERENCES produto(id),
    CONSTRAINT ck_historico_tipo_operacao CHECK (tipo_operacao IN ('CREATE', 'UPDATE', 'DELETE'))
);

-- Criação de índice para busca por produto
CREATE INDEX idx_historico_produto_id ON historico_alteracao(produto_id);

-- Criação de índice para busca por tipo de operação
CREATE INDEX idx_historico_tipo_operacao ON historico_alteracao(tipo_operacao);

-- Criação de índice para busca por data/hora
CREATE INDEX idx_historico_data_hora ON historico_alteracao(data_hora);

-- Criação de índice composto para consultas combinadas
CREATE INDEX idx_historico_produto_data ON historico_alteracao(produto_id, data_hora);

-- Comentários na tabela e colunas
COMMENT ON TABLE historico_alteracao IS 'Tabela de auditoria de alterações em produtos';
COMMENT ON COLUMN historico_alteracao.id IS 'Identificador único do histórico';
COMMENT ON COLUMN historico_alteracao.produto_id IS 'ID do produto que foi alterado';
COMMENT ON COLUMN historico_alteracao.tipo_operacao IS 'Tipo de operação realizada (CREATE, UPDATE, DELETE)';
COMMENT ON COLUMN historico_alteracao.data_hora IS 'Data e hora da operação';
COMMENT ON COLUMN historico_alteracao.dados_anteriores IS 'Estado anterior do produto em formato JSON';
COMMENT ON COLUMN historico_alteracao.dados_novos IS 'Novo estado do produto em formato JSON';