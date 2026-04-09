package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.EmpresaDataProvider;
import br.com.flowtechsolutions.core.entity.Empresa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Use Case para listagem de empresas cadastradas.
 */
public class ListarEmpresasUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ListarEmpresasUseCase.class);

    private final EmpresaDataProvider empresaDataProvider;

    public ListarEmpresasUseCase(EmpresaDataProvider empresaDataProvider) {
        this.empresaDataProvider = empresaDataProvider;
    }

    /**
     * Lista todas as empresas cadastradas no sistema.
     *
     * @return lista de empresas (pode estar vazia)
     */
    public List<Empresa> executar() {
        logger.debug("Listando todas as empresas");
        return empresaDataProvider.listarTodas();
    }

    /**
     * Lista apenas as empresas ativas.
     *
     * @return lista de empresas ativas
     */
    public List<Empresa> executarApenasAtivas() {
        logger.debug("Listando empresas ativas");
        return empresaDataProvider.listarAtivas();
    }
}
