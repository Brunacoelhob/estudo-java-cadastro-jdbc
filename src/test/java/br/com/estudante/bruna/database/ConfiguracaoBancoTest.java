package br.com.estudante.bruna.database;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracaoBancoTest {

    @Test
    void senhaEhObrigatoria() {
        var erro = assertThrows(IllegalStateException.class, () -> ConfiguracaoBanco.deAmbiente(Map.of()));
        assertTrue(erro.getMessage().contains("DB_PASSWORD"));
        assertThrows(IllegalStateException.class, () -> ConfiguracaoBanco.deAmbiente(Map.of("DB_PASSWORD", "")));
    }

    @Test
    void usaPadroesQuandoSoASenhaEhInformada() {
        var config = ConfiguracaoBanco.deAmbiente(Map.of("DB_PASSWORD", "x"));

        assertEquals("root", config.usuario());
        assertEquals(ConfiguracaoBanco.URL_PADRAO, config.url());
    }

    @Test
    void respeitaVariaveisInformadas() {
        var config = ConfiguracaoBanco.deAmbiente(
                Map.of("DB_PASSWORD", "x", "DB_USER", "app", "DB_URL", "jdbc:mysql://db:3306/outro"));

        assertEquals("app", config.usuario());
        assertEquals("jdbc:mysql://db:3306/outro", config.url());
    }

    @Test
    void toStringNaoVazaSenha() {
        var config = ConfiguracaoBanco.deAmbiente(Map.of("DB_PASSWORD", "segredo-super"));

        assertFalse(config.toString().contains("segredo-super"));
    }
}
