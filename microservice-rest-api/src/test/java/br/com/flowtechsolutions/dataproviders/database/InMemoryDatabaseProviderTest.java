package br.com.flowtechsolutions.dataproviders.database;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.flowtechsolutions.core.entity.BusinessEntity;
import br.com.flowtechsolutions.dataproviders.database.repository.InMemoryEntityRepositoryImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryDatabaseProviderTest {
    private static final String NAME = "somename";
    private static final BusinessEntity BUSINESS_ENTITY = new BusinessEntity(null, NAME);

    private final InMemoryDatabaseProvider subject =
        new InMemoryDatabaseProvider(new InMemoryEntityRepositoryImpl());

    @Test
    @DisplayName("Deve salvar entidade.")
    void shouldSaveEntity() {
        assertDoesNotThrow(() -> subject.save(BUSINESS_ENTITY));
    }

    @Test
    @DisplayName("Deve encontrar entidade pelo nome.")
    void shouldFindEntityByName() {
        subject.save(BUSINESS_ENTITY);

        final Optional<BusinessEntity> optionalBusinessEntity =
            subject.findByName(BUSINESS_ENTITY.name());
        
        assertTrue(optionalBusinessEntity.isPresent());
    }

    @Test
    @DisplayName("Não deve encontrar entidade pelo nome.")
    void shouldNotFindEntityByName() {
        final Optional<BusinessEntity> optionalBusinessEntity = subject.findByName("anyThingElse");

        assertTrue(optionalBusinessEntity.isEmpty());
    }
}
