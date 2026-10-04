package br.com.estudante.bruna.repository;

import br.com.estudante.bruna.database.FonteConexao;
import br.com.estudante.bruna.model.Funcionario;
import br.com.estudante.bruna.model.RegraDeNegocioException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Implementação JDBC. Todo SQL é parametrizado (PreparedStatement) e todo recurso é fechado automaticamente. */
public class FuncionarioRepositorioJdbc implements FuncionarioRepositorio {

    private final FonteConexao fonte;

    public FuncionarioRepositorioJdbc(FonteConexao fonte) {
        this.fonte = fonte;
    }

    @Override
    public List<Funcionario> listar() {
        String sql = "SELECT idFuncionario, nome, matricula, departamento_FK FROM funcionario ORDER BY idFuncionario";
        try (Connection c = fonte.abrir();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Funcionario> resultado = new ArrayList<>();
            while (rs.next()) {
                resultado.add(new Funcionario(
                        rs.getInt("idFuncionario"),
                        rs.getString("nome"),
                        rs.getInt("matricula"),
                        rs.getInt("departamento_FK")));
            }
            return resultado;
        } catch (SQLException e) {
            throw new RepositorioException("Falha ao listar funcionários", e);
        }
    }

    @Override
    public Funcionario inserir(Funcionario f) {
        String sql = "INSERT INTO funcionario (nome, matricula, departamento_FK) VALUES (?, ?, ?)";
        try (Connection c = fonte.abrir();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.nome());
            ps.setInt(2, f.matricula());
            ps.setInt(3, f.departamento());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                return chaves.next() ? f.comId(chaves.getInt(1)) : f;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            // Corrida entre a verificação do serviço e o INSERT: o banco é a última linha de defesa.
            throw new RegraDeNegocioException("Matrícula já cadastrada ou departamento inexistente.");
        } catch (SQLException e) {
            throw new RepositorioException("Falha ao cadastrar funcionário", e);
        }
    }

    @Override
    public boolean remover(int id) {
        return executarAtualizacao("DELETE FROM funcionario WHERE idFuncionario = ?", id) > 0;
    }

    @Override
    public boolean existeMatricula(int matricula) {
        return existe("SELECT 1 FROM funcionario WHERE matricula = ?", matricula);
    }

    @Override
    public boolean existeDepartamento(int idDepartamento) {
        return existe("SELECT 1 FROM departamento WHERE idDepartamento = ?", idDepartamento);
    }

    private boolean existe(String sql, int parametro) {
        try (Connection c = fonte.abrir(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RepositorioException("Falha ao consultar o banco", e);
        }
    }

    private int executarAtualizacao(String sql, int parametro) {
        try (Connection c = fonte.abrir(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositorioException("Falha ao atualizar o banco", e);
        }
    }
}
