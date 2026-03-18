package br.com.flowtechsolutions.dataproviders.database.repository;

import br.com.flowtechsolutions.dataproviders.database.entity.PersistentEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface InMemoryEntityRepository {
    Map<String, PersistentEntity> ENTITY_BY_NAME = new HashMap<>();

    default void save(final PersistentEntity persistentEntity) {
        ENTITY_BY_NAME.put(persistentEntity.getName(), persistentEntity);
    }

    default Optional<PersistentEntity> findByName(final String name) {
        return Optional.ofNullable(ENTITY_BY_NAME.get(name));
    }
}