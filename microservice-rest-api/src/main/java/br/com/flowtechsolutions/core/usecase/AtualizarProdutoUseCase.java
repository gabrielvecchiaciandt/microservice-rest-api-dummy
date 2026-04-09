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
 * Use Case para atualização de produto existente.
 * Implementa a lógica de negócio para atualizar os dados de um produto,
 * preservando ID e dataCadastro originais, e registrando a operação no histórico.
 */
public class AtualizarProdutoUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(AtualizarProdutoUseCase.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private final ProdutoDataProvider produtoDataProvider;
    private final HistoricoAlteracaoDataProvider historicoDataProvider;
    
    /**
     * Construtor com injeção de dependências.
     *
     * @param produtoDataProvider provedor de dados de produtos
     * @param historicoDataProvider provedor de dados de histórico
     */
    public AtualizarProdutoUseCase(ProdutoDataProvider produtoDataProvider,
                                   HistoricoAlteracaoDataProvider historicoDataProvider) {
        this.produtoDataProvider = produtoDataProvider;
        this.historicoDataProvider = historicoDataProvider;
    }
    
    /**
     * Atualiza os dados de um produto existente.
     * ID e dataCadastro não podem ser alterados.
     *
     * @param id o identificador do produto a ser atualizado
     * @param produtoAtualizado dados atualizados do produto
     * @return o produto atualizado
     * @throws ProdutoNaoEncontradoException se o produto não existir
     * @throws IllegalArgumentException se os dados do produto forem inválidos
     */
    public Produto executar(Long id, Produto produtoAtualizado) {
        logger.debug("Iniciando atualização do produto ID: {}", id);
        
        Produto produtoExistente = produtoDataProvider.buscarPorId(id)
            .orElseThrow(() -> {
                logger.warn("Produto com ID {} não encontrado para atualização", id);
                return new ProdutoNaoEncontradoException(id);
            });
        
        produtoAtualizado.validar();
        
        Produto produtoParaSalvar = new Produto(
            id,
            produtoAtualizado.nome(),
            produtoAtualizado.descricao(),
            produtoAtualizado.preco(),
            produtoAtualizado.categoria(),
            produtoExistente.dataCadastro(),
            produtoAtualizado.cnpjFornecedor() != null
                ? produtoAtualizado.cnpjFornecedor()
                : produtoExistente.cnpjFornecedor()
        );
        
        Produto produtoSalvo = produtoDataProvider.salvar(produtoParaSalvar);
        
        registrarHistorico(produtoExistente, produtoSalvo);
        
        logger.info("Produto ID {} atualizado com sucesso", id);
        return produtoSalvo;
    }
    
    /**
     * Registra a operação de atualização no histórico.
     * Em caso de falha, apenas loga o erro sem interromper o fluxo principal.
     *
     * @param produtoAntigo o produto antes da atualização
     * @param produtoNovo o produto após a atualização
     */
    private void registrarHistorico(Produto produtoAntigo, Produto produtoNovo) {
        try {
            String dadosAnteriores = serializarProduto(produtoAntigo);
            String dadosNovos = serializarProduto(produtoNovo);
            
            HistoricoAlteracao historico = new HistoricoAlteracao(
                produtoNovo.id(),
                TipoOperacao.UPDATE,
                dadosAnteriores,
                dadosNovos
            );
            historicoDataProvider.registrar(historico);
            logger.debug("Histórico de atualização registrado para produto ID {}", produtoNovo.id());
        } catch (Exception e) {
            logger.error("Erro ao registrar histórico de atualização do produto ID {}", produtoNovo.id(), e);
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