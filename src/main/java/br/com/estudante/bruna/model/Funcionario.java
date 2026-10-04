package br.com.estudante.bruna.model;

/**
 * Funcionário da empresa. Imutável: para "alterar", cria-se outro valor.
 * O id é 0 enquanto o funcionário ainda não foi gravado no banco.
 */
public record Funcionario(int id, String nome, int matricula, int departamento) {

    public static Funcionario novo(String nome, int matricula, int departamento) {
        return new Funcionario(0, nome, matricula, departamento);
    }

    public Funcionario comId(int novoId) {
        return new Funcionario(novoId, nome, matricula, departamento);
    }
}
