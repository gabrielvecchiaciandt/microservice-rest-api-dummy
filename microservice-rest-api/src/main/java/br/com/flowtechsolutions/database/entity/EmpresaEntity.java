package br.com.flowtechsolutions.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade JPA para persistência de Empresa.
 * O CNPJ é armazenado como 14 dígitos numéricos (formato legado sem máscara).
 */
@Entity
@Table(name = "empresa")
public class EmpresaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** CNPJ armazenado como 14 dígitos numéricos sem formatação: XXXXXXXXXXXXXX */
    @Column(name = "cnpj", nullable = false, length = 14, unique = true)
    private String cnpj;

    @Column(name = "razao_social", nullable = false, length = 150)
    private String razaoSocial;

    @Column(name = "nome_fantasia", length = 100)
    private String nomeFantasia;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "ativa", nullable = false)
    private boolean ativa;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    public EmpresaEntity() {
    }

    public EmpresaEntity(Long id, String cnpj, String razaoSocial, String nomeFantasia,
                         String email, String telefone, boolean ativa, LocalDateTime dataCadastro) {
        this.id = id;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.email = email;
        this.telefone = telefone;
        this.ativa = ativa;
        this.dataCadastro = dataCadastro;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpresaEntity that = (EmpresaEntity) o;
        return Objects.equals(cnpj, that.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnpj);
    }

    @Override
    public String toString() {
        return "EmpresaEntity{id=" + id + ", cnpj='" + cnpj + "', razaoSocial='" + razaoSocial + "'}";
    }
}
