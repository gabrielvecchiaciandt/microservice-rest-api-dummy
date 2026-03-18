package br.com.flowtechsolutions.configuration;

import br.com.flowtechsolutions.core.dataprovider.DatabaseProvider;
import br.com.flowtechsolutions.core.usecase.BusinessUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessUseCaseConfiguration {
    @Bean
    BusinessUseCase businessUseCase(final DatabaseProvider databaseProvider) {
        return new BusinessUseCase(databaseProvider);
    }
}