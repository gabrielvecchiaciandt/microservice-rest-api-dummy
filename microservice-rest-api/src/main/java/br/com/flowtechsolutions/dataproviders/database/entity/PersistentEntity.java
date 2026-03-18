package br.com.flowtechsolutions.dataproviders.database.entity;

import br.com.flowtechsolutions.core.entity.BusinessEntity;
import java.util.Optional;

public class PersistentEntity {
    private Long id;
    private final String name;

    public PersistentEntity(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PersistentEntity from(
        final BusinessEntity persistentEntity) {
        return new PersistentEntity(persistentEntity.name());
    }

    public static Optional<BusinessEntity> toBusiness(
        final Optional<PersistentEntity> optionalPersistentEntity) {
        return optionalPersistentEntity
            .map(persistentEntity -> new BusinessEntity(persistentEntity.getId(),
                persistentEntity.getName()));
    }
}
