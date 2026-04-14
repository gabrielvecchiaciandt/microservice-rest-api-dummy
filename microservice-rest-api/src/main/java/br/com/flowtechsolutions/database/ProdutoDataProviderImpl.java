package br.com.flowtechsolutions.database;

import br.com.flowtechsolutions.core.dataprovider.ProdutoDataProvider;
import br.com.flowtechsolutions.core.entity.Produto;
import br.com.flowtechsolutions.core.entity.value.Cnpj;
import br.com.flowtechsolutions.database.entity.ProdutoEntity;
import br.com.flowtechsolutions.database.repository.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do ProdutoDataProvider usando Spring Data JPA.
 * Responsável por converter entre entidades de domínio e entidades JPA,
 * e executar operações de persistência no banco de dados.
 */
public class ProdutoDataProviderImpl implements ProdutoDataProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(ProdutoDataProviderImpl.class);
    
    private final ProdutoRepository produtoRepository;
    
    /**
     * Construtor com injeção de dependência do repository.
     *
     * @param produtoRepository o repository JPA de produtos
     */
    public ProdutoDataProviderImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }
    
    @Override
    public Produto salvar(Produto produto) {
        logger.debug("Salvando produto no banco de dados");
        
        ProdutoEntity entity = toEntity(produto);
        ProdutoEntity entitySalva = produtoRepository.save(entity);
        
        return toDomain(entitySalva);
    }
    
    @Override
    public Optional<Produto> buscarPorId(Long id) {
        logger.debug("Buscando produto por ID: {}", id);
        
        return produtoRepository.findById(id)
            .map(this::toDomain);
    }
    
    @Override
    public List<Produto> listarTodos() {
        logger.debug("Listando todos os produtos do banco de dados");
        
        return produtoRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void remover(Long id) {
        logger.debug("Removendo produto ID: {}", id);
        
        produtoRepository.deleteById(id);
    }
    
    @Override
    public boolean existePorId(Long id) {
        logger.debug("Verificando existência do produto ID: {}", id);
        
        return produtoRepository.existsById(id);
    }
    
    /**
     * Converte entidade de domínio para entidade JPA.
     *
     * @param produto a entidade de domínio
     * @return a entidade JPA
     */
    private ProdutoEntity toEntity(Produto produto) {
        String cnpjFornecedor = produto.cnpjFornecedor() != null
            ? produto.cnpjFornecedor().valor()
            : null;
        return new ProdutoEntity(
            produto.id(),
            produto.nome(),
            produto.descricao(),
            produto.preco(),
            produto.categoria(),
            produto.dataCadastro(),
            cnpjFornecedor
        );
    }

    /**
     * Converte entidade JPA para entidade de domínio.
     *
     * @param entity a entidade JPA
     * @return a entidade de domínio
     */
    private Produto toDomain(ProdutoEntity entity) {
        Cnpj cnpjFornecedor = entity.getCnpjFornecedor() != null
            ? new Cnpj(entity.getCnpjFornecedor())
            : null;
        return new Produto(
            entity.getId(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getPreco(),
            entity.getCategoria(),
            entity.getDataCadastro(),
            cnpjFornecedor
        );
    }
}