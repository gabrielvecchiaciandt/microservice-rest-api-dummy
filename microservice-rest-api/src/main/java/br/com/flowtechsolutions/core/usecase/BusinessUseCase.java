package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.DatabaseProvider;
import br.com.flowtechsolutions.core.entity.BusinessEntity;
import br.com.flowtechsolutions.core.entity.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BusinessUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessUseCase.class);

    private final DatabaseProvider databaseProvider;

    public BusinessUseCase(final DatabaseProvider databaseProvider) {
        this.databaseProvider = databaseProvider;
    }

    public void create(final BusinessEntity businessEntity) {
        //TODO Aqui sua regra de negócio
        LOGGER.info("Creating business entity with name = {}", businessEntity.name());
        try {
            databaseProvider.save(businessEntity);
        } catch (final Exception e) {
            throw new BusinessException(e);
        }
    }

    public Optional<BusinessEntity> findByName(final String name) {
        //TODO Aqui sua regra de negócio
        try {
            return databaseProvider.findByName(name);
        } catch (final Exception e) {
            throw new BusinessException(e);
        }
    }
}