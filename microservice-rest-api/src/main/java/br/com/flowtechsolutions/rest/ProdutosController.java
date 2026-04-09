package br.com.flowtechsolutions.rest;

import br.com.flowtechsolutions.api.api.ApiApi;
import br.com.flowtechsolutions.core.entity.HistoricoAlteracao;
import br.com.flowtechsolutions.core.entity.Produto;
import br.com.flowtechsolutions.core.usecase.AtualizarProdutoUseCase;
import br.com.flowtechsolutions.core.usecase.BuscarProdutoPorIdUseCase;
import br.com.flowtechsolutions.core.usecase.ConsultarHistoricoUseCase;
import br.com.flowtechsolutions.core.usecase.CriarProdutoUseCase;
import br.com.flowtechsolutions.core.usecase.ListarProdutosUseCase;
import br.com.flowtechsolutions.core.usecase.RemoverProdutoUseCase;
import br.com.flowtechsolutions.api.dto.HistoricoResponse;
import br.com.flowtechsolutions.api.dto.ProdutoRequest;
import br.com.flowtechsolutions.api.dto.ProdutoResponse;
import br.com.flowtechsolutions.rest.mapper.HistoricoAlteracaoMapper;
import br.com.flowtechsolutions.rest.mapper.ProdutoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para gerenciamento de produtos.
 * Implementa a interface gerada pelo OpenAPI Generator.
 */
@RestController
public class ProdutosController implements ApiApi {
    
    private final CriarProdutoUseCase criarProdutoUseCase;
    private final ListarProdutosUseCase listarProdutosUseCase;
    private final BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;
    private final AtualizarProdutoUseCase atualizarProdutoUseCase;
    private final RemoverProdutoUseCase removerProdutoUseCase;
    private final ConsultarHistoricoUseCase consultarHistoricoUseCase;
    
    /**
     * Construtor com injeção de dependências dos Use Cases.
     *
     * @param criarProdutoUseCase use case para criar produto
     * @param listarProdutosUseCase use case para listar produtos
     * @param buscarProdutoPorIdUseCase use case para buscar produto por ID
     * @param atualizarProdutoUseCase use case para atualizar produto
     * @param removerProdutoUseCase use case para remover produto
     * @param consultarHistoricoUseCase use case para consultar histórico
     */
    public ProdutosController(CriarProdutoUseCase criarProdutoUseCase,
                              ListarProdutosUseCase listarProdutosUseCase,
                              BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase,
                              AtualizarProdutoUseCase atualizarProdutoUseCase,
                              RemoverProdutoUseCase removerProdutoUseCase,
                              ConsultarHistoricoUseCase consultarHistoricoUseCase) {
        this.criarProdutoUseCase = criarProdutoUseCase;
        this.listarProdutosUseCase = listarProdutosUseCase;
        this.buscarProdutoPorIdUseCase = buscarProdutoPorIdUseCase;
        this.atualizarProdutoUseCase = atualizarProdutoUseCase;
        this.removerProdutoUseCase = removerProdutoUseCase;
        this.consultarHistoricoUseCase = consultarHistoricoUseCase;
    }
    
    @Override
    public ResponseEntity<ProdutoResponse> criarProduto(ProdutoRequest produtoRequest) {
        Produto produtoDomain = ProdutoMapper.toDomain(produtoRequest);
        Produto produtoCriado = criarProdutoUseCase.executar(produtoDomain);
        ProdutoResponse response = ProdutoMapper.toResponse(produtoCriado);
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(produtoCriado.id())
            .toUri();
        
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<List<ProdutoResponse>> listarProdutos() {
        List<Produto> produtos = listarProdutosUseCase.executar();
        List<ProdutoResponse> response = ProdutoMapper.toResponseList(produtos);
        
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<ProdutoResponse> buscarProdutoPorId(Long id) {
        Produto produto = buscarProdutoPorIdUseCase.executar(id);
        ProdutoResponse response = ProdutoMapper.toResponse(produto);
        
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<ProdutoResponse> atualizarProduto(Long id, ProdutoRequest produtoRequest) {
        Produto produtoDomain = ProdutoMapper.toDomain(produtoRequest);
        Produto produtoAtualizado = atualizarProdutoUseCase.executar(id, produtoDomain);
        ProdutoResponse response = ProdutoMapper.toResponse(produtoAtualizado);
        
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> removerProduto(Long id) {
        removerProdutoUseCase.executar(id);
        
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<List<HistoricoResponse>> consultarHistorico(Long id) {
        List<HistoricoAlteracao> historico = consultarHistoricoUseCase.executar(id);
        List<HistoricoResponse> response = HistoricoAlteracaoMapper.toResponseList(historico);
        
        return ResponseEntity.ok(response);
    }
}