package br.com.flowtechsolutions.core.dataprovider;

import br.com.flowtechsolutions.core.entity.Produto;

import java.util.List;
import java.util.Optional;

/**
 * Interface de contrato para operações de persistência de Produto.
 * Esta interface define o contrato entre a camada Core e a camada de Data Providers,
 * seguindo os princípios da Arquitetura Hexagonal.
 */
public interface ProdutoDataProvider {
    
    /**
     * Salva um novo produto ou atualiza um existente.
     * Se o produto não possui ID, um novo registro será criado.
     * Se o produto possui ID, o registro existente será atualizado.
     *
     * @param produto o produto a ser salvo
     * @return o produto salvo com ID gerado (para novos produtos) ou atualizado
     */
    Produto salvar(Produto produto);
    
    /**
     * Busca um produto pelo seu identificador único.
     *
     * @param id o identificador do produto
     * @return Optional contendo o produto se encontrado, ou Optional.empty() caso contrário
     */
    Optional<Produto> buscarPorId(Long id);
    
    /**
     * Lista todos os produtos cadastrados no sistema.
     * Os produtos são ordenados por data de cadastro (mais recente primeiro).
     *
     * @return lista de todos os produtos (pode estar vazia)
     */
    List<Produto> listarTodos();
    
    /**
     * Remove um produto pelo seu identificador.
     * Se o produto não existir, nenhuma ação é realizada.
     *
     * @param id o identificador do produto a ser removido
     */
    void remover(Long id);
    
    /**
     * Verifica se existe um produto com o identificador especificado.
     *
     * @param id o identificador do produto
     * @return true se o produto existe, false caso contrário
     */
    boolean existePorId(Long id);
}