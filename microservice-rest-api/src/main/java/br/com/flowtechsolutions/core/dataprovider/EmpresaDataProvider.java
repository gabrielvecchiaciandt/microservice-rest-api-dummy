package br.com.flowtechsolutions.core.dataprovider;

import br.com.flowtechsolutions.core.entity.Empresa;

import java.util.List;
import java.util.Optional;

/**
 * Interface de contrato para operações de persistência de Empresa.
 * Define o contrato entre a camada Core e a camada de Data Providers.
 */
public interface EmpresaDataProvider {

    /**
     * Salva uma nova empresa ou atualiza uma existente.
     *
     * @param empresa a empresa a ser salva
     * @return a empresa salva com ID gerado ou atualizado
     */
    Empresa salvar(Empresa empresa);

    /**
     * Busca uma empresa pelo seu CNPJ.
     *
     * @param cnpj os 14 caracteres do CNPJ sem formatação
     * @return Optional com a empresa se encontrada
     */
    Optional<Empresa> buscarPorCnpj(String cnpj);

    /**
     * Busca uma empresa pelo ID.
     *
     * @param id o identificador da empresa
     * @return Optional com a empresa se encontrada
     */
    Optional<Empresa> buscarPorId(Long id);

    /**
     * Lista todas as empresas cadastradas.
     *
     * @return lista de empresas (pode estar vazia)
     */
    List<Empresa> listarTodas();

    /**
     * Lista apenas as empresas ativas.
     *
     * @return lista de empresas ativas
     */
    List<Empresa> listarAtivas();

    /**
     * Verifica se existe uma empresa com o CNPJ informado.
     *
     * @param cnpj os 14 dígitos do CNPJ sem formatação
     * @return true se a empresa existe
     */
    boolean existePorCnpj(String cnpj);
}
