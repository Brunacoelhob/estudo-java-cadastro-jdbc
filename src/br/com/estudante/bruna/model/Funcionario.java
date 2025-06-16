package br.com.estudante.bruna.model;

import java.util.Scanner;

public class Funcionario {

    private int idFuncionario;
    private String nome;
    private int departamento;
    private int matricula;

    Scanner scanner = new Scanner(System.in);

    public void cadastrarFuncionario() {
        System.out.println("Cadastro");
        System.out.print("Informe o nome: ");
        this.nome = scanner.nextLine();

        System.out.print("Informe a matricula: ");
        this.matricula = scanner.nextInt();

        System.out.print("Informe o Departamento [1- DEV  2 - QA  3 - ENG]: ");
        this.departamento = scanner.nextInt();
        scanner.nextLine(); // limpa buffer
    }

    public void exibirInformacoes() {
        System.out.println("ID: " + this.idFuncionario);
        System.out.println("Nome: " + this.nome);
        System.out.println("Matrícula: " + this.matricula);
        System.out.println("Departamento: " + this.departamento);
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDepartamento() {
        return departamento;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
}
