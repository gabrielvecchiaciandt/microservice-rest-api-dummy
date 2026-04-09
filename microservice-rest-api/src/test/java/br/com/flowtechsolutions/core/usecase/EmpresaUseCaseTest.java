package br.com.flowtechsolutions.core.usecase;

import br.com.flowtechsolutions.core.dataprovider.EmpresaDataProvider;
import br.com.flowtechsolutions.core.entity.Empresa;
import br.com.flowtechsolutions.core.entity.exception.CnpjInvalidoException;
import br.com.flowtechsolutions.core.entity.exception.EmpresaNaoEncontradaException;
import br.com.flowtechsolutions.core.entity.value.Cnpj;
import br.com.flowtechsolutions.core.usecase.BuscarEmpresaPorCnpjUseCase;
import br.com.flowtechsolutions.core.usecase.CriarEmpresaUseCase;
import br.com.flowtechsolutions.core.usecase.InativarEmpresaUseCase;
import br.com.flowtechsolutions.core.usecase.ListarEmpresasUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes unitários dos use cases de Empresa.
 * Todos os CNPJs são no formato numérico legado (14 dígitos, pré-2026).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Use Cases de Empresa")
class EmpresaUseCaseTest {

    // CNPJs numéricos legados para os testes
    private static final String CNPJ_EMPRESA_A      = "11222333000181"; // 11.222.333/0001-81
    private static final String CNPJ_FORNECEDOR_A   = "12345678000195"; // 12.345.678/0001-95
    private static final String CNPJ_FORNECEDOR_B   = "45678901000175"; // 45.678.901/0001-75
    private static final String CNPJ_PETROBRAS       = "33000167000101"; // 33.000.167/0001-01
    private static final String CNPJ_ITAU            = "60701190000104"; // 60.701.190/0001-04

    @Mock
    private EmpresaDataProvider empresaDataProvider;

    private Empresa empresaFixtura(String cnpjDigitos, String razaoSocial) {
        return new Empresa(
            1L,
            new Cnpj(cnpjDigitos),
            razaoSocial,
            razaoSocial,
            "contato@empresa.com.br",
            "(11) 3000-0000",
            true,
            LocalDateTime.of(2026, 1, 15, 10, 0)
        );
    }

    @Nested
    @DisplayName("CriarEmpresaUseCase")
    class CriarEmpresa {

        private CriarEmpresaUseCase useCase;

        @BeforeEach
        void setUp() {
            useCase = new CriarEmpresaUseCase(empresaDataProvider);
        }

        @Test
        @DisplayName("Deve criar empresa com CNPJ numérico válido")
        void deveCriarEmpresaComCnpjValido() {
            Empresa novaEmpresa = new Empresa(
                new Cnpj(CNPJ_EMPRESA_A),
                "Empresa A Soluções LTDA",
                "Empresa A",
                "contato@empresaa.com.br",
                "(11) 3003-0000"
            );
            Empresa empresaSalva = empresaFixtura(CNPJ_EMPRESA_A, "Empresa A Soluções LTDA");

            when(empresaDataProvider.existePorCnpj(CNPJ_EMPRESA_A)).thenReturn(false);
            when(empresaDataProvider.salvar(any())).thenReturn(empresaSalva);

            Empresa resultado = useCase.executar(novaEmpresa);

            assertThat(resultado.id()).isEqualTo(1L);
            assertThat(resultado.cnpj().soDigitos()).isEqualTo(CNPJ_EMPRESA_A);
            assertThat(resultado.cnpj().formatado()).isEqualTo("11.222.333/0001-81");
            verify(empresaDataProvider).salvar(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando CNPJ já cadastrado")
        void deveLancarExcecaoQuandoCnpjJaCadastrado() {
            Empresa novaEmpresa = new Empresa(
                new Cnpj(CNPJ_EMPRESA_A),
                "Empresa Duplicada",
                null, null, null
            );

            when(empresaDataProvider.existePorCnpj(CNPJ_EMPRESA_A)).thenReturn(true);

            assertThatThrownBy(() -> useCase.executar(novaEmpresa))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("11.222.333/0001-81");

            verify(empresaDataProvider, never()).salvar(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando razão social é nula")
        void deveLancarExcecaoQuandoRazaoSocialNula() {
            Empresa semRazaoSocial = new Empresa(
                new Cnpj(CNPJ_FORNECEDOR_A), null, null, null, null
            );

            // validar() é chamada antes de existePorCnpj no use case,
            // portanto a exceção é lançada sem precisar de stub no provider
            assertThatThrownBy(() -> useCase.executar(semRazaoSocial))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Razão social");
        }
    }

    @Nested
    @DisplayName("BuscarEmpresaPorCnpjUseCase")
    class BuscarEmpresa {

        private BuscarEmpresaPorCnpjUseCase useCase;

        @BeforeEach
        void setUp() {
            useCase = new BuscarEmpresaPorCnpjUseCase(empresaDataProvider);
        }

        @Test
        @DisplayName("Deve buscar empresa por CNPJ somente dígitos")
        void deveBuscarPorCnpjSoDigitos() {
            Empresa empresa = empresaFixtura(CNPJ_PETROBRAS, "Petróleo Brasileiro S.A.");
            when(empresaDataProvider.buscarPorCnpj(CNPJ_PETROBRAS)).thenReturn(Optional.of(empresa));

            Empresa resultado = useCase.executar(CNPJ_PETROBRAS);
            assertThat(resultado.cnpj().formatado()).isEqualTo("33.000.167/0001-01");
        }

        @Test
        @DisplayName("Deve buscar empresa por CNPJ formatado com máscara")
        void deveBuscarPorCnpjFormatado() {
            Empresa empresa = empresaFixtura(CNPJ_ITAU, "Itaú Unibanco S.A.");
            when(empresaDataProvider.buscarPorCnpj(CNPJ_ITAU)).thenReturn(Optional.of(empresa));

            Empresa resultado = useCase.executar("60.701.190/0001-04");
            assertThat(resultado.razaoSocial()).isEqualTo("Itaú Unibanco S.A.");
        }

        @Test
        @DisplayName("Deve lançar EmpresaNaoEncontradaException quando não existe")
        void deveLancarExcecaoQuandoNaoEncontrada() {
            when(empresaDataProvider.buscarPorCnpj(CNPJ_FORNECEDOR_B)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.executar(CNPJ_FORNECEDOR_B))
                .isInstanceOf(EmpresaNaoEncontradaException.class);
        }

        @Test
        @DisplayName("Deve lançar CnpjInvalidoException para CNPJ inválido")
        void deveLancarExcecaoParaCnpjInvalido() {
            assertThatThrownBy(() -> useCase.executar("00000000000000"))
                .isInstanceOf(CnpjInvalidoException.class);
        }
    }

    @Nested
    @DisplayName("ListarEmpresasUseCase")
    class ListarEmpresas {

        private ListarEmpresasUseCase useCase;

        @BeforeEach
        void setUp() {
            useCase = new ListarEmpresasUseCase(empresaDataProvider);
        }

        @Test
        @DisplayName("Deve listar todas as empresas cadastradas")
        void deveListarTodasEmpresas() {
            List<Empresa> empresas = List.of(
                empresaFixtura(CNPJ_EMPRESA_A, "Empresa A"),
                empresaFixtura(CNPJ_FORNECEDOR_A, "Fornecedor A"),
                empresaFixtura(CNPJ_PETROBRAS, "Petrobras")
            );
            when(empresaDataProvider.listarTodas()).thenReturn(empresas);

            List<Empresa> resultado = useCase.executar();

            assertThat(resultado).hasSize(3);
            assertThat(resultado).extracting(e -> e.cnpj().soDigitos())
                .containsExactly(CNPJ_EMPRESA_A, CNPJ_FORNECEDOR_A, CNPJ_PETROBRAS);
        }

        @Test
        @DisplayName("Deve listar apenas empresas ativas")
        void deveListarApenasEmpresasAtivas() {
            List<Empresa> ativas = List.of(empresaFixtura(CNPJ_EMPRESA_A, "Empresa A"));
            when(empresaDataProvider.listarAtivas()).thenReturn(ativas);

            List<Empresa> resultado = useCase.executarApenasAtivas();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).ativa()).isTrue();
        }
    }

    @Nested
    @DisplayName("InativarEmpresaUseCase")
    class InativarEmpresa {

        private InativarEmpresaUseCase useCase;

        @BeforeEach
        void setUp() {
            useCase = new InativarEmpresaUseCase(empresaDataProvider);
        }

        @Test
        @DisplayName("Deve inativar empresa existente")
        void deveInativarEmpresaExistente() {
            Empresa ativa = empresaFixtura(CNPJ_FORNECEDOR_A, "Fornecedor A");
            Empresa inativada = ativa.inativar();

            when(empresaDataProvider.buscarPorCnpj(CNPJ_FORNECEDOR_A)).thenReturn(Optional.of(ativa));
            when(empresaDataProvider.salvar(any())).thenReturn(inativada);

            Empresa resultado = useCase.executar(CNPJ_FORNECEDOR_A);

            assertThat(resultado.ativa()).isFalse();
            verify(empresaDataProvider).salvar(any());
        }

        @Test
        @DisplayName("Deve lançar exceção ao inativar empresa inexistente")
        void deveLancarExcecaoParaEmpresaInexistente() {
            when(empresaDataProvider.buscarPorCnpj(CNPJ_FORNECEDOR_B)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.executar(CNPJ_FORNECEDOR_B))
                .isInstanceOf(EmpresaNaoEncontradaException.class);
        }
    }
}
