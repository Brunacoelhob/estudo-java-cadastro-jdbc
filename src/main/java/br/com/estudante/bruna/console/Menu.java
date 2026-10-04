package br.com.estudante.bruna.console;

import br.com.estudante.bruna.model.Funcionario;
import br.com.estudante.bruna.model.RegraDeNegocioException;
import br.com.estudante.bruna.repository.RepositorioException;
import br.com.estudante.bruna.service.FuncionarioServico;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/** Interface de console. Entrada e saída são injetadas para permitir testes automatizados. */
public class Menu {

    private final FuncionarioServico servico;
    private final Scanner entrada;
    private final PrintStream saida;

    public Menu(FuncionarioServico servico, Scanner entrada, PrintStream saida) {
        this.servico = servico;
        this.entrada = entrada;
        this.saida = saida;
    }

    public void executar() {
        boolean sair = false;
        while (!sair) {
            saida.println("""

                    === Cadastro de Funcionários ===
                    1 - Listar
                    2 - Cadastrar
                    3 - Remover
                    4 - Sair""");
            Integer opcao = lerInteiro("Escolha uma opção: ");
            if (opcao == null) {
                return; // fim da entrada (Ctrl+D / pipe encerrado)
            }
            try {
                switch (opcao) {
                    case 1 -> listar();
                    case 2 -> cadastrar();
                    case 3 -> remover();
                    case 4 -> sair = true;
                    default -> saida.println("Opção inválida.");
                }
            } catch (RegraDeNegocioException e) {
                saida.println("Não foi possível concluir: " + e.getMessage());
            } catch (RepositorioException e) {
                // O detalhe técnico fica fora da tela do usuário; só uma mensagem amigável aparece.
                saida.println("Erro ao acessar o banco de dados. Tente novamente mais tarde.");
            }
        }
    }

    private void listar() {
        List<Funcionario> lista = servico.listar();
        if (lista.isEmpty()) {
            saida.println("Nenhum funcionário cadastrado.");
            return;
        }
        saida.printf("%-4s %-30s %-10s %s%n", "ID", "Nome", "Matrícula", "Depto");
        lista.forEach(f -> saida.printf("%-4d %-30s %-10d %d%n", f.id(), f.nome(), f.matricula(), f.departamento()));
    }

    private void cadastrar() {
        saida.print("Nome: ");
        String nome = entrada.hasNextLine() ? entrada.nextLine() : "";
        Integer matricula = lerInteiro("Matrícula: ");
        Integer departamento = lerInteiro("Departamento [1-DEV 2-QA 3-ENG]: ");
        if (matricula == null || departamento == null) {
            return;
        }
        Funcionario criado = servico.cadastrar(nome, matricula, departamento);
        saida.println("Funcionário cadastrado com id " + criado.id() + ".");
    }

    private void remover() {
        Integer id = lerInteiro("Id do funcionário: ");
        if (id == null) {
            return;
        }
        servico.remover(id);
        saida.println("Funcionário removido.");
    }

    /** Lê um inteiro, repetindo a pergunta se a pessoa digitar algo inválido. {@code null} = fim da entrada. */
    private Integer lerInteiro(String pergunta) {
        while (true) {
            saida.print(pergunta);
            if (!entrada.hasNextLine()) {
                return null;
            }
            String texto = entrada.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                saida.println("Digite um número inteiro válido.");
            }
        }
    }
}
