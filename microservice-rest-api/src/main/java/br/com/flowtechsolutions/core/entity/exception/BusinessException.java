package br.com.flowtechsolutions.core.entity.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(final Throwable cause) {
        super(cause);
    }
}