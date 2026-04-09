package br.com.flowtechsolutions.database.repository;

import br.com.flowtechsolutions.database.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data JPA para operações de persistência de Empresa.
 * O CNPJ é tratado como string de 14 dígitos numéricos (formato legado).
 */
@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long> {

    /**
     * Busca empresa pelo CNPJ (14 dígitos numéricos sem formatação).
     *
     * @param cnpj os 14 dígitos do CNPJ
     * @return Optional com a empresa se encontrada
     */
    Optional<EmpresaEntity> findByCnpj(String cnpj);

    /**
     * Verifica se existe empresa com o CNPJ informado.
     *
     * @param cnpj os 14 dígitos do CNPJ
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
