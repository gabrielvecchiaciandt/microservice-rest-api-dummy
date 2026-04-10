package br.com.flowtechsolutions.core.entity.value;

import br.com.flowtechsolutions.core.entity.exception.CnpjInvalidoException;

/**
 * Value object representando um CNPJ no formato numérico (14 dígitos).
 * Formato: AA.AAA.AAA/AAAA-##
 *
 * Suporta tanto o formato numérico legado quanto o novo formato alfanumérico
 * (a partir de junho/2026).
 */
public record Cnpj(String valor) {

    /** Padrão formatado: AA.AAA.AAA/AAAA-## */
    public static final String FORMATO_MASCARA = "[A-Za-z0-9]{2}\\.([A-Za-z0-9]{3})\\.([A-Za-z0-9]{3})/([A-Za-z0-9]{4})-\\d{2}";

    /** Padrão somente dígitos (12 alfanuméricos + 2 numéricos) */
    public static final String FORMATO_SOMENTE_DIGITOS = "[A-Za-z0-9]{12}\\d{2}";

    public Cnpj {
        if (valor == null || valor.isBlank()) {
            throw new CnpjInvalidoException("CNPJ não pode ser nulo ou vazio");
        }
        String soDigitos = removerFormatacao(valor);
        validarDigitos(soDigitos);
    }

    /**
     * Retorna o CNPJ no formato AA.AAA.AAA/AAAA-##.
     */
    public String formatado() {
        String d = removerFormatacao(valor);
        return d.substring(0, 2) + "." +
               d.substring(2, 5) + "." +
               d.substring(5, 8) + "/" +
               d.substring(8, 12) + "-" +
               d.substring(12, 14);
    }

    /**
     * Retorna apenas os 14 dígitos numéricos, sem formatação.
     */
    public String soDigitos() {
        return removerFormatacao(valor);
    }

    // --- Validação interna ---

    private static String removerFormatacao(String cnpj) {
        return cnpj.replaceAll("[.\\-/]", "");
    }

    private static void validarDigitos(String cnpj) {
        if (!cnpj.matches("[a-zA-Z0-9]{12}[0-9]{2}")) {
            throw new CnpjInvalidoException(
                "CNPJ deve ter 12 caracteres alfanuméricos e 2 dígitos verificadores numéricos: " + cnpj);
        }
        if (cnpj.chars().distinct().count() == 1) {
            throw new CnpjInvalidoException("CNPJ inválido: todos os dígitos são iguais: " + cnpj);
        }
        if (!verificarPrimeiroDigito(cnpj) || !verificarSegundoDigito(cnpj)) {
            throw new CnpjInvalidoException("CNPJ com dígitos verificadores inválidos: " + cnpj);
        }
    }

    private static boolean verificarPrimeiroDigito(String cnpj) {
        int[] pesos = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += Character.getNumericValue(Character.toUpperCase(cnpj.charAt(i))) * pesos[i];
        }
        int resto = soma % 11;
        int digito = (resto < 2) ? 0 : (11 - resto);
        return digito == Character.getNumericValue(cnpj.charAt(12));
    }

    private static boolean verificarSegundoDigito(String cnpj) {
        int[] pesos = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += Character.getNumericValue(Character.toUpperCase(cnpj.charAt(i))) * pesos[i];
        }
        int resto = soma % 11;
        int digito = (resto < 2) ? 0 : (11 - resto);
        return digito == Character.getNumericValue(cnpj.charAt(13));
    }
}
