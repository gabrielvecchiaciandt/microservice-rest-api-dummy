package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.HistoricoAlteracaoDataProvider;
import br.com.flowtechsolutions.core.dataprovider.ProdutoDataProvider;
import br.com.flowtechsolutions.core.entity.HistoricoAlteracao;
import br.com.flowtechsolutions.core.entity.Produto;
import br.com.flowtechsolutions.core.entity.constants.TipoOperacao;
import br.com.flowtechsolutions.core.entity.exception.ProdutoNaoEncontradoException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use Case para remoção de produto.
 * Implementa a lógica de negócio para remover um produto do sistema,
 * registrando a operação no histórico antes da remoção.
 */
public class RemoverProdutoUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(RemoverProdutoUseCase.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private final ProdutoDataProvider produtoDataProvider;
    private final HistoricoAlteracaoDataProvider historicoDataProvider;
    
    /**
     * Construtor com injeção de dependências.
     *
     * @param produtoDataProvider provedor de dados de produtos
     * @param historicoDataProvider provedor de dados de histórico
     */
    public RemoverProdutoUseCase(ProdutoDataProvider produtoDataProvider,
                                 HistoricoAlteracaoDataProvider historicoDataProvider) {
        this.produtoDataProvider = produtoDataProvider;
        this.historicoDataProvider = historicoDataProvider;
    }
    
    /**
     * Remove um produto do sistema.
     * O histórico da operação é registrado antes da remoção.
     *
     * @param id o identificador do produto a ser removido
     * @throws ProdutoNaoEncontradoException se o produto não existir
     */
    public void executar(Long id) {
        logger.debug("Iniciando remoção do produto ID: {}", id);
        
        Produto produto = produtoDataProvider.buscarPorId(id)
            .orElseThrow(() -> {
                logger.warn("Produto com ID {} não encontrado para remoção", id);
                return new ProdutoNaoEncontradoException(id);
            });
        
        registrarHistorico(produto);
        
        produtoDataProvider.remover(id);
        
        logger.info("Produto ID {} removido com sucesso", id);
    }
    
    /**
     * Registra a operação de remoção no histórico.
     * Em caso de falha, apenas loga o erro sem interromper o fluxo principal.
     *
     * @param produto o produto a ser removido
     */
    private void registrarHistorico(Produto produto) {
        try {
            String dadosAnteriores = serializarProduto(produto);
            
            HistoricoAlteracao historico = new HistoricoAlteracao(
                produto.id(),
                TipoOperacao.DELETE,
                dadosAnteriores,
                null
            );
            historicoDataProvider.registrar(historico);
            logger.debug("Histórico de remoção registrado para produto ID {}", produto.id());
        } catch (Exception e) {
            logger.error("Erro ao registrar histórico de remoção do produto ID {}", produto.id(), e);
        }
    }
    
    /**
     * Serializa o produto para formato JSON.
     *
     * @param produto o produto a ser serializado
     * @return string JSON representando o produto
     */
    private String serializarProduto(Produto produto) {
        try {
            return objectMapper.writeValueAsString(produto);
        } catch (JsonProcessingException e) {
            logger.error("Erro ao serializar produto para JSON", e);
            return String.format("{\"id\":%d,\"nome\":\"%s\"}", produto.id(), produto.nome());
        }
    }
}