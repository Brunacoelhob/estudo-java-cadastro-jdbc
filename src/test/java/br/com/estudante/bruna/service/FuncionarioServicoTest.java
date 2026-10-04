package br.com.estudante.bruna.service;

import br.com.estudante.bruna.model.Funcionario;
import br.com.estudante.bruna.model.RegraDeNegocioException;
import br.com.estudante.bruna.repository.FuncionarioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioServicoTest {

    /** Repositório falso em memória: o serviço é testado sem banco. */
    static class RepositorioEmMemoria implements FuncionarioRepositorio {
        final List<Funcionario> dados = new ArrayList<>();
        int proximoId = 1;

        public List<Funcionario> listar() { return List.copyOf(dados); }
        public Funcionario inserir(Funcionario f) {
            Funcionario salvo = f.comId(proximoId++);
            dados.add(salvo);
            return salvo;
        }
        public boolean remover(int id) { return dados.removeIf(f -> f.id() == id); }
        public boolean existeMatricula(int m) { return dados.stream().anyMatch(f -> f.matricula() == m); }
        public boolean existeDepartamento(int d) { return d >= 1 && d <= 3; }
    }

    private RepositorioEmMemoria repositorio;
    private FuncionarioServico servico;

    @BeforeEach
    void preparar() {
        repositorio = new RepositorioEmMemoria();
        servico = new FuncionarioServico(repositorio);
    }

    @Test
    void cadastraComDadosValidosAparandoEspacos() {
        Funcionario f = servico.cadastrar("  Maria  ", 10, 1);

        assertEquals("Maria", f.nome());
        assertEquals(1, f.id());
    }

    @Test
    void rejeitaNomeVazioOuNulo() {
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("   ", 10, 1));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar(null, 10, 1));
    }

    @Test
    void rejeitaNomeMuitoLongo() {
        String longo = "a".repeat(FuncionarioServico.TAMANHO_MAXIMO_NOME + 1);
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar(longo, 10, 1));
    }

    @Test
    void rejeitaMatriculaNaoPositiva() {
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("Ana", 0, 1));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("Ana", -5, 1));
    }

    @Test
    void rejeitaDepartamentoInexistente() {
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("Ana", 10, 9));
    }

    @Test
    void rejeitaMatriculaDuplicada() {
        servico.cadastrar("Ana", 10, 1);

        var erro = assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("Bia", 10, 2));
        assertTrue(erro.getMessage().contains("10"));
        assertEquals(1, repositorio.dados.size());
    }

    @Test
    void removeExistenteELancaErroParaInexistente() {
        Funcionario f = servico.cadastrar("Ana", 10, 1);

        servico.remover(f.id());

        assertTrue(servico.listar().isEmpty());
        assertThrows(RegraDeNegocioException.class, () -> servico.remover(f.id()));
    }
}
