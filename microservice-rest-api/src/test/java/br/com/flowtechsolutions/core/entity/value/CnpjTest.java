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
 * Testes para o value object Cnpj no formato numérico legado (pré-2026).
 *
 * CNPJs válidos utilizados nestes testes (formato XX.XXX.XXX/YYYY-ZZ):
 *   11.222.333/0001-81
 *   11.222.333/0002-62
 *   12.345.678/0001-95
 *   33.000.167/0001-01  (Petrobras)
 *   45.678.901/0001-75
 *   60.701.190/0001-04  (Itaú Unibanco)
 *   98.765.432/0001-98
 */
@DisplayName("Cnpj (formato numérico legado)")
class CnpjTest {

    @Nested
    @DisplayName("CNPJs válidos")
    class CnpjsValidos {

        @ParameterizedTest(name = "CNPJ formatado: {0}")
        @ValueSource(strings = {
            "11.222.333/0001-81",
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
            "11222333000181",
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
        @DisplayName("Deve retornar formato com máscara XX.XXX.XXX/YYYY-ZZ")
        void deveRetornarFormatoComMascara() {
            Cnpj cnpj = new Cnpj("11222333000181");
            assertThat(cnpj.formatado()).isEqualTo("11.222.333/0001-81");
        }

        @Test
        @DisplayName("Deve retornar somente os 14 dígitos")
        void deveRetornarSomenteDigitos() {
            Cnpj cnpj = new Cnpj("11.222.333/0001-81");
            assertThat(cnpj.soDigitos()).isEqualTo("11222333000181");
            assertThat(cnpj.soDigitos()).hasSize(14);
            assertThat(cnpj.soDigitos()).matches("\\d{14}");
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
            Cnpj a = new Cnpj("11222333000181");
            Cnpj b = new Cnpj("11.222.333/0001-81");
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

        @ParameterizedTest(name = "CNPJ alfanumérico (novo formato 2026 — não suportado): {0}")
        @ValueSource(strings = {
            "1A.ABC.DEF/0001-XX",
            "AB.CDE.FGH/0001-IJ",
            "A1B2C3D4E5F6G7"
        })
        @DisplayName("Deve rejeitar CNPJ alfanumérico (formato 2026 não é suportado)")
        void deveRejeitarCnpjAlfanumerico(String cnpj) {
            assertThatThrownBy(() -> new Cnpj(cnpj))
                .isInstanceOf(CnpjInvalidoException.class)
                .hasMessageContaining("apenas dígitos numéricos");
        }
    }
}
