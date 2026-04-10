package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.EmpresaDataProvider;
import br.com.flowtechsolutions.core.entity.Empresa;
import br.com.flowtechsolutions.core.entity.exception.CnpjInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Use Case para cadastro de nova Empresa.
 * Valida o CNPJ numérico legado antes de persistir.
 */
public class CriarEmpresaUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CriarEmpresaUseCase.class);

    private final EmpresaDataProvider empresaDataProvider;

    public CriarEmpresaUseCase(EmpresaDataProvider empresaDataProvider) {
        this.empresaDataProvider = empresaDataProvider;
    }

    /**
     * Cria uma nova empresa no sistema.
     *
     * @param empresa dados da empresa a ser criada (sem ID e dataCadastro)
     * @return a empresa criada com ID e dataCadastro preenchidos
     * @throws IllegalArgumentException se os dados forem inválidos
     * @throws CnpjInvalidoException se o CNPJ for inválido
     * @throws IllegalStateException se já existir empresa com o mesmo CNPJ
     */
    public Empresa executar(Empresa empresa) {
        logger.debug("Iniciando cadastro de empresa com CNPJ: {}", empresa.cnpj().formatado());

        empresa.validar();

        if (empresaDataProvider.existePorCnpj(empresa.cnpj().getValor())) {
            throw new IllegalStateException(
                "Já existe uma empresa cadastrada com o CNPJ: " + empresa.cnpj().formatado());
        }

        Empresa empresaComData = empresa.comIdEDataCadastro(null, LocalDateTime.now());
        Empresa empresaSalva = empresaDataProvider.salvar(empresaComData);

        logger.info("Empresa cadastrada com sucesso: CNPJ {} ID {}",
                    empresaSalva.cnpj().formatado(), empresaSalva.id());
        return empresaSalva;
    }
}
