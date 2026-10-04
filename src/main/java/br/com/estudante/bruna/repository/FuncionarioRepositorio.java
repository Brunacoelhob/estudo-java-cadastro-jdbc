package br.com.estudante.bruna.repository;

import br.com.estudante.bruna.model.Funcionario;

import java.util.List;

/** Acesso aos dados de funcionários. Falhas de infraestrutura viram {@link RepositorioException}. */
public interface FuncionarioRepositorio {

    List<Funcionario> listar();

    /** Grava e devolve o funcionário já com o id gerado pelo banco. */
    Funcionario inserir(Funcionario funcionario);

    /** @return {@code true} se havia um funcionário com esse id e ele foi removido. */
    boolean remover(int id);

    boolean existeMatricula(int matricula);

    boolean existeDepartamento(int idDepartamento);
}
