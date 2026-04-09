package br.com.flowtechsolutions.configuration;

import br.com.flowtechsolutions.core.dataprovider.EmpresaDataProvider;
import br.com.flowtechsolutions.core.dataprovider.HistoricoAlteracaoDataProvider;
import br.com.flowtechsolutions.core.dataprovider.ProdutoDataProvider;
import br.com.flowtechsolutions.database.EmpresaDataProviderImpl;
import br.com.flowtechsolutions.database.HistoricoAlteracaoDataProviderImpl;
import br.com.flowtechsolutions.database.ProdutoDataProviderImpl;
import br.com.flowtechsolutions.database.repository.EmpresaRepository;
import br.com.flowtechsolutions.database.repository.HistoricoAlteracaoRepository;
import br.com.flowtechsolutions.database.repository.ProdutoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de beans para Data Providers.
 * Responsável por criar as instâncias de implementações de data providers
 * e injetar as dependências necessárias (repositories).
 */
@Configuration
public class DataProviderConfiguration {
    
    /**
     * Cria o bean de ProdutoDataProvider.
     *
     * @param produtoRepository o repository JPA de produtos
     * @return implementação de ProdutoDataProvider
     */
    @Bean
    public ProdutoDataProvider produtoDataProvider(ProdutoRepository produtoRepository) {
        return new ProdutoDataProviderImpl(produtoRepository);
    }
    
    /**
     * Cria o bean de HistoricoAlteracaoDataProvider.
     *
     * @param historicoAlteracaoRepository o repository JPA de histórico
     * @return implementação de HistoricoAlteracaoDataProvider
     */
    @Bean
    public HistoricoAlteracaoDataProvider historicoAlteracaoDataProvider(
            HistoricoAlteracaoRepository historicoAlteracaoRepository) {
        return new HistoricoAlteracaoDataProviderImpl(historicoAlteracaoRepository);
    }

    @Bean
    public EmpresaDataProvider empresaDataProvider(EmpresaRepository empresaRepository) {
        return new EmpresaDataProviderImpl(empresaRepository);
    }
}