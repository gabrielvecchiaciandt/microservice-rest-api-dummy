package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.HistoricoAlteracaoDataProvider;
import br.com.flowtechsolutions.core.dataprovider.ProdutoDataProvider;
import br.com.flowtechsolutions.core.entity.HistoricoAlteracao;
import br.com.flowtechsolutions.core.entity.Produto;
import br.com.flowtechsolutions.core.entity.constants.TipoOperacao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Use Case para criação de novo produto.
 * Implementa a lógica de negócio para cadastrar um produto no sistema,
 * incluindo validações e registro no histórico.
 */
public class CriarProdutoUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(CriarProdutoUseCase.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private final ProdutoDataProvider produtoDataProvider;
    private final HistoricoAlteracaoDataProvider historicoDataProvider;
    
    /**
     * Construtor com injeção de dependências.
     *
     * @param produtoDataProvider provedor de dados de produtos
     * @param historicoDataProvider provedor de dados de histórico
     */
    public CriarProdutoUseCase(ProdutoDataProvider produtoDataProvider, 
                               HistoricoAlteracaoDataProvider historicoDataProvider) {
        this.produtoDataProvider = produtoDataProvider;
        this.historicoDataProvider = historicoDataProvider;
    }
    
    /**
     * Cria um novo produto no sistema.
     * Valida os dados, persiste o produto e registra a operação no histórico.
     *
     * @param produto dados do produto a ser criado (sem ID e dataCadastro)
     * @return o produto criado com ID e dataCadastro preenchidos
     * @throws IllegalArgumentException se os dados do produto forem inválidos
     */
    public Produto executar(Produto produto) {
        logger.debug("Iniciando criação de produto: {}", produto.nome());
        
        produto.validar();
        
        Produto produtoComDataCadastro = produto.comIdEDataCadastro(null, LocalDateTime.now());
        Produto produtoSalvo = produtoDataProvider.salvar(produtoComDataCadastro);
        
        registrarHistorico(produtoSalvo);
        
        logger.info("Produto criado com sucesso: ID {}", produtoSalvo.id());
        return produtoSalvo;
    }
    
    /**
     * Registra a operação de criação no histórico.
     * Em caso de falha, apenas loga o erro sem interromper o fluxo principal.
     *
     * @param produto o produto criado
     */
    private void registrarHistorico(Produto produto) {
        try {
            String dadosNovos = serializarProduto(produto);
            HistoricoAlteracao historico = new HistoricoAlteracao(
                produto.id(),
                TipoOperacao.CREATE,
                null,
                dadosNovos
            );
            historicoDataProvider.registrar(historico);
            logger.debug("Histórico de criação registrado para produto ID {}", produto.id());
        } catch (Exception e) {
            logger.error("Erro ao registrar histórico de criação do produto ID {}", produto.id(), e);
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