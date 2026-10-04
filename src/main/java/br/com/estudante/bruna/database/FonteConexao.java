package br.com.estudante.bruna.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Abre conexões com o banco. Quem recebe a conexão é responsável por fechá-la (try-with-resources). */
@FunctionalInterface
public interface FonteConexao {

    Connection abrir() throws SQLException;

    static FonteConexao de(ConfiguracaoBanco config) {
        return () -> DriverManager.getConnection(config.url(), config.usuario(), config.senha());
    }
}
