package br.com.estudante.bruna.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

    private String servidor, banco, user, password;
    public Connection conexao;

    public Conexao() {
        this.servidor = "localhost";
        this.banco = "db_Java";
        this.user = "root";
        this.password = "***SENHA-REMOVIDA***";
    }

    public Boolean connectDrive() {
        try {
            this.conexao = DriverManager.getConnection(
                    "jdbc:mysql://" + this.servidor + ":3306/" + this.banco + "?useSSL=false&serverTimezone=UTC",
                    this.user, this.password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() {
        Boolean validaConexao = connectDrive();
        if (validaConexao) {
            return this.conexao;
        }
        return null;
    }
}
