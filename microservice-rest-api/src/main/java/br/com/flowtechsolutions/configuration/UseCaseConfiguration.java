package br.com.flowtechsolutions.configuration;

import br.com.flowtechsolutions.core.dataprovider.EmpresaDataProvider;
import br.com.flowtechsolutions.core.dataprovider.HistoricoAlteracaoDataProvider;
import br.com.flowtechsolutions.core.dataprovider.ProdutoDataProvider;
import br.com.flowtechsolutions.core.usecase.AtualizarEmpresaUseCase;
import br.com.flowtechsolutions.core.usecase.AtualizarProdutoUseCase;
import br.com.flowtechsolutions.core.usecase.BuscarEmpresaPorCnpjUseCase;
import br.com.flowtechsolutions.core.usecase.BuscarProdutoPorIdUseCase;
import br.com.flowtechsolutions.core.usecase.ConsultarHistoricoUseCase;
import br.com.flowtechsolutions.core.usecase.CriarEmpresaUseCase;
import br.com.flowtechsolutions.core.usecase.CriarProdutoUseCase;
import br.com.flowtechsolutions.core.usecase.InativarEmpresaUseCase;
import br.com.flowtechsolutions.core.usecase.ListarEmpresasUseCase;
import br.com.flowtechsolutions.core.usecase.ListarProdutosUseCase;
import br.com.flowtechsolutions.core.usecase.RemoverProdutoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de beans para Use Cases.
 * Responsável por criar as instâncias de use cases e injetar as dependências necessárias (data providers).
 */
@Configuration
public class UseCaseConfiguration {
    
    /**
     * Cria o bean de CriarProdutoUseCase.
     *
     * @param produtoDataProvider provedor de dados de produtos
     * @param historicoDataProvider provedor de dados de histórico
     * @return instância de CriarProdutoUseCase
     */
    @Bean
    public CriarProdutoUseCase criarProdutoUseCase(ProdutoDataProvider produtoDataProvider,
                                                   HistoricoAlteracaoDataProvider historicoDataProvider) {
        return new CriarProdutoUseCase(produtoDataProvider, historicoDataProvider);
    }
    
    /**
     * Cria o bean de ListarProdutosUseCase.
     *
     * @param produtoDataProvider provedor de dados de produtos
     * @return instância de ListarProdutosUseCase
     */
    @Bean
    public ListarProdutosUseCase listarProdutosUseCase(ProdutoDataProvider produtoDataProvider) {
        return new ListarProdutosUseCase(produtoDataProvider);
    }
    
    /**
     * Cria o bean de BuscarProdutoPorIdUseCase.
     *
     * @param produtoDataProvider provedor de dados de produtos
     * @return instância de BuscarProdutoPorIdUseCase
     */
    @Bean
    public BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase(ProdutoDataProvider produtoDataProvider) {
        return new BuscarProdutoPorIdUseCase(produtoDataProvider);
    }
    
    /**
     * Cria o bean de AtualizarProdutoUseCase.
     *
     * @param produtoDataProvider provedor de dados de produtos
     * @param historicoDataProvider provedor de dados de histórico
     * @return instância de AtualizarProdutoUseCase
     */
    @Bean
    public AtualizarProdutoUseCase atualizarProdutoUseCase(ProdutoDataProvider produtoDataProvider,
                                                           HistoricoAlteracaoDataProvider historicoDataProvider) {
        return new AtualizarProdutoUseCase(produtoDataProvider, historicoDataProvider);
    }
    
    /**
     * Cria o bean de RemoverProdutoUseCase.
     *
     * @param produtoDataProvider provedor de dados de produtos
     * @param historicoDataProvider provedor de dados de histórico
     * @return instância de RemoverProdutoUseCase
     */
    @Bean
    public RemoverProdutoUseCase removerProdutoUseCase(ProdutoDataProvider produtoDataProvider,
                                                       HistoricoAlteracaoDataProvider historicoDataProvider) {
        return new RemoverProdutoUseCase(produtoDataProvider, historicoDataProvider);
    }
    
    /**
     * Cria o bean de ConsultarHistoricoUseCase.
     *
     * @param produtoDataProvider provedor de dados de produtos
     * @param historicoDataProvider provedor de dados de histórico
     * @return instância de ConsultarHistoricoUseCase
     */
    @Bean
    public ConsultarHistoricoUseCase consultarHistoricoUseCase(ProdutoDataProvider produtoDataProvider,
                                                               HistoricoAlteracaoDataProvider historicoDataProvider) {
        return new ConsultarHistoricoUseCase(produtoDataProvider, historicoDataProvider);
    }

    @Bean
    public CriarEmpresaUseCase criarEmpresaUseCase(EmpresaDataProvider empresaDataProvider) {
        return new CriarEmpresaUseCase(empresaDataProvider);
    }

    @Bean
    public ListarEmpresasUseCase listarEmpresasUseCase(EmpresaDataProvider empresaDataProvider) {
        return new ListarEmpresasUseCase(empresaDataProvider);
    }

    @Bean
    public BuscarEmpresaPorCnpjUseCase buscarEmpresaPorCnpjUseCase(EmpresaDataProvider empresaDataProvider) {
        return new BuscarEmpresaPorCnpjUseCase(empresaDataProvider);
    }

    @Bean
    public AtualizarEmpresaUseCase atualizarEmpresaUseCase(EmpresaDataProvider empresaDataProvider) {
        return new AtualizarEmpresaUseCase(empresaDataProvider);
    }

    @Bean
    public InativarEmpresaUseCase inativarEmpresaUseCase(EmpresaDataProvider empresaDataProvider) {
        return new InativarEmpresaUseCase(empresaDataProvider);
    }
}