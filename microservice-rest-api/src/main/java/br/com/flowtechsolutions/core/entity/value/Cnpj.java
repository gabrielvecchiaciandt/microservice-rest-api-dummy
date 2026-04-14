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
        // Normaliza: remove formatação e converte para MAIÚSCULAS
        valor = removerFormatacao(valor).toUpperCase();
        validarDigitos(valor);
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


    // --- Validação interna ---

    private static String removerFormatacao(String cnpj) {
        return cnpj.replaceAll("[.\\-/]", "");
    }

    private static void validarDigitos(String cnpj) {
        if (!cnpj.matches(FORMATO_SOMENTE_DIGITOS)) {
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

    /**
     * Calcula e verifica os dígitos verificadores do CNPJ.
     * Utiliza o algoritmo oficial do governo brasileiro para CNPJs alfanuméricos.
     * 
     * Conversão de caracteres:
     * - '0'-'9' → valores 0-9
     * - 'A'-'Z' → valores 17-42 (usando subtração ASCII: char - '0')
     * 
     * Pesos: {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2}
     * Fórmula: DV = (soma % 11 < 2) ? 0 : 11 - (soma % 11)
     */
    private static boolean verificarPrimeiroDigito(String cnpj) {
        return calcularDigito(cnpj.substring(0, 12)) == Character.getNumericValue(cnpj.charAt(12));
    }

    private static boolean verificarSegundoDigito(String cnpj) {
        return calcularDigito(cnpj.substring(0, 13)) == Character.getNumericValue(cnpj.charAt(13));
    }

    /**
     * Calcula um dígito verificador usando o algoritmo oficial do BACEN.
     * Implementação baseada no CNPJValidator.java do governo brasileiro.
     */
    private static int calcularDigito(String baseCnpj) {
        int[] pesos = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        
        for (int indice = baseCnpj.length() - 1; indice >= 0; indice--) {
            // Conversão usando subtração ASCII (oficial do governo)
            int valorCaracter = (int) baseCnpj.charAt(indice) - (int) '0';
            soma += valorCaracter * pesos[pesos.length - baseCnpj.length() + indice];
        }
        
        return soma % 11 < 2 ? 0 : 11 - (soma % 11);
    }
}
