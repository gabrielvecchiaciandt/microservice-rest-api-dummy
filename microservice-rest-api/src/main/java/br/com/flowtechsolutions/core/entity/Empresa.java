package br.com.flowtechsolutions.core.entity;

import br.com.flowtechsolutions.core.entity.value.Cnpj;

import java.time.LocalDateTime;

/**
 * Entidade de domínio representando uma Empresa fornecedora.
 * Identificada pelo CNPJ no formato numérico legado (14 dígitos).
 * Imutável por design (Java record).
 */
public record Empresa(
    Long id,
    Cnpj cnpj,
    String razaoSocial,
    String nomeFantasia,
    String email,
    String telefone,
    boolean ativa,
    LocalDateTime dataCadastro
) {

    /**
     * Construtor para criação de nova empresa (sem ID e dataCadastro).
     */
    public Empresa(Cnpj cnpj, String razaoSocial, String nomeFantasia,
                   String email, String telefone) {
        this(null, cnpj, razaoSocial, nomeFantasia, email, telefone, true, null);
    }

    /**
     * Valida os dados obrigatórios da empresa.
     *
     * @throws IllegalArgumentException se os dados forem inválidos
     */
    public void validar() {
        if (cnpj == null) {
            throw new IllegalArgumentException("CNPJ é obrigatório");
        }
        if (razaoSocial == null || razaoSocial.isBlank()) {
            throw new IllegalArgumentException("Razão social é obrigatória");
        }
        if (razaoSocial.length() < 3) {
            throw new IllegalArgumentException("Razão social deve ter no mínimo 3 caracteres");
        }
        if (razaoSocial.length() > 150) {
            throw new IllegalArgumentException("Razão social deve ter no máximo 150 caracteres");
        }
        if (nomeFantasia != null && nomeFantasia.length() > 100) {
            throw new IllegalArgumentException("Nome fantasia deve ter no máximo 100 caracteres");
        }
    }

    /**
     * Retorna nova instância com ID e dataCadastro preenchidos após persistência.
     */
    public Empresa comIdEDataCadastro(Long id, LocalDateTime dataCadastro) {
        return new Empresa(id, this.cnpj, this.razaoSocial, this.nomeFantasia,
                           this.email, this.telefone, this.ativa, dataCadastro);
    }

    /**
     * Retorna nova instância com status inativo.
     */
    public Empresa inativar() {
        return new Empresa(this.id, this.cnpj, this.razaoSocial, this.nomeFantasia,
                           this.email, this.telefone, false, this.dataCadastro);
    }
}
