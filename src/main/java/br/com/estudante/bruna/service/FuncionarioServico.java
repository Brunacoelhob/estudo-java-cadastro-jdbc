package br.com.estudante.bruna.service;

import br.com.estudante.bruna.model.Funcionario;
import br.com.estudante.bruna.model.RegraDeNegocioException;
import br.com.estudante.bruna.repository.FuncionarioRepositorio;

import java.util.List;

/** Regras de negócio de funcionários: valida os dados antes de falar com o banco. */
public class FuncionarioServico {

    static final int TAMANHO_MAXIMO_NOME = 45;

    private final FuncionarioRepositorio repositorio;

    public FuncionarioServico(FuncionarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<Funcionario> listar() {
        return repositorio.listar();
    }

    public Funcionario cadastrar(String nome, int matricula, int departamento) {
        String nomeLimpo = nome == null ? "" : nome.trim();
        if (nomeLimpo.isEmpty()) {
            throw new RegraDeNegocioException("O nome é obrigatório.");
        }
        if (nomeLimpo.length() > TAMANHO_MAXIMO_NOME) {
            throw new RegraDeNegocioException("O nome deve ter no máximo " + TAMANHO_MAXIMO_NOME + " caracteres.");
        }
        if (matricula <= 0) {
            throw new RegraDeNegocioException("A matrícula deve ser um número positivo.");
        }
        if (!repositorio.existeDepartamento(departamento)) {
            throw new RegraDeNegocioException("Departamento " + departamento + " não existe.");
        }
        if (repositorio.existeMatricula(matricula)) {
            throw new RegraDeNegocioException("A matrícula " + matricula + " já está cadastrada.");
        }
        return repositorio.inserir(Funcionario.novo(nomeLimpo, matricula, departamento));
    }

    public void remover(int id) {
        if (!repositorio.remover(id)) {
            throw new RegraDeNegocioException("Funcionário " + id + " não encontrado.");
        }
    }
}
