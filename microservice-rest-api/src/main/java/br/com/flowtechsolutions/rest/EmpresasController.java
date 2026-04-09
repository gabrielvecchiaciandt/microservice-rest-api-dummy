package br.com.flowtechsolutions.rest;

import br.com.flowtechsolutions.core.entity.Empresa;
import br.com.flowtechsolutions.core.entity.value.Cnpj;
import br.com.flowtechsolutions.core.usecase.AtualizarEmpresaUseCase;
import br.com.flowtechsolutions.core.usecase.BuscarEmpresaPorCnpjUseCase;
import br.com.flowtechsolutions.core.usecase.CriarEmpresaUseCase;
import br.com.flowtechsolutions.core.usecase.InativarEmpresaUseCase;
import br.com.flowtechsolutions.core.usecase.ListarEmpresasUseCase;
import br.com.flowtechsolutions.api.dto.EmpresaRequest;
import br.com.flowtechsolutions.api.dto.EmpresaResponse;
import br.com.flowtechsolutions.rest.mapper.EmpresaMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para gerenciamento de empresas fornecedoras.
 * Todos os CNPJs tratados neste controller são no formato numérico legado (pré-2026).
 *
 * Endpoints:
 *   GET    /api/v1/empresas              - listar todas (ou apenas ativas)
 *   POST   /api/v1/empresas              - cadastrar nova empresa
 *   GET    /api/v1/empresas/{cnpj}       - buscar por CNPJ
 *   PUT    /api/v1/empresas/{cnpj}       - atualizar dados cadastrais
 *   DELETE /api/v1/empresas/{cnpj}       - inativar logicamente
 */
@RestController
@RequestMapping("/api/v1/empresas")
public class EmpresasController {

    private final CriarEmpresaUseCase criarEmpresaUseCase;
    private final ListarEmpresasUseCase listarEmpresasUseCase;
    private final BuscarEmpresaPorCnpjUseCase buscarEmpresaPorCnpjUseCase;
    private final AtualizarEmpresaUseCase atualizarEmpresaUseCase;
    private final InativarEmpresaUseCase inativarEmpresaUseCase;

    public EmpresasController(CriarEmpresaUseCase criarEmpresaUseCase,
                              ListarEmpresasUseCase listarEmpresasUseCase,
                              BuscarEmpresaPorCnpjUseCase buscarEmpresaPorCnpjUseCase,
                              AtualizarEmpresaUseCase atualizarEmpresaUseCase,
                              InativarEmpresaUseCase inativarEmpresaUseCase) {
        this.criarEmpresaUseCase = criarEmpresaUseCase;
        this.listarEmpresasUseCase = listarEmpresasUseCase;
        this.buscarEmpresaPorCnpjUseCase = buscarEmpresaPorCnpjUseCase;
        this.atualizarEmpresaUseCase = atualizarEmpresaUseCase;
        this.inativarEmpresaUseCase = inativarEmpresaUseCase;
    }

    @PostMapping
    public ResponseEntity<EmpresaResponse> criarEmpresa(@RequestBody EmpresaRequest request) {
        Empresa empresa = EmpresaMapper.toDomain(request);
        Empresa criada = criarEmpresaUseCase.executar(empresa);
        EmpresaResponse response = EmpresaMapper.toResponse(criada);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{cnpj}")
            .buildAndExpand(criada.cnpj().soDigitos())
            .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EmpresaResponse>> listarEmpresas(
            @RequestParam(required = false, defaultValue = "false") Boolean apenasAtivas) {
        List<Empresa> empresas = Boolean.TRUE.equals(apenasAtivas)
            ? listarEmpresasUseCase.executarApenasAtivas()
            : listarEmpresasUseCase.executar();
        return ResponseEntity.ok(EmpresaMapper.toResponseList(empresas));
    }

    @GetMapping("/{cnpj}")
    public ResponseEntity<EmpresaResponse> buscarEmpresaPorCnpj(@PathVariable String cnpj) {
        Empresa empresa = buscarEmpresaPorCnpjUseCase.executar(cnpj);
        return ResponseEntity.ok(EmpresaMapper.toResponse(empresa));
    }

    @PutMapping("/{cnpj}")
    public ResponseEntity<EmpresaResponse> atualizarEmpresa(
            @PathVariable String cnpj,
            @RequestBody EmpresaRequest request) {
        Empresa empresaExistente = buscarEmpresaPorCnpjUseCase.executar(cnpj);
        Empresa dadosAtualizados = new Empresa(
            empresaExistente.cnpj(),
            request.getRazaoSocial(),
            request.getNomeFantasia(),
            request.getEmail(),
            request.getTelefone()
        );
        Empresa atualizada = atualizarEmpresaUseCase.executar(empresaExistente.id(), dadosAtualizados);
        return ResponseEntity.ok(EmpresaMapper.toResponse(atualizada));
    }

    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Void> inativarEmpresa(@PathVariable String cnpj) {
        Cnpj cnpjValidado = new Cnpj(cnpj);
        inativarEmpresaUseCase.executar(cnpjValidado.soDigitos());
        return ResponseEntity.noContent().build();
    }
}
