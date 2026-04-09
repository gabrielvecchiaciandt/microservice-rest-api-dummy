package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.ProdutoDataProvider;
import br.com.flowtechsolutions.core.entity.Produto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Use Case para listagem de todos os produtos.
 * Implementa a lógica de negócio para recuperar todos os produtos cadastrados.
 */
public class ListarProdutosUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ListarProdutosUseCase.class);
    
    private final ProdutoDataProvider produtoDataProvider;
    
    /**
     * Construtor com injeção de dependências.
     *
     * @param produtoDataProvider provedor de dados de produtos
     */
    public ListarProdutosUseCase(ProdutoDataProvider produtoDataProvider) {
        this.produtoDataProvider = produtoDataProvider;
    }
    
    /**
     * Lista todos os produtos cadastrados no sistema.
     * Os produtos são retornados ordenados por data de cadastro (mais recente primeiro).
     *
     * @return lista de produtos (pode estar vazia se não houver produtos cadastrados)
     */
    public List<Produto> executar() {
        logger.debug("Listando todos os produtos");
        
        List<Produto> produtos = produtoDataProvider.listarTodos();
        
        logger.info("Total de produtos encontrados: {}", produtos.size());
        return produtos;
    }
}