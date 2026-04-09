package br.com.flowtechsolutions.core.entity.constants;

/**
 * Enum representando os tipos de operação registrados no histórico de alterações.
 * Cada operação realizada em um produto é classificada em um desses tipos.
 */
public enum TipoOperacao {
    /**
     * Operação de criação de um novo produto
     */
    CREATE,
    
    /**
     * Operação de atualização de um produto existente
     */
    UPDATE,
    
    /**
     * Operação de remoção de um produto
     */
    DELETE
}