package br.com.flowtechsolutions.core.entity.exception;

/**
 * Exceção lançada quando um CNPJ no formato numérico legado é inválido.
 */
public class CnpjInvalidoException extends RuntimeException {

    public CnpjInvalidoException(String mensagem) {
        super(mensagem);
    }
}
