package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.EmpresaDataProvider;
import br.com.flowtechsolutions.core.entity.Empresa;
import br.com.flowtechsolutions.core.entity.exception.EmpresaNaoEncontradaException;
import br.com.flowtechsolutions.core.entity.value.Cnpj;
import br.com.flowtechsolutions.core.entity.exception.CnpjInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use Case para busca de Empresa pelo CNPJ numérico legado.
 */
public class BuscarEmpresaPorCnpjUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarEmpresaPorCnpjUseCase.class);

    private final EmpresaDataProvider empresaDataProvider;

    public BuscarEmpresaPorCnpjUseCase(EmpresaDataProvider empresaDataProvider) {
        this.empresaDataProvider = empresaDataProvider;
    }

    /**
     * Busca uma empresa pelo CNPJ.
     * Aceita o CNPJ com ou sem formatação (ex: "11.222.333/0001-81" ou "11222333000181").
     *
     * @param cnpj o CNPJ da empresa (com ou sem máscara)
     * @return a empresa encontrada
     * @throws CnpjInvalidoException se o CNPJ for inválido
     * @throws EmpresaNaoEncontradaException se a empresa não for encontrada
     */
    public Empresa executar(String cnpj) {
        Cnpj cnpjValidado = new Cnpj(cnpj);
        logger.debug("Buscando empresa por CNPJ: {}", cnpjValidado.formatado());

        return empresaDataProvider.buscarPorCnpj(cnpjValidado.soDigitos())
            .orElseThrow(() -> new EmpresaNaoEncontradaException(cnpjValidado.formatado()));
    }
}
