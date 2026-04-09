package br.com.flowtechsolutions.core.entity.exception;

/**
 * Exceção lançada quando uma empresa não é encontrada no sistema.
 */
public class EmpresaNaoEncontradaException extends RuntimeException {

    public EmpresaNaoEncontradaException(String cnpj) {
        super(String.format("Empresa com CNPJ %s não encontrada", cnpj));
    }

    public EmpresaNaoEncontradaException(Long id) {
        super(String.format("Empresa com ID %d não encontrada", id));
    }
}
