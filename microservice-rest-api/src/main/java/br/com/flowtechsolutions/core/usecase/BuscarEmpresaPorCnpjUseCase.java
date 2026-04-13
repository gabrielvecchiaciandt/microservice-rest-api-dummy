package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.EmpresaDataProvider;
import br.com.flowtechsolutions.core.entity.Empresa;
import br.com.flowtechsolutions.core.entity.exception.EmpresaNaoEncontradaException;
import br.com.flowtechsolutions.core.entity.value.Cnpj;
import br.com.flowtechsolutions.core.entity.exception.CnpjInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use Case para busca de Empresa pelo CNPJ.
 */
public class BuscarEmpresaPorCnpjUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarEmpresaPorCnpjUseCase.class);

    private final EmpresaDataProvider empresaDataProvider;

    public BuscarEmpresaPorCnpjUseCase(EmpresaDataProvider empresaDataProvider) {
        this.empresaDataProvider = empresaDataProvider;
    }

    /**
     * Busca uma empresa pelo CNPJ.
     * Aceita o CNPJ com ou sem formatação (ex: "A1.B2C.D34/E5F6-78" ou "A1B2C3D4E5F678").
     *
     * @param cnpj o CNPJ da empresa (com ou sem máscara)
     * @return a empresa encontrada
     * @throws CnpjInvalidoException se o CNPJ for inválido
     * @throws EmpresaNaoEncontradaException se a empresa não for encontrada
     */
    public Empresa executar(String cnpj) {
        Cnpj cnpjValidado = new Cnpj(cnpj);
        logger.debug("Buscando empresa por CNPJ: {}", cnpjValidado.formatado());

        return empresaDataProvider.buscarPorCnpj(cnpjValidado.getValor())
            .orElseThrow(() -> new EmpresaNaoEncontradaException(cnpjValidado.formatado()));
    }
}
