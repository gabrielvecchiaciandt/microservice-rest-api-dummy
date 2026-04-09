package br.com.flowtechsolutions.core.entity;

import br.com.flowtechsolutions.core.entity.constants.TipoOperacao;

import java.time.LocalDateTime;

/**
 * Entidade de domínio representando um Histórico de Alteração.
 * Registra todas as operações realizadas em produtos para fins de auditoria e rastreabilidade.
 * Esta é uma entidade da camada Core e não deve conter anotações de frameworks.
 */
public record HistoricoAlteracao(
    Long id,
    Long produtoId,
    TipoOperacao tipoOperacao,
    LocalDateTime dataHora,
    String dadosAnteriores,
    String dadosNovos
) {
    /**
     * Construtor para criação de novo histórico (sem ID e com dataHora atual).
     * Utilizado quando uma nova alteração está sendo registrada.
     *
     * @param produtoId o identificador do produto alterado
     * @param tipoOperacao o tipo de operação realizada (CREATE, UPDATE, DELETE)
     * @param dadosAnteriores dados anteriores do produto em formato JSON (null para CREATE)
     * @param dadosNovos dados novos do produto em formato JSON (null para DELETE)
     */
    public HistoricoAlteracao(Long produtoId, TipoOperacao tipoOperacao, String dadosAnteriores, String dadosNovos) {
        this(null, produtoId, tipoOperacao, LocalDateTime.now(), dadosAnteriores, dadosNovos);
    }
    
    /**
     * Cria uma nova instância do histórico com ID preenchido.
     * Utilizado após a persistência do histórico.
     *
     * @param id o ID gerado automaticamente
     * @return nova instância do histórico com ID
     */
    public HistoricoAlteracao comId(Long id) {
        return new HistoricoAlteracao(id, this.produtoId, this.tipoOperacao, this.dataHora, this.dadosAnteriores, this.dadosNovos);
    }
}