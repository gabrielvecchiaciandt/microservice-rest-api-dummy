package br.com.flowtechsolutions.database.repository;

import br.com.flowtechsolutions.database.entity.HistoricoAlteracaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository Spring Data JPA para operações de persistência de Histórico de Alterações.
 * Fornece métodos CRUD padrão e consultas customizadas.
 */
@Repository
public interface HistoricoAlteracaoRepository extends JpaRepository<HistoricoAlteracaoEntity, Long> {
    
    /**
     * Busca todos os registros de histórico de um produto específico,
     * ordenados por data/hora em ordem decrescente (mais recente primeiro).
     *
     * @param produtoId o identificador do produto
     * @return lista ordenada de registros de histórico
     */
    List<HistoricoAlteracaoEntity> findByProdutoIdOrderByDataHoraDesc(Long produtoId);
}