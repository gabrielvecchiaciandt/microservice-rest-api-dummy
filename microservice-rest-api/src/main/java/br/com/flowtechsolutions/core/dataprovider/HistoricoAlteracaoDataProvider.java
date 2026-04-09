package br.com.flowtechsolutions.core.dataprovider;

import br.com.flowtechsolutions.core.entity.HistoricoAlteracao;

import java.util.List;

/**
 * Interface de contrato para operações de persistência de Histórico de Alterações.
 * Esta interface define o contrato entre a camada Core e a camada de Data Providers,
 * seguindo os princípios da Arquitetura Hexagonal.
 */
public interface HistoricoAlteracaoDataProvider {
    
    /**
     * Registra uma nova alteração no histórico.
     * Utilizado para fins de auditoria e rastreabilidade de todas as operações
     * realizadas em produtos (CREATE, UPDATE, DELETE).
     *
     * @param historico o registro de histórico a ser salvo
     * @return o histórico salvo com ID gerado automaticamente
     */
    HistoricoAlteracao registrar(HistoricoAlteracao historico);
    
    /**
     * Busca todo o histórico de alterações de um produto específico.
     * Os registros são ordenados do mais recente para o mais antigo (dataHora DESC).
     * Esta ordenação facilita a visualização das alterações mais recentes primeiro.
     *
     * @param produtoId o identificador do produto
     * @return lista ordenada de alterações do produto (pode estar vazia)
     */
    List<HistoricoAlteracao> buscarPorProdutoId(Long produtoId);
}