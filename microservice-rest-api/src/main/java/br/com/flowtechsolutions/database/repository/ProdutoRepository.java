package br.com.flowtechsolutions.database.repository;

import br.com.flowtechsolutions.database.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA para operações de persistência de Produto.
 * Fornece métodos CRUD padrão e consultas customizadas.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
    // Métodos herdados do JpaRepository:
    // - save(ProdutoEntity): salva ou atualiza um produto
    // - findById(Long): busca produto por ID
    // - findAll(): lista todos os produtos
    // - deleteById(Long): remove produto por ID
    // - existsById(Long): verifica se produto existe
    
    // Métodos customizados podem ser adicionados aqui se necessário no futuro
}