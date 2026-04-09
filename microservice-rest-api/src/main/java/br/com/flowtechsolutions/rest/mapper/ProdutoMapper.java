package br.com.flowtechsolutions.rest.mapper;

import br.com.flowtechsolutions.core.entity.Produto;
import br.com.flowtechsolutions.core.entity.constants.CategoriaEnum;
import br.com.flowtechsolutions.core.entity.value.Cnpj;
import br.com.flowtechsolutions.api.dto.ProdutoRequest;
import br.com.flowtechsolutions.api.dto.ProdutoResponse;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão entre DTOs de Produto (gerados pelo OpenAPI) e entidades de domínio.
 * Responsável por transformar dados entre as camadas de API e Core.
 */
public final class ProdutoMapper {
    
    private ProdutoMapper() {
        // Construtor privado para classe utilitária
    }
    
    /**
     * Converte ProdutoRequest (DTO de entrada) para entidade de domínio Produto.
     *
     * @param request o DTO de requisição
     * @return a entidade de domínio
     */
    public static Produto toDomain(ProdutoRequest request) {
        if (request == null) {
            return null;
        }
        Cnpj cnpjFornecedor = (request.getCnpjFornecedor() != null && !request.getCnpjFornecedor().isBlank())
            ? new Cnpj(request.getCnpjFornecedor())
            : null;
        return new Produto(
            request.getNome(),
            request.getDescricao(),
            BigDecimal.valueOf(request.getPreco()),
            CategoriaEnum.valueOf(request.getCategoria().name()),
            cnpjFornecedor
        );
    }
    
    /**
     * Converte entidade de domínio Produto para ProdutoResponse (DTO de saída).
     *
     * @param produto a entidade de domínio
     * @return o DTO de resposta
     */
    public static ProdutoResponse toResponse(Produto produto) {
        if (produto == null) {
            return null;
        }
        
        ProdutoResponse response = new ProdutoResponse();
        response.setId(produto.id());
        response.setNome(produto.nome());
        response.setDescricao(produto.descricao());
        response.setPreco(produto.preco().doubleValue());
        response.setCategoria(ProdutoResponse.CategoriaEnum.valueOf(produto.categoria().name()));
        
        if (produto.dataCadastro() != null) {
            response.setDataCadastro(produto.dataCadastro().atOffset(ZoneOffset.UTC));
        }
        if (produto.cnpjFornecedor() != null) {
            response.setCnpjFornecedor(produto.cnpjFornecedor().formatado());
        }

        return response;
    }
    
    /**
     * Converte uma lista de entidades de domínio Produto para uma lista de ProdutoResponse.
     *
     * @param produtos lista de entidades de domínio
     * @return lista de DTOs de resposta
     */
    public static List<ProdutoResponse> toResponseList(List<Produto> produtos) {
        if (produtos == null) {
            return null;
        }
        
        return produtos.stream()
            .map(ProdutoMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Converte ProdutoRequest e ID para entidade de domínio Produto (para atualizações).
     *
     * @param id o identificador do produto
     * @param request o DTO de requisição
     * @return a entidade de domínio
     */
    public static Produto toDomain(Long id, ProdutoRequest request) {
        if (request == null) {
            return null;
        }
        
        Cnpj cnpjFornecedor = (request.getCnpjFornecedor() != null && !request.getCnpjFornecedor().isBlank())
            ? new Cnpj(request.getCnpjFornecedor())
            : null;
        return new Produto(
            id,
            request.getNome(),
            request.getDescricao(),
            BigDecimal.valueOf(request.getPreco()),
            CategoriaEnum.valueOf(request.getCategoria().name()),
            null,
            cnpjFornecedor
        );
    }
}