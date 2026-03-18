package br.com.flowtechsolutions.core.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.flowtechsolutions.core.dataprovider.DatabaseProvider;
import br.com.flowtechsolutions.core.entity.BusinessEntity;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BusinessUseCaseTest {
    private static final String NAME = "somename";
    private static final BusinessEntity BUSINESS_ENTITY = new BusinessEntity(null, NAME);

    private final BusinessUseCase subject = new BusinessUseCase(new FakeDatabaseProvider());

    @Test
    @DisplayName("Deve criar entidade.")
    void shouldCreateEntity() {
        assertDoesNotThrow(() -> subject.create(BUSINESS_ENTITY));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar entidade.")
    void shouldThrowExceptionCreateEntity() {
        final BusinessUseCase subject = new BusinessUseCase(null);

        assertThrows(Exception.class, () -> subject.create(BUSINESS_ENTITY));
    }

    @Test
    @DisplayName("Deve encontrar entidade pelo nome.")
    void shouldFindEntityByName() {
        final Optional<BusinessEntity> optionalBusinessEntity =
            subject.findByName(BUSINESS_ENTITY.name());

        assertTrue(optionalBusinessEntity.isPresent());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar encontrar entidade pelo nome.")
    void shouldThrowExceptionFindEntityByName() {
        final BusinessUseCase subject = new BusinessUseCase(null);

        assertThrows(Exception.class, () -> subject.findByName("name"));
    }

    private static class FakeDatabaseProvider implements DatabaseProvider {
        @Override
        public void save(final BusinessEntity businessEntity) {
        }

        @Override
        public Optional<BusinessEntity> findByName(final String name) {
            return NAME.equals(name) ? Optional.of(BUSINESS_ENTITY) : Optional.empty();
        }
    }
}