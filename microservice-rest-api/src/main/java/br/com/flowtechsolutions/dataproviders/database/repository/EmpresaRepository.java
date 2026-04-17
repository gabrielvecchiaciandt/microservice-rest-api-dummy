package br.com.flowtechsolutions.dataproviders.database.repository;

import br.com.flowtechsolutions.dataproviders.database.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data JPA para operações de persistência de Empresa.
 * O CNPJ é tratado como string de 14 caracteres (formato alfanumérico a partir de 2026).
 */
@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long> {

    /**
     * Busca empresa pelo CNPJ (14 caracteres alfanuméricos sem formatação).
     *
     * @param cnpj os 14 caracteres do CNPJ
     * @return Optional com a empresa se encontrada
     */
    Optional<EmpresaEntity> findByCnpj(String cnpj);

    /**
     * Verifica se existe empresa com o CNPJ informado.
     *
     * @param cnpj os 14 caracteres do CNPJ
     * @return true se existe
     */
    boolean existsByCnpj(String cnpj);

    /**
     * Lista apenas as empresas ativas.
     *
     * @return lista de empresas ativas
     */
    List<EmpresaEntity> findByAtivaTrue();
}
