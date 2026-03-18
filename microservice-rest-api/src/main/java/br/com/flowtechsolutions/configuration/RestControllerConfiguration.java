package br.com.flowtechsolutions.configuration;

import br.com.flowtechsolutions.core.usecase.BusinessUseCase;
import br.com.flowtechsolutions.entrypoints.rest.RestApiController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestControllerConfiguration {
    @Bean
    RestApiController restApiController(final BusinessUseCase businessUseCase) {
        return new RestApiController(businessUseCase);
    }
}