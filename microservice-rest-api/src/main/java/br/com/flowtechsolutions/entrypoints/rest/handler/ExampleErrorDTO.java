package br.com.flowtechsolutions.entrypoints.rest.handler;

import java.time.LocalDateTime;
import java.util.List;

//Exemplo de um DTO de erro
public record ExampleErrorDTO(int statusCode, LocalDateTime localDateTime, List<String> messages) {
}