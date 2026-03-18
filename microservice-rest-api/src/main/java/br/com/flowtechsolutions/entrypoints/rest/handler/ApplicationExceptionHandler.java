package br.com.flowtechsolutions.entrypoints.rest.handler;

import br.com.flowtechsolutions.core.entity.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//TODO Aqui você pode colocar o tratamento das exceções lançadas na camada core da aplicação, por exemplo
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExampleErrorDTO> businessExceptionHandle(
        final BusinessException businessException) {
        return ResponseEntity.unprocessableEntity()
            .body(createErrorDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), businessException));
    }

    private static ExampleErrorDTO createErrorDTO(final int statusCode,
                                                  final BusinessException businessException) {
        return new ExampleErrorDTO(statusCode, LocalDateTime.now(),
            List.of(businessException.getMessage()));
    }
}