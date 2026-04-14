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
 * Testes para o value object Cnpj no formato numérico (14 dígitos).
 *
 * CNPJs válidos utilizados nestes testes:
 *   11222333000181 (11.222.333/0001-81) - Empresa fictícia matriz
 *   12345678000195 (12.345.678/0001-95) - Fornecedor Alpha LTDA
 *   33000167000101 (33.000.167/0001-01) - Petróleo Brasileiro S.A.
 *   60701190000104 (60.701.190/0001-04) - Itaú Unibanco S.A.
 */
@DisplayName("Cnpj (formato numérico)")
class CnpjTest {

    @Nested
    @DisplayName("CNPJs válidos")
    class CnpjsValidos {

        @ParameterizedTest(name = "CNPJ formatado: {0}")
        @ValueSource(strings = {
            "11.222.333/0001-81",
            "12.345.678/0001-95",
            "33.000.167/0001-01",
            "60.701.190/0001-04"
        })
        @DisplayName("Deve aceitar CNPJ formatado válido")
        void deveAceitarCnpjFormatado(String cnpj) {
            Cnpj resultado = new Cnpj(cnpj);
            assertThat(resultado.formatado()).isEqualTo(cnpj);
        }

        @ParameterizedTest(name = "CNPJ somente dígitos: {0}")
        @ValueSource(strings = {
            "11222333000181",
            "12345678000195",
            "33000167000101",
            "60701190000104"
        })
        @DisplayName("Deve aceitar CNPJ com somente dígitos")
        void deveAceitarCnpjSoDigitos(String cnpj) {
            Cnpj resultado = new Cnpj(cnpj);
            assertThat(resultado.valor()).isEqualTo(cnpj);
        }

        @Test
        @DisplayName("Deve retornar formato com máscara XX.XXX.XXX/XXXX-XX")
        void deveRetornarFormatoComMascara() {
            Cnpj cnpj = new Cnpj("11222333000181");
            assertThat(cnpj.formatado()).isEqualTo("11.222.333/0001-81");
        }

        @Test
        @DisplayName("Deve retornar somente os 14 dígitos")
        void deveRetornarSomenteDigitos() {
            Cnpj cnpj = new Cnpj("11.222.333/0001-81");
            // O valor é normalizado (sem formatação) internamente
            assertThat(cnpj.valor()).isEqualTo("11222333000181");
            assertThat(cnpj.valor()).hasSize(14);
            assertThat(cnpj.valor()).matches("\\d{14}");
        }

        @Test
        @DisplayName("CNPJ com dígito verificador 0 deve ser válido (ex: Petrobras)")
        void cnpjComDigitoVerificadorZeroDeveSerValido() {
            // 33.000.167/0001-01 — Petrobras com dígito verificador 01
            assertThat(new Cnpj("33000167000101").valor()).isEqualTo("33000167000101");
        }

        @Test
        @DisplayName("Deve ser imutável — dois Cnpj com mesmo valor são iguais")
        void deveSerImutavel() {
            Cnpj a = new Cnpj("11222333000181");
            Cnpj b = new Cnpj("11.222.333/0001-81");
            // Ambos são normalizados para o mesmo valor
            assertThat(a.valor()).isEqualTo(b.valor());
        }

        @ParameterizedTest(name = "CNPJ alfanumérico válido: {0}")
        @ValueSource(strings = {
            "12ABC34501DE35",
            "1345C3A5000106",
            "R55231B3000757",
            "90.021.382/0001-22",
            "90.024.778/000123"
        })
        @DisplayName("Deve aceitar CNPJs alfanuméricos válidos (exemplos oficiais do governo)")
        void deveAceitarCnpjsAlfanumericosValidos(String cnpj) {
            Cnpj resultado = new Cnpj(cnpj);
            assertThat(resultado.valor()).isNotNull();
        }

        @Test
        @DisplayName("Deve normalizar CNPJs com letras minúsculas para MAIÚSCULAS")
        void deveNormalizarParaMaiusculas() {
            Cnpj cnpjMinuscula = new Cnpj("12abc34501de35");
            Cnpj cnpjMaiuscula = new Cnpj("12ABC34501DE35");
            assertThat(cnpjMinuscula.valor()).isEqualTo(cnpjMaiuscula.valor());
            assertThat(cnpjMinuscula.valor()).isEqualTo("12ABC34501DE35");
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
            // 11.222.333/0001-00 — dígitos verificadores incorretos (deveria ser -81)
            assertThatThrownBy(() -> new Cnpj("11222333000100"))
                .isInstanceOf(CnpjInvalidoException.class);
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com menos de 14 caracteres")
        void deveRejeitarMenosDe14Digitos() {
            assertThatThrownBy(() -> new Cnpj("1122233300018"))
                .isInstanceOf(CnpjInvalidoException.class);
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com mais de 14 caracteres")
        void deveRejeitarMaisDe14Digitos() {
            assertThatThrownBy(() -> new Cnpj("112223330001811"))
                .isInstanceOf(CnpjInvalidoException.class);
        }

        @ParameterizedTest(name = "CNPJ alfanumérico inválido: {0}")
        @ValueSource(strings = {
            "R55231B3000700",     // DV incorreto (deveria ser 57)
            "90.025.108/000101",  // DV incorreto (deveria ser 21)
            "90.025.255/0001",    // Tamanho inválido (falta DV)
            "90.024.420/0001A2"   // Letra no DV (deve ser numérico)
        })
        @DisplayName("Deve rejeitar CNPJs alfanuméricos inválidos (exemplos oficiais do governo)")
        void deveRejeitarCnpjsAlfanumericosInvalidos(String cnpj) {
            assertThatThrownBy(() -> new Cnpj(cnpj))
                .isInstanceOf(CnpjInvalidoException.class);
        }
    }
}