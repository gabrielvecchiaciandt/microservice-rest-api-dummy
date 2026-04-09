package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.EmpresaDataProvider;
import br.com.flowtechsolutions.core.entity.Empresa;
import br.com.flowtechsolutions.core.entity.exception.EmpresaNaoEncontradaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use Case para inativação lógica de uma Empresa.
 * A empresa não é excluída do banco, apenas marcada como inativa.
 */
public class InativarEmpresaUseCase {

    private static final Logger logger = LoggerFactory.getLogger(InativarEmpresaUseCase.class);

    private final EmpresaDataProvider empresaDataProvider;

    public InativarEmpresaUseCase(EmpresaDataProvider empresaDataProvider) {
        this.empresaDataProvider = empresaDataProvider;
    }

    /**
     * Inativa uma empresa pelo seu CNPJ numérico.
     *
     * @param cnpjSoDigitos os 14 dígitos do CNPJ
     * @return a empresa inativada
     * @throws EmpresaNaoEncontradaException se a empresa não for encontrada
     */
    public Empresa executar(String cnpjSoDigitos) {
        logger.debug("Inativando empresa com CNPJ: {}", cnpjSoDigitos);

        Empresa empresa = empresaDataProvider.buscarPorCnpj(cnpjSoDigitos)
            .orElseThrow(() -> new EmpresaNaoEncontradaException(cnpjSoDigitos));

        Empresa inativada = empresa.inativar();
        Empresa salva = empresaDataProvider.salvar(inativada);

        logger.info("Empresa CNPJ {} inativada com sucesso", cnpjSoDigitos);
        return salva;
    }
}
