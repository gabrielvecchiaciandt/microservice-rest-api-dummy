package br.com.flowtechsolutions.dataproviders.database.entity;

import br.com.flowtechsolutions.core.entity.constants.CategoriaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade JPA para persistência de Produto.
 * Representa a tabela 'produto' no banco de dados.
 */
@Entity
@Table(name = "produto")
public class ProdutoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    
    @Column(name = "descricao", length = 500)
    private String descricao;
    
    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 20)
    private CategoriaEnum categoria;
    
    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    /** CNPJ do fornecedor — 14 dígitos numéricos sem formatação (formato legado pré-2026). Nullable. */
    @Column(name = "cnpj_fornecedor", length = 14)
    private String cnpjFornecedor;

    /**
     * Construtor padrão necessário para o JPA.
     */
    public ProdutoEntity() {
    }

    /**
     * Construtor com todos os campos.
     */
    public ProdutoEntity(Long id, String nome, String descricao, BigDecimal preco,
                         CategoriaEnum categoria, LocalDateTime dataCadastro, String cnpjFornecedor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.dataCadastro = dataCadastro;
        this.cnpjFornecedor = cnpjFornecedor;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getPreco() {
        return preco;
    }
    
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
    
    public CategoriaEnum getCategoria() {
        return categoria;
    }
    
    public void setCategoria(CategoriaEnum categoria) {
        this.categoria = categoria;
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoEntity that = (ProdutoEntity) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "ProdutoEntity{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", categoria=" + categoria +
                ", dataCadastro=" + dataCadastro +
                '}';
    }
}