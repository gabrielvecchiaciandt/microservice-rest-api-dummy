package br.com.flowtechsolutions.rest.mapper;

import br.com.flowtechsolutions.core.entity.Empresa;
import br.com.flowtechsolutions.core.entity.value.Cnpj;
import br.com.flowtechsolutions.api.dto.EmpresaRequest;
import br.com.flowtechsolutions.api.dto.EmpresaResponse;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão entre DTOs de Empresa (gerados pelo OpenAPI) e entidades de domínio.
 * O CNPJ é tratado no formato numérico legado em toda a camada de domínio.
 */
public final class EmpresaMapper {

    private EmpresaMapper() {
    }

    /**
     * Converte EmpresaRequest (DTO de entrada) para entidade de domínio Empresa.
     * O CNPJ do request é validado como alfanumérico (12 primeiros caracteres) e numérico (2 últimos) pelo value object Cnpj.
     *
     * @param request o DTO de requisição
     * @return a entidade de domínio
     */
    public static Empresa toDomain(EmpresaRequest request) {
        if (request == null) {
            return null;
        }
        return new Empresa(
            new Cnpj(request.getCnpj()),
            request.getRazaoSocial(),
            request.getNomeFantasia(),
            request.getEmail(),
            request.getTelefone()
        );
    }

    /**
     * Converte entidade de domínio Empresa para EmpresaResponse (DTO de saída).
     * O CNPJ é retornado no formato formatado (AA.AAA.AAA/AAAA-##).
     *
     * @param empresa a entidade de domínio
     * @return o DTO de resposta
     */
    public static EmpresaResponse toResponse(Empresa empresa) {
        if (empresa == null) {
            return null;
        }
        EmpresaResponse response = new EmpresaResponse();
        response.setId(empresa.id());
        response.setCnpj(empresa.cnpj().formatado());
        response.setRazaoSocial(empresa.razaoSocial());
        response.setNomeFantasia(empresa.nomeFantasia());
        response.setEmail(empresa.email());
        response.setTelefone(empresa.telefone());
        response.setAtiva(empresa.ativa());
        if (empresa.dataCadastro() != null) {
            response.setDataCadastro(empresa.dataCadastro().atOffset(ZoneOffset.UTC));
        }
        return response;
    }

    /**
     * Converte lista de entidades de domínio para lista de EmpresaResponse.
     *
     * @param empresas lista de entidades de domínio
     * @return lista de DTOs de resposta
     */
    public static List<EmpresaResponse> toResponseList(List<Empresa> empresas) {
        if (empresas == null) {
            return null;
        }
        return empresas.stream()
            .map(EmpresaMapper::toResponse)
            .collect(Collectors.toList());
    }
}
