package br.com.flowtechsolutions.configuration;

import br.com.flowtechsolutions.core.dataprovider.DatabaseProvider;
import br.com.flowtechsolutions.dataproviders.database.InMemoryDatabaseProvider;
import br.com.flowtechsolutions.dataproviders.database.repository.InMemoryEntityRepository;
import br.com.flowtechsolutions.dataproviders.database.repository.InMemoryEntityRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseProviderConfiguration {
    @Bean
    InMemoryEntityRepository inMemoryEntityRepository() {
        return new InMemoryEntityRepositoryImpl();
    }

    @Bean
    DatabaseProvider databaseProvider(final InMemoryEntityRepository inMemoryEntityRepository) {
        return new InMemoryDatabaseProvider(inMemoryEntityRepository);
    }
}