package br.com.flowtechsolutions.fixtures;

import br.com.flowtechsolutions.core.entity.value.Cnpj;

/**
 * Fixtures de CNPJs numéricos válidos (formato legado, pré-2026) para uso em testes.
 *
 * Todos os CNPJs seguem o formato XXXXXXXXXXXXXX (14 dígitos numéricos) e possuem
 * dígitos verificadores calculados pelo algoritmo módulo 11 conforme a Receita Federal.
 *
 * ATENÇÃO: Nenhum destes CNPJs utiliza o novo formato alfanumérico previsto para junho/2026.
 * Esta classe representa o estado LEGADO que será migrado pela ferramenta de transformação.
 *
 * Tabela de CNPJs de teste:
 *
 *  Constante               Somente dígitos    Formatado              Descrição
 *  ----------------------- ------------------ ---------------------- -----------------
 *  CNPJ_EMPRESA_A_MATRIZ   11222333000181     11.222.333/0001-81     Empresa fictícia — matriz
 *  CNPJ_EMPRESA_A_FILIAL   11222333000262     11.222.333/0002-62     Empresa fictícia — filial
 *  CNPJ_FORNECEDOR_ALPHA   12345678000195     12.345.678/0001-95     Fornecedor Alpha LTDA
 *  CNPJ_FORNECEDOR_BETA    45678901000175     45.678.901/0001-75     Fornecedor Beta S.A.
 *  CNPJ_FORNECEDOR_GAMMA   98765432000198     98.765.432/0001-98     Fornecedor Gamma ME
 *  CNPJ_PETROBRAS          33000167000101     33.000.167/0001-01     Petróleo Brasileiro S.A.
 *  CNPJ_ITAU               60701190000104     60.701.190/0001-04     Itaú Unibanco S.A.
 */
public final class CnpjFixtures {

    private CnpjFixtures() {
    }

    // --- Somente dígitos (14 chars numéricos) ---

    /** 11.222.333/0001-81 — empresa fictícia, matriz */
    public static final String CNPJ_EMPRESA_A_MATRIZ_DIGITOS = "11222333000181";

    /** 11.222.333/0002-62 — empresa fictícia, filial */
    public static final String CNPJ_EMPRESA_A_FILIAL_DIGITOS = "11222333000262";

    /** 12.345.678/0001-95 — Fornecedor Alpha LTDA */
    public static final String CNPJ_FORNECEDOR_ALPHA_DIGITOS = "12345678000195";

    /** 45.678.901/0001-75 — Fornecedor Beta S.A. */
    public static final String CNPJ_FORNECEDOR_BETA_DIGITOS  = "45678901000175";

    /** 98.765.432/0001-98 — Fornecedor Gamma ME */
    public static final String CNPJ_FORNECEDOR_GAMMA_DIGITOS = "98765432000198";

    /** 33.000.167/0001-01 — Petróleo Brasileiro S.A. */
    public static final String CNPJ_PETROBRAS_DIGITOS        = "33000167000101";

    /** 60.701.190/0001-04 — Itaú Unibanco S.A. */
    public static final String CNPJ_ITAU_DIGITOS             = "60701190000104";

    // --- Formatado (XX.XXX.XXX/YYYY-ZZ) ---

    public static final String CNPJ_EMPRESA_A_MATRIZ_FORMATADO = "11.222.333/0001-81";
    public static final String CNPJ_EMPRESA_A_FILIAL_FORMATADO = "11.222.333/0002-62";
    public static final String CNPJ_FORNECEDOR_ALPHA_FORMATADO = "12.345.678/0001-95";
    public static final String CNPJ_FORNECEDOR_BETA_FORMATADO  = "45.678.901/0001-75";
    public static final String CNPJ_FORNECEDOR_GAMMA_FORMATADO = "98.765.432/0001-98";
    public static final String CNPJ_PETROBRAS_FORMATADO        = "33.000.167/0001-01";
    public static final String CNPJ_ITAU_FORMATADO             = "60.701.190/0001-04";

    // --- Value objects prontos para uso ---

    public static final Cnpj CNPJ_EMPRESA_A_MATRIZ = new Cnpj(CNPJ_EMPRESA_A_MATRIZ_DIGITOS);
    public static final Cnpj CNPJ_EMPRESA_A_FILIAL = new Cnpj(CNPJ_EMPRESA_A_FILIAL_DIGITOS);
    public static final Cnpj CNPJ_FORNECEDOR_ALPHA = new Cnpj(CNPJ_FORNECEDOR_ALPHA_DIGITOS);
    public static final Cnpj CNPJ_FORNECEDOR_BETA  = new Cnpj(CNPJ_FORNECEDOR_BETA_DIGITOS);
    public static final Cnpj CNPJ_FORNECEDOR_GAMMA = new Cnpj(CNPJ_FORNECEDOR_GAMMA_DIGITOS);
    public static final Cnpj CNPJ_PETROBRAS        = new Cnpj(CNPJ_PETROBRAS_DIGITOS);
    public static final Cnpj CNPJ_ITAU             = new Cnpj(CNPJ_ITAU_DIGITOS);

    // --- CNPJs inválidos para teste de rejeição ---

    /** CNPJ com dígitos verificadores incorretos */
    public static final String CNPJ_INVALIDO_DIGITO_VERIFICADOR = "11222333000100";

    /** CNPJ com todos os dígitos iguais */
    public static final String CNPJ_INVALIDO_TODOS_IGUAIS = "11111111111111";

    /** CNPJ com menos de 14 dígitos */
    public static final String CNPJ_INVALIDO_CURTO = "1122233300018";

    /** CNPJ alfanumérico — novo formato 2026, não suportado neste sistema legado */
    public static final String CNPJ_INVALIDO_ALFANUMERICO = "1A.ABC.DEF/0001-XX";
}
