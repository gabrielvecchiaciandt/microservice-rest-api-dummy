package br.com.flowtechsolutions.core.entity.exception;

/**
 * Exceção lançada quando um produto não é encontrado no sistema.
 * Utilizada para indicar que uma operação não pode ser realizada
 * porque o produto especificado não existe.
 */
public class ProdutoNaoEncontradoException extends RuntimeException {
    
    /**
     * Construtor que recebe o ID do produto não encontrado.
     *
     * @param id o identificador do produto que não foi encontrado
     */
    public ProdutoNaoEncontradoException(Long id) {
        super(String.format("Produto com ID %d não encontrado", id));
    }
    
    /**
     * Construtor que recebe uma mensagem customizada.
     *
     * @param mensagem a mensagem de erro
     */
    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}