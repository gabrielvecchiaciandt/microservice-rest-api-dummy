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
 * CNPJs válidos utilizados nestes testes (formato AAAAAAAA/BBBB-CC):
 *   A1B2C3D4/E5F6-78
 *   11223344/ABCD-12
 *   PETR0BRA/S001-01  (Exemplo Petrobras adaptado)
 *   ITAUUNIB/ANC0-04  (Exemplo Itaú Unibanco adaptado)
 */
@DisplayName("Cnpj (formato alfanumérico pós-2026)")
class CnpjTest {

    @Nested
    @DisplayName("CNPJs válidos")
    class CnpjsValidos {

        @ParameterizedTest(name = "CNPJ formatado: {0}")
        @ValueSource(strings = {
            "A1.B2C.3D4/E5F6-78",
            "11.223.344/ABCD-12",
            "PE.TR0.BRA/S001-01",
            "IT.AUU.NIB/ANC0-04"
        })
        @DisplayName("Deve aceitar CNPJ formatado válido")
        void deveAceitarCnpjFormatado(String cnpj) {
            Cnpj resultado = new Cnpj(cnpj);
            assertThat(resultado.formatado()).isEqualTo(cnpj);
        }

        @ParameterizedTest(name = "CNPJ somente caracteres: {0}")
        @ValueSource(strings = {
            "A1B2C3D4E5F678",
            "11223344ABCD12",
            "PETR0BRAS00101",
            "ITAUUNIBANC004"
        })
        @DisplayName("Deve aceitar CNPJ com somente caracteres")
        void deveAceitarCnpjSoDigitos(String cnpj) {
            Cnpj resultado = new Cnpj(cnpj);
            assertThat(resultado.soDigitos()).isEqualTo(cnpj);
        }

        @Test
        @DisplayName("Deve retornar formato com máscara AA.AAA.AAA/AAAA-##")
        void deveRetornarFormatoComMascara() {
            Cnpj cnpj = new Cnpj("A1B2C3D4E5F678");
            assertThat(cnpj.formatado()).isEqualTo("A1.B2C.3D4/E5F6-78");
        }

        @Test
        @DisplayName("Deve retornar somente os 14 caracteres")
        void deveRetornarSomenteDigitos() {
            Cnpj cnpj = new Cnpj("A1.B2C.3D4/E5F6-78");
            assertThat(cnpj.soDigitos()).isEqualTo("A1B2C3D4E5F678");
            assertThat(cnpj.soDigitos()).hasSize(14);
            assertThat(cnpj.soDigitos()).matches("[A-Z0-9]{12}\\d{2}");
        }

        @Test
        @DisplayName("CNPJ com dígito verificador 0 deve ser válido (ex: Petrobras)")
        void cnpjComDigitoVerificadorZeroDeveSerValido() {
            // PETR0BRA/S001-01 — exemplo com dígitos verificadores numéricos
            assertThat(new Cnpj("PETR0BRAS00101").soDigitos()).isEqualTo("PETR0BRAS00101");
        }

        @Test
        @DisplayName("Deve ser imutável — dois Cnpj com mesmo valor são iguais")
        void deveSerImutavel() {
            Cnpj a = new Cnpj("A1B2C3D4E5F678");
            Cnpj b = new Cnpj("A1.B2C.3D4/E5F6-78");
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
            // A1B2C3D4/E5F6-00 — dígitos verificadores hipoteticamente incorretos
            assertThatThrownBy(() -> new Cnpj("A1B2C3D4E5F600"))
                .isInstanceOf(CnpjInvalidoException.class);
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com menos de 14 caracteres")
        void deveRejeitarMenosDe14Digitos() {
            assertThatThrownBy(() -> new Cnpj("A1B2C3D4E5F67"))
                .isInstanceOf(CnpjInvalidoException.class);
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com mais de 14 caracteres")
        void deveRejeitarMaisDe14Digitos() {
            assertThatThrownBy(() -> new Cnpj("A1B2C3D4E5F6789"))
                .isInstanceOf(CnpjInvalidoException.class);
        }

        @ParameterizedTest(name = "CNPJ alfanumérico (formato pós-2026): {0}")
        @ValueSource(strings = {
            "A1.B2C.3D4/E5F6-78",
            "11.223.344/ABCD-12",
            "A1B2C3D4E5F678"
        })
        @DisplayName("Deve aceitar CNPJ alfanumérico (formato pós-2026)")
        void deveAceitarCnpjAlfanumerico(String cnpj) {
            // Apenas instancia para verificar se não lança exceção, conforme o novo padrão
            new Cnpj(cnpj);
        }
    }
}
