package br.com.flowtechsolutions.rest.handler;

import br.com.flowtechsolutions.core.entity.exception.ProdutoNaoEncontradoException;
import br.com.flowtechsolutions.api.dto.ErrorResponse;
import br.com.flowtechsolutions.api.dto.ErrorResponseErrorsInner;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler centralizado para tratamento de exceções da aplicação.
 * Captura exceções e as transforma em respostas HTTP apropriadas.
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
    
    /**
     * Trata exceções de produto não encontrado.
     *
     * @param ex a exceção capturada
     * @param request a requisição HTTP
     * @return resposta com status 404 Not Found
     */
    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProdutoNaoEncontrado(
            ProdutoNaoEncontradoException ex, HttpServletRequest request) {
        
        logger.warn("Produto não encontrado: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    /**
     * Trata exceções de argumentos inválidos (falhas de validação de Bean Validation).
     *
     * @param ex a exceção capturada
     * @param request a requisição HTTP
     * @return resposta com status 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        logger.warn("Erro de validação: {}", ex.getMessage());
        
        List<ErrorResponseErrorsInner> errors = new ArrayList<>();
        
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            ErrorResponseErrorsInner error = new ErrorResponseErrorsInner();
            error.setField(fieldError.getField());
            error.setMessage(fieldError.getDefaultMessage());
            errors.add(error);
        }
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage("Erro de validação");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setErrors(errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    /**
     * Trata exceções de argumentos ilegais (validações de negócio).
     *
     * @param ex a exceção capturada
     * @param request a requisição HTTP
     * @return resposta com status 400 Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        
        logger.warn("Argumento ilegal: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    /**
     * Trata exceções genéricas não capturadas por outros handlers.
     *
     * @param ex a exceção capturada
     * @param request a requisição HTTP
     * @return resposta com status 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        logger.error("Erro interno do servidor: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponse.setMessage("Erro interno do servidor");
        errorResponse.setPath(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}