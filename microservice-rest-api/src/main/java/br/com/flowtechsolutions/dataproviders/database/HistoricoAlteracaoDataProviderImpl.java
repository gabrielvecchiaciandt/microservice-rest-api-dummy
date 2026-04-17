package br.com.flowtechsolutions.dataproviders.database;

import br.com.flowtechsolutions.core.dataprovider.HistoricoAlteracaoDataProvider;
import br.com.flowtechsolutions.core.entity.HistoricoAlteracao;
import br.com.flowtechsolutions.dataproviders.database.entity.HistoricoAlteracaoEntity;
import br.com.flowtechsolutions.dataproviders.database.repository.HistoricoAlteracaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do HistoricoAlteracaoDataProvider usando Spring Data JPA.
 * Responsável por converter entre entidades de domínio e entidades JPA,
 * e executar operações de persistência de histórico no banco de dados.
 */
public class HistoricoAlteracaoDataProviderImpl implements HistoricoAlteracaoDataProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(HistoricoAlteracaoDataProviderImpl.class);
    
    private final HistoricoAlteracaoRepository historicoRepository;
    
    /**
     * Construtor com injeção de dependência do repository.
     *
     * @param historicoRepository o repository JPA de histórico
     */
    public HistoricoAlteracaoDataProviderImpl(HistoricoAlteracaoRepository historicoRepository) {
        this.historicoRepository = historicoRepository;
    }
    
    @Override
    public HistoricoAlteracao registrar(HistoricoAlteracao historico) {
        logger.debug("Registrando histórico de alteração para produto ID: {}", historico.produtoId());
        
        HistoricoAlteracaoEntity entity = toEntity(historico);
        HistoricoAlteracaoEntity entitySalva = historicoRepository.save(entity);
        
        return toDomain(entitySalva);
    }
    
    @Override
    public List<HistoricoAlteracao> buscarPorProdutoId(Long produtoId) {
        logger.debug("Buscando histórico para produto ID: {}", produtoId);
        
        return historicoRepository.findByProdutoIdOrderByDataHoraDesc(produtoId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    /**
     * Converte entidade de domínio para entidade JPA.
     *
     * @param historico a entidade de domínio
     * @return a entidade JPA
     */
    private HistoricoAlteracaoEntity toEntity(HistoricoAlteracao historico) {
        return new HistoricoAlteracaoEntity(
            historico.id(),
            historico.produtoId(),
            historico.tipoOperacao(),
            historico.dataHora(),
            historico.dadosAnteriores(),
            historico.dadosNovos()
        );
    }
    
    /**
     * Converte entidade JPA para entidade de domínio.
     *
     * @param entity a entidade JPA
     * @return a entidade de domínio
     */
    private HistoricoAlteracao toDomain(HistoricoAlteracaoEntity entity) {
        return new HistoricoAlteracao(
            entity.getId(),
            entity.getProdutoId(),
            entity.getTipoOperacao(),
            entity.getDataHora(),
            entity.getDadosAnteriores(),
            entity.getDadosNovos()
        );
    }
}