package br.com.flowtechsolutions.dataproviders.database.entity;

import br.com.flowtechsolutions.core.entity.constants.TipoOperacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade JPA para persistência de Histórico de Alterações.
 * Representa a tabela 'historico_alteracao' no banco de dados.
 */
@Entity
@Table(name = "historico_alteracao")
public class HistoricoAlteracaoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "produto_id", nullable = false)
    private Long produtoId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacao", nullable = false, length = 20)
    private TipoOperacao tipoOperacao;
    
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
    
    @Column(name = "dados_anteriores", columnDefinition = "TEXT")
    private String dadosAnteriores;
    
    @Column(name = "dados_novos", columnDefinition = "TEXT")
    private String dadosNovos;
    
    /**
     * Construtor padrão necessário para o JPA.
     */
    public HistoricoAlteracaoEntity() {
    }
    
    /**
     * Construtor com todos os campos.
     *
     * @param id o identificador único do histórico
     * @param produtoId o identificador do produto alterado
     * @param tipoOperacao o tipo de operação (CREATE, UPDATE, DELETE)
     * @param dataHora a data e hora da alteração
     * @param dadosAnteriores os dados anteriores em formato JSON
     * @param dadosNovos os dados novos em formato JSON
     */
    public HistoricoAlteracaoEntity(Long id, Long produtoId, TipoOperacao tipoOperacao,
                                    LocalDateTime dataHora, String dadosAnteriores, String dadosNovos) {
        this.id = id;
        this.produtoId = produtoId;
        this.tipoOperacao = tipoOperacao;
        this.dataHora = dataHora;
        this.dadosAnteriores = dadosAnteriores;
        this.dadosNovos = dadosNovos;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getProdutoId() {
        return produtoId;
    }
    
    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
    
    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }
    
    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }
    
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    
    public String getDadosAnteriores() {
        return dadosAnteriores;
    }
    
    public void setDadosAnteriores(String dadosAnteriores) {
        this.dadosAnteriores = dadosAnteriores;
    }
    
    public String getDadosNovos() {
        return dadosNovos;
    }
    
    public void setDadosNovos(String dadosNovos) {
        this.dadosNovos = dadosNovos;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoAlteracaoEntity that = (HistoricoAlteracaoEntity) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "HistoricoAlteracaoEntity{" +
                "id=" + id +
                ", produtoId=" + produtoId +
                ", tipoOperacao=" + tipoOperacao +
                ", dataHora=" + dataHora +
                '}';
    }
}