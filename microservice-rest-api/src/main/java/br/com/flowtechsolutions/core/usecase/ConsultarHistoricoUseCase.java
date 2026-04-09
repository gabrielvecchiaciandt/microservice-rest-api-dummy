package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.HistoricoAlteracaoDataProvider;
import br.com.flowtechsolutions.core.dataprovider.ProdutoDataProvider;
import br.com.flowtechsolutions.core.entity.HistoricoAlteracao;
import br.com.flowtechsolutions.core.entity.exception.ProdutoNaoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Use Case para consulta de histórico de alterações.
 * Implementa a lógica de negócio para recuperar o histórico completo
 * de alterações de um produto específico.
 */
public class ConsultarHistoricoUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ConsultarHistoricoUseCase.class);
    
    private final ProdutoDataProvider produtoDataProvider;
    private final HistoricoAlteracaoDataProvider historicoDataProvider;
    
    /**
     * Construtor com injeção de dependências.
     *
     * @param produtoDataProvider provedor de dados de produtos
     * @param historicoDataProvider provedor de dados de histórico
     */
    public ConsultarHistoricoUseCase(ProdutoDataProvider produtoDataProvider,
                                     HistoricoAlteracaoDataProvider historicoDataProvider) {
        this.produtoDataProvider = produtoDataProvider;
        this.historicoDataProvider = historicoDataProvider;
    }
    
    /**
     * Consulta o histórico completo de alterações de um produto.
     * Verifica primeiro se o produto existe antes de buscar o histórico.
     *
     * @param produtoId o identificador do produto
     * @return lista ordenada de alterações (mais recente primeiro)
     * @throws ProdutoNaoEncontradoException se o produto não existir
     */
    public List<HistoricoAlteracao> executar(Long produtoId) {
        logger.debug("Consultando histórico do produto ID: {}", produtoId);
        
        if (!produtoDataProvider.existePorId(produtoId)) {
            logger.warn("Produto com ID {} não encontrado para consulta de histórico", produtoId);
            throw new ProdutoNaoEncontradoException(produtoId);
        }
        
        List<HistoricoAlteracao> historico = historicoDataProvider.buscarPorProdutoId(produtoId);
        
        logger.info("Histórico do produto ID {} consultado: {} registros encontrados", produtoId, historico.size());
        return historico;
    }
}