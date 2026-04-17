package br.com.flowtechsolutions.dataproviders.database;

import br.com.flowtechsolutions.core.entity.Empresa;
import br.com.flowtechsolutions.core.entity.value.Cnpj;
import br.com.flowtechsolutions.dataproviders.database.entity.EmpresaEntity;
import br.com.flowtechsolutions.dataproviders.database.repository.EmpresaRepository;
import br.com.flowtechsolutions.fixtures.CnpjFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Testes do EmpresaDataProviderImpl.
 * Verifica que o CNPJ é armazenado como 14 dígitos numéricos e reconstituído corretamente.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EmpresaDataProviderImpl")
class EmpresaDataProviderTest {

    @Mock
    private EmpresaRepository empresaRepository;

    private EmpresaDataProviderImpl dataProvider;

    @BeforeEach
    void setUp() {
        dataProvider = new EmpresaDataProviderImpl(empresaRepository);
    }

    private EmpresaEntity entityFixtura(Long id, String cnpjDigitos, String razaoSocial) {
        return new EmpresaEntity(id, cnpjDigitos, razaoSocial, razaoSocial,
                                 "contato@empresa.com.br", "(11) 3000-0000",
                                 true, LocalDateTime.of(2026, 2, 10, 9, 0));
    }

    @Test
    @DisplayName("Deve salvar empresa com CNPJ armazenado como 14 dígitos numéricos")
    void deveSalvarComCnpjSoDigitos() {
        Empresa empresa = new Empresa(
            CnpjFixtures.CNPJ_EMPRESA_A_MATRIZ,
            "Empresa A LTDA", "Empresa A", null, null
        );
        EmpresaEntity entitySalva = entityFixtura(1L, CnpjFixtures.CNPJ_EMPRESA_A_MATRIZ_DIGITOS, "Empresa A LTDA");
        when(empresaRepository.save(any())).thenReturn(entitySalva);

        Empresa resultado = dataProvider.salvar(empresa.comIdEDataCadastro(null, LocalDateTime.now()));

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.cnpj().soDigitos()).isEqualTo(CnpjFixtures.CNPJ_EMPRESA_A_MATRIZ_DIGITOS);
        assertThat(resultado.cnpj().formatado()).isEqualTo(CnpjFixtures.CNPJ_EMPRESA_A_MATRIZ_FORMATADO);
    }

    @Test
    @DisplayName("Deve buscar empresa por CNPJ e reconstruir value object Cnpj")
    void deveBuscarPorCnpjEReconstruirValueObject() {
        EmpresaEntity entity = entityFixtura(2L, CnpjFixtures.CNPJ_PETROBRAS_DIGITOS, "Petrobras");
        when(empresaRepository.findByCnpj(CnpjFixtures.CNPJ_PETROBRAS_DIGITOS))
            .thenReturn(Optional.of(entity));

        Optional<Empresa> resultado = dataProvider.buscarPorCnpj(CnpjFixtures.CNPJ_PETROBRAS_DIGITOS);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().cnpj()).isInstanceOf(Cnpj.class);
        assertThat(resultado.get().cnpj().formatado()).isEqualTo(CnpjFixtures.CNPJ_PETROBRAS_FORMATADO);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty() quando empresa não encontrada pelo CNPJ")
    void deveRetornarVazioQuandoNaoEncontrada() {
        when(empresaRepository.findByCnpj(CnpjFixtures.CNPJ_FORNECEDOR_GAMMA_DIGITOS))
            .thenReturn(Optional.empty());

        Optional<Empresa> resultado = dataProvider.buscarPorCnpj(CnpjFixtures.CNPJ_FORNECEDOR_GAMMA_DIGITOS);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Deve listar todas as empresas reconstruindo CNPJs numéricos")
    void deveListarTodasEmpresas() {
        List<EmpresaEntity> entities = List.of(
            entityFixtura(1L, CnpjFixtures.CNPJ_EMPRESA_A_MATRIZ_DIGITOS, "Empresa A"),
            entityFixtura(2L, CnpjFixtures.CNPJ_FORNECEDOR_ALPHA_DIGITOS, "Fornecedor Alpha"),
            entityFixtura(3L, CnpjFixtures.CNPJ_ITAU_DIGITOS, "Itaú Unibanco")
        );
        when(empresaRepository.findAll()).thenReturn(entities);

        List<Empresa> resultado = dataProvider.listarTodas();

        assertThat(resultado).hasSize(3);
        assertThat(resultado).extracting(e -> e.cnpj().soDigitos())
            .containsExactly(
                CnpjFixtures.CNPJ_EMPRESA_A_MATRIZ_DIGITOS,
                CnpjFixtures.CNPJ_FORNECEDOR_ALPHA_DIGITOS,
                CnpjFixtures.CNPJ_ITAU_DIGITOS
            );
        // Todos os CNPJs devem estar no formato alfanumérico (12) e numérico (2)
        resultado.forEach(e ->
            assertThat(e.cnpj().soDigitos()).matches("[A-Z0-9]{12}\\d{2}")
        );
    }

    @Test
    @DisplayName("Deve verificar existência de empresa por CNPJ")
    void deveVerificarExistenciaPorCnpj() {
        when(empresaRepository.existsByCnpj(CnpjFixtures.CNPJ_FORNECEDOR_BETA_DIGITOS)).thenReturn(true);
        when(empresaRepository.existsByCnpj(CnpjFixtures.CNPJ_FORNECEDOR_GAMMA_DIGITOS)).thenReturn(false);

        assertThat(dataProvider.existePorCnpj(CnpjFixtures.CNPJ_FORNECEDOR_BETA_DIGITOS)).isTrue();
        assertThat(dataProvider.existePorCnpj(CnpjFixtures.CNPJ_FORNECEDOR_GAMMA_DIGITOS)).isFalse();
    }
}
