package br.com.estudante.bruna.console;

import br.com.estudante.bruna.model.Funcionario;
import br.com.estudante.bruna.repository.FuncionarioRepositorio;
import br.com.estudante.bruna.repository.RepositorioException;
import br.com.estudante.bruna.service.FuncionarioServico;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    static class Falso implements FuncionarioRepositorio {
        final List<Funcionario> dados = new ArrayList<>();
        boolean bancoFora;

        private void checar() { if (bancoFora) throw new RepositorioException("fora", new RuntimeException("detalhe-secreto")); }
        public List<Funcionario> listar() { checar(); return dados; }
        public Funcionario inserir(Funcionario f) { checar(); Funcionario s = f.comId(dados.size() + 1); dados.add(s); return s; }
        public boolean remover(int id) { checar(); return dados.removeIf(f -> f.id() == id); }
        public boolean existeMatricula(int m) { checar(); return dados.stream().anyMatch(f -> f.matricula() == m); }
        public boolean existeDepartamento(int d) { return d >= 1 && d <= 3; }
    }

    private String rodar(Falso repo, String entrada) {
        var out = new ByteArrayOutputStream();
        var saida = new PrintStream(out, true, StandardCharsets.UTF_8);
        new Menu(new FuncionarioServico(repo), new Scanner(entrada), saida).executar();
        return out.toString(StandardCharsets.UTF_8);
    }

    @Test
    void cadastraEListaFuncionario() {
        Falso repo = new Falso();
        String saida = rodar(repo, "2\nMaria\n123\n1\n1\n4\n");

        assertTrue(saida.contains("cadastrado com id 1"));
        assertTrue(saida.contains("Maria"));
        assertEquals(1, repo.dados.size());
    }

    @Test
    void entradaNaoNumericaNaoDerrubaOPrograma() {
        // O original quebrava com InputMismatchException; agora pergunta de novo.
        String saida = rodar(new Falso(), "abc\n4\n");

        assertTrue(saida.contains("Digite um número inteiro válido."));
    }

    @Test
    void opcaoInvalidaEhInformada() {
        assertTrue(rodar(new Falso(), "9\n4\n").contains("Opção inválida."));
    }

    @Test
    void erroDeNegocioEhMostradoEMenuContinua() {
        String saida = rodar(new Falso(), "3\n99\n4\n");

        assertTrue(saida.contains("não encontrado"));
        assertTrue(saida.contains("Escolha uma opção:"));
    }

    @Test
    void falhaDeBancoNaoVazaDetalheTecnico() {
        Falso repo = new Falso();
        repo.bancoFora = true;
        String saida = rodar(repo, "1\n4\n");

        assertTrue(saida.contains("Erro ao acessar o banco de dados"));
        assertFalse(saida.contains("detalhe-secreto"));
    }

    @Test
    void fimDaEntradaEncerraSemTravar() {
        assertDoesNotThrow(() -> rodar(new Falso(), ""));
    }
}
