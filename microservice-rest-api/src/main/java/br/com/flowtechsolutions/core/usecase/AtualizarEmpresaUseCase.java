package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.EmpresaDataProvider;
import br.com.flowtechsolutions.core.entity.Empresa;
import br.com.flowtechsolutions.core.entity.exception.EmpresaNaoEncontradaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use Case para atualização de dados de uma Empresa.
 * O CNPJ é imutável e não pode ser alterado.
 */
public class AtualizarEmpresaUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AtualizarEmpresaUseCase.class);

    private final EmpresaDataProvider empresaDataProvider;

    public AtualizarEmpresaUseCase(EmpresaDataProvider empresaDataProvider) {
        this.empresaDataProvider = empresaDataProvider;
    }

    /**
     * Atualiza os dados cadastrais de uma empresa existente.
     * O CNPJ não pode ser alterado.
     *
     * @param id o identificador da empresa
     * @param dadosAtualizados os novos dados da empresa
     * @return a empresa atualizada
     * @throws EmpresaNaoEncontradaException se a empresa não for encontrada
     */
    public Empresa executar(Long id, Empresa dadosAtualizados) {
        logger.debug("Atualizando empresa ID: {}", id);

        Empresa empresaExistente = empresaDataProvider.buscarPorId(id)
            .orElseThrow(() -> new EmpresaNaoEncontradaException(id));

        dadosAtualizados.validar();

        Empresa empresaAtualizada = new Empresa(
            empresaExistente.id(),
            empresaExistente.cnpj(),
            dadosAtualizados.razaoSocial(),
            dadosAtualizados.nomeFantasia(),
            dadosAtualizados.email(),
            dadosAtualizados.telefone(),
            dadosAtualizados.ativa(),
            empresaExistente.dataCadastro()
        );

        Empresa salva = empresaDataProvider.salvar(empresaAtualizada);
        logger.info("Empresa ID {} atualizada com sucesso", id);
        return salva;
    }
}
