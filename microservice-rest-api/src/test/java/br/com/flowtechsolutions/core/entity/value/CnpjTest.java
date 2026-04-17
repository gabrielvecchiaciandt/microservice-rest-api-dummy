package br.com.flowtechsolutions.core.entity.value;

import br.com.flowtechsolutions.core.entity.exception.CnpjInvalidoException;
import br.com.flowtechsolutions.core.entity.value.Cnpj;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes para o value object Cnpj no formato alfanumérico (pós-2026).
 *
 * CNPJs válidos utilizados nestes testes (formato AA.AAA.AAA/AAAA-##):
 *   A1.B2C.3D4/0001-11
 *   11.222.333/0002-62
 *   12.345.678/0001-95
 *   33.000.167/0001-01  (Petrobras)
 *   45.678.901/0001-75
 *   60.701.190/0001-04  (Itaú Unibanco)
 *   98.765.432/0001-98
 */
@DisplayName("Cnpj (formato alfanumérico)")
class CnpjTest {

    @Nested
    @DisplayName("CNPJs válidos")
    class CnpjsValidos {

        @ParameterizedTest(name = "CNPJ formatado: {0}")
        @ValueSource(strings = {
            "A1.B2C.3D4/0001-11",
            "11.222.333/0002-62",
            "12.345.678/0001-95",
            "33.000.167/0001-01",
            "45.678.901/0001-75",
            "60.701.190/0001-04",
            "98.765.432/0001-98"
        })
        @DisplayName("Deve aceitar CNPJ formatado válido")
        void deveAceitarCnpjFormatado(String cnpj) {
            Cnpj resultado = new Cnpj(cnpj);
            assertThat(resultado.formatado()).isEqualTo(cnpj);
        }

        @ParameterizedTest(name = "CNPJ somente dígitos: {0}")
        @ValueSource(strings = {
            "A1B2C3D4000111",
            "11222333000262",
            "12345678000195",
            "33000167000101",
            "45678901000175",
            "60701190000104",
            "98765432000198"
        })
        @DisplayName("Deve aceitar CNPJ com somente dígitos")
        void deveAceitarCnpjSoDigitos(String cnpj) {
            Cnpj resultado = new Cnpj(cnpj);
            assertThat(resultado.soDigitos()).isEqualTo(cnpj);
        }

        @Test
        @DisplayName("Deve retornar formato com máscara AA.AAA.AAA/AAAA-##")
        void deveRetornarFormatoComMascara() {
            Cnpj cnpj = new Cnpj("A1B2C3D4000111");
            assertThat(cnpj.formatado()).isEqualTo("A1.B2C.3D4/0001-11");
        }

        @Test
        @DisplayName("Deve retornar somente os 14 caracteres")
        void deveRetornarSomenteDigitos() {
            Cnpj cnpj = new Cnpj("A1.B2C.3D4/0001-11");
            assertThat(cnpj.soDigitos()).isEqualTo("A1B2C3D4000111");
            assertThat(cnpj.soDigitos()).hasSize(14);
            assertThat(cnpj.soDigitos()).matches("[A-Z0-9]{12}\\d{2}");
        }

        @Test
        @DisplayName("CNPJ com dígito verificador 0 deve ser válido (ex: Petrobras)")
        void cnpjComDigitoVerificadorZeroDeveSerValido() {
            // 33.000.167/0001-01 — primeiro dígito verificador = 0
            assertThat(new Cnpj("33000167000101").soDigitos()).isEqualTo("33000167000101");
        }

        @Test
        @DisplayName("Deve ser imutável — dois Cnpj com mesmo valor são iguais")
        void deveSerImutavel() {
            Cnpj a = new Cnpj("A1B2C3D4000111");
            Cnpj b = new Cnpj("A1.B2C.3D4/0001-11");
            assertThat(a.soDigitos()).isEqualTo(b.soDigitos());
        }
    }

    @Nested
    @DisplayName("CNPJs inválidos")
    class CnpjsInvalidos {

        @Test
        @DisplayName("Deve rejeitar CNPJ nulo")
        void deveRejeitarNulo() {
            assertThatThrownBy(() -> new Cnpj(null))
                .isInstanceOf(CnpjInvalidoException.class);
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ em branco")
        void deveRejeitarEmBranco() {
            assertThatThrownBy(() -> new Cnpj("   "))
                .isInstanceOf(CnpjInvalidoException.class);
        }

        @ParameterizedTest(name = "CNPJ com todos dígitos iguais: {0}")
        @ValueSource(strings = {
            "00000000000000",
            "11111111111111",
            "22222222222222",
            "99999999999999"
        })
        @DisplayName("Deve rejeitar CNPJ com todos os dígitos iguais")
        void deveRejeitarTodosDigitosIguais(String cnpj) {
            assertThatThrownBy(() -> new Cnpj(cnpj))
                .isInstanceOf(CnpjInvalidoException.class)
                .hasMessageContaining("todos os dígitos são iguais");
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com dígitos verificadores incorretos")
        void deveRejeitarDigitosVerificadoresIncorretos() {
            // 11.222.333/0001-00 — dígitos verificadores deveriam ser 81
            assertThatThrownBy(() -> new Cnpj("11222333000100"))
                .isInstanceOf(CnpjInvalidoException.class);
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com menos de 14 dígitos")
        void deveRejeitarMenosDe14Digitos() {
            assertThatThrownBy(() -> new Cnpj("1122233300018"))
                .isInstanceOf(CnpjInvalidoException.class);
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com mais de 14 dígitos")
        void deveRejeitarMaisDe14Digitos() {
            assertThatThrownBy(() -> new Cnpj("112223330001810"))
                .isInstanceOf(CnpjInvalidoException.class);
        }

        @ParameterizedTest(name = "CNPJ com dígito verificador não-numérico: {0}")
        @ValueSource(strings = {
            "A1.B2C.3D4/0001-A1",
            "A1B2C3D4E5F6G7",
            "1122233300018A"
        })
        @DisplayName("Deve rejeitar CNPJ com dígito verificador não-numérico")
        void deveRejeitarCnpjComDigitoVerificadorNaoNumerico(String cnpj) {
            assertThatThrownBy(() -> new Cnpj(cnpj))
                .isInstanceOf(CnpjInvalidoException.class);
        }
    }
}
