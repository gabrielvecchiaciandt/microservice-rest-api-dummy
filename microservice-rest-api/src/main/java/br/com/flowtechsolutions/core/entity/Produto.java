package br.com.flowtechsolutions.core.entity;

import br.com.flowtechsolutions.core.entity.constants.CategoriaEnum;
import br.com.flowtechsolutions.core.entity.value.Cnpj;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade de domínio representando um Produto.
 * Esta é uma entidade da camada Core e não deve conter anotações de frameworks.
 * Utiliza record para garantir imutabilidade e simplicidade.
 */
public record Produto(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    CategoriaEnum categoria,
    LocalDateTime dataCadastro,
    Cnpj cnpjFornecedor
) {
    /**
     * Construtor para criação de novo produto (sem ID e dataCadastro).
     * Utilizado quando um novo produto está sendo criado e ainda não foi persistido.
     *
     * @param nome o nome do produto
     * @param descricao a descrição do produto (pode ser null)
     * @param preco o preço do produto
     * @param categoria a categoria do produto
     */
    public Produto(String nome, String descricao, BigDecimal preco, CategoriaEnum categoria) {
        this(null, nome, descricao, preco, categoria, null, null);
    }

    /**
     * Construtor para criação de novo produto com CNPJ do fornecedor.
     *
     * @param nome o nome do produto
     * @param descricao a descrição do produto (pode ser null)
     * @param preco o preço do produto
     * @param categoria a categoria do produto
     * @param cnpjFornecedor CNPJ numérico do fornecedor (pode ser null)
     */
    public Produto(String nome, String descricao, BigDecimal preco, CategoriaEnum categoria, Cnpj cnpjFornecedor) {
        this(null, nome, descricao, preco, categoria, null, cnpjFornecedor);
    }
    
    /**
     * Valida os dados do produto conforme regras de negócio.
     * 
     * @throws IllegalArgumentException se os dados forem inválidos
     */
    public void validar() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (nome.trim().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter no mínimo 3 caracteres");
        }
        if (nome.trim().length() > 100) {
            throw new IllegalArgumentException("Nome deve ter no máximo 100 caracteres");
        }
        if (descricao != null && descricao.length() > 500) {
            throw new IllegalArgumentException("Descrição deve ter no máximo 500 caracteres");
        }
        if (preco == null) {
            throw new IllegalArgumentException("Preço é obrigatório");
        }
        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
    }
    
    /**
     * Cria uma nova instância do produto com ID e dataCadastro preenchidos.
     * Utilizado após a persistência do produto.
     *
     * @param id o ID gerado automaticamente
     * @param dataCadastro a data/hora de cadastro
     * @return nova instância do produto com ID e dataCadastro
     */
    public Produto comIdEDataCadastro(Long id, LocalDateTime dataCadastro) {
        return new Produto(id, this.nome, this.descricao, this.preco, this.categoria, dataCadastro, this.cnpjFornecedor);
    }
}