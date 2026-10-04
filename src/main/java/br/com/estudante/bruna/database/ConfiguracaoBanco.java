package br.com.estudante.bruna.database;

import java.util.Map;

/**
 * Configuração de acesso ao banco, lida de variáveis de ambiente (nunca do código-fonte).
 *
 * <ul>
 *   <li>{@code DB_URL} (opcional): padrão {@code jdbc:mysql://localhost:3306/db_java}</li>
 *   <li>{@code DB_USER} (opcional): padrão {@code root}</li>
 *   <li>{@code DB_PASSWORD} (obrigatória)</li>
 * </ul>
 */
public record ConfiguracaoBanco(String url, String usuario, String senha) {

    static final String URL_PADRAO = "jdbc:mysql://localhost:3306/db_java?serverTimezone=UTC";

    public static ConfiguracaoBanco deAmbiente(Map<String, String> ambiente) {
        String senha = ambiente.get("DB_PASSWORD");
        if (senha == null || senha.isEmpty()) {
            throw new IllegalStateException(
                    "Defina a variável de ambiente DB_PASSWORD com a senha do banco (veja o README).");
        }
        return new ConfiguracaoBanco(
                valorOu(ambiente.get("DB_URL"), URL_PADRAO),
                valorOu(ambiente.get("DB_USER"), "root"),
                senha);
    }

    private static String valorOu(String valor, String padrao) {
        return valor == null || valor.isBlank() ? padrao : valor;
    }

    /** Evita vazar a senha em logs ou stack traces. */
    @Override
    public String toString() {
        return "ConfiguracaoBanco[url=" + url + ", usuario=" + usuario + ", senha=***]";
    }
}
