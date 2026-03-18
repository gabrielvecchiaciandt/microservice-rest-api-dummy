package br.com.flowtechsolutions.core.dataprovider;

import br.com.flowtechsolutions.core.entity.BusinessEntity;
import java.util.Optional;

public interface DatabaseProvider {
    void save(BusinessEntity businessEntity);

    Optional<BusinessEntity> findByName(String name);
}