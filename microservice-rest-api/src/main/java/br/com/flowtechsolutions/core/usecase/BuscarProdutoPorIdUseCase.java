package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.ProdutoDataProvider;
import br.com.flowtechsolutions.core.entity.Produto;
import br.com.flowtechsolutions.core.entity.exception.ProdutoNaoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use Case para busca de produto por ID.
 * Implementa a lógica de negócio para recuperar um produto específico.
 */
public class BuscarProdutoPorIdUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(BuscarProdutoPorIdUseCase.class);
    
    private final ProdutoDataProvider produtoDataProvider;
    
    /**
     * Construtor com injeção de dependências.
     *
     * @param produtoDataProvider provedor de dados de produtos
     */
    public BuscarProdutoPorIdUseCase(ProdutoDataProvider produtoDataProvider) {
        this.produtoDataProvider = produtoDataProvider;
    }
    
    /**
     * Busca um produto pelo seu identificador único.
     *
     * @param id o identificador do produto
     * @return o produto encontrado
     * @throws ProdutoNaoEncontradoException se o produto não existir
     */
    public Produto executar(Long id) {
        logger.debug("Buscando produto por ID: {}", id);
        
        return produtoDataProvider.buscarPorId(id)
            .orElseThrow(() -> {
                logger.warn("Produto com ID {} não encontrado", id);
                return new ProdutoNaoEncontradoException(id);
            });
    }
}