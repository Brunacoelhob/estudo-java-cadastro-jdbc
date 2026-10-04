package br.com.estudante.bruna;

import br.com.estudante.bruna.console.Menu;
import br.com.estudante.bruna.database.ConfiguracaoBanco;
import br.com.estudante.bruna.database.FonteConexao;
import br.com.estudante.bruna.repository.FuncionarioRepositorioJdbc;
import br.com.estudante.bruna.service.FuncionarioServico;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        ConfiguracaoBanco config;
        try {
            config = ConfiguracaoBanco.deAmbiente(System.getenv());
        } catch (IllegalStateException e) {
            System.err.println("Erro de configuração: " + e.getMessage());
            System.exit(1);
            return;
        }

        var repositorio = new FuncionarioRepositorioJdbc(FonteConexao.de(config));
        var servico = new FuncionarioServico(repositorio);
        new Menu(servico, new Scanner(System.in), System.out).executar();
    }
}
