package br.com.estudante.bruna.controller;

import br.com.estudante.bruna.database.Conexao;
import br.com.estudante.bruna.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class FuncionarioController {

    public void consultarFuncionarios() {
        try {
            Conexao conexao = new Conexao();
            Connection objetoConnection = conexao.getConnection();
            Statement statement = objetoConnection.createStatement();

            String queryConsulta = "SELECT * FROM funcionario";
            ResultSet resultSet = statement.executeQuery(queryConsulta);

            while (resultSet.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setIdFuncionario(resultSet.getInt("idFuncionario"));
                funcionario.setNome(resultSet.getString("nome"));
                funcionario.setMatricula(resultSet.getInt("matricula"));
                funcionario.setDepartamento(resultSet.getInt("departamento_FK"));

                funcionario.exibirInformacoes();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean cadastrarFuncionarios(Funcionario funcionario) {
        try {
            Conexao conexao = new Conexao();
            Connection objetoConnection = conexao.getConnection();

            String queryCreate = "INSERT INTO funcionario (nome, matricula, departamento_FK) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = objetoConnection.prepareStatement(queryCreate);

            preparedStatement.setString(1, funcionario.getNome());
            preparedStatement.setInt(2, funcionario.getMatricula());
            preparedStatement.setInt(3, funcionario.getDepartamento());

            preparedStatement.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deletarFuncionario(int id) {
        try {
            Conexao conexao = new Conexao();
            Connection objConnection = conexao.getConnection();

            String queryDelete = "DELETE FROM funcionario WHERE idFuncionario = ?";
            PreparedStatement preparedStatement = objConnection.prepareStatement(queryDelete);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
