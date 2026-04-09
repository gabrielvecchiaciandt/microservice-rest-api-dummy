package br.com.flowtechsolutions.rest.mapper;

import br.com.flowtechsolutions.core.entity.HistoricoAlteracao;
import br.com.flowtechsolutions.api.dto.HistoricoResponse;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão entre DTOs de HistoricoAlteracao (gerados pelo OpenAPI) e entidades de domínio.
 * Responsável por transformar dados entre as camadas de API e Core.
 */
public final class HistoricoAlteracaoMapper {
    
    private HistoricoAlteracaoMapper() {
        // Construtor privado para classe utilitária
    }
    
    /**
     * Converte entidade de domínio HistoricoAlteracao para HistoricoResponse (DTO de saída).
     *
     * @param historico a entidade de domínio
     * @return o DTO de resposta
     */
    public static HistoricoResponse toResponse(HistoricoAlteracao historico) {
        if (historico == null) {
            return null;
        }
        
        HistoricoResponse response = new HistoricoResponse();
        response.setId(historico.id());
        response.setProdutoId(historico.produtoId());
        response.setTipoOperacao(HistoricoResponse.TipoOperacaoEnum.valueOf(historico.tipoOperacao().name()));
        
        if (historico.dataHora() != null) {
            response.setDataHora(historico.dataHora().atOffset(ZoneOffset.UTC));
        }
        
        response.setDadosAnteriores(historico.dadosAnteriores());
        response.setDadosNovos(historico.dadosNovos());
        
        return response;
    }
    
    /**
     * Converte uma lista de entidades de domínio HistoricoAlteracao para uma lista de HistoricoResponse.
     *
     * @param historicos lista de entidades de domínio
     * @return lista de DTOs de resposta
     */
    public static List<HistoricoResponse> toResponseList(List<HistoricoAlteracao> historicos) {
        if (historicos == null) {
            return null;
        }
        
        return historicos.stream()
            .map(HistoricoAlteracaoMapper::toResponse)
            .collect(Collectors.toList());
    }
}