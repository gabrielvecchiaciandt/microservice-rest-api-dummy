package br.com.flowtechsolutions.dataproviders.database;

import br.com.flowtechsolutions.core.dataprovider.DatabaseProvider;
import br.com.flowtechsolutions.core.entity.BusinessEntity;
import br.com.flowtechsolutions.dataproviders.database.entity.PersistentEntity;
import br.com.flowtechsolutions.dataproviders.database.repository.InMemoryEntityRepository;
import java.util.Optional;

public class InMemoryDatabaseProvider implements DatabaseProvider {
    private final InMemoryEntityRepository entityRepository;

    public InMemoryDatabaseProvider(final InMemoryEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public void save(final BusinessEntity businessEntity) {
        entityRepository.save(PersistentEntity.from(businessEntity));
    }

    @Override
    public Optional<BusinessEntity> findByName(final String name) {
        return PersistentEntity.toBusiness(entityRepository.findByName(name));
    }
}