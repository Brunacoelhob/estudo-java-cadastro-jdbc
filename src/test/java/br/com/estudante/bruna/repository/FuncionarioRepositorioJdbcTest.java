package br.com.estudante.bruna.repository;

import br.com.estudante.bruna.database.FonteConexao;
import br.com.estudante.bruna.model.Funcionario;
import br.com.estudante.bruna.model.RegraDeNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/** Testa o SQL de verdade contra um banco H2 em memória (modo MySQL), usando o mesmo schema.sql da aplicação. */
class FuncionarioRepositorioJdbcTest {

    private FuncionarioRepositorioJdbc repositorio;

    @BeforeEach
    void preparar() throws Exception {
        // Banco novo e isolado por teste.
        String url = "jdbc:h2:mem:" + UUID.randomUUID()
                + ";MODE=MySQL;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:db/schema.sql'";
        FonteConexao fonte = () -> DriverManager.getConnection(url);
        try (Connection c = fonte.abrir(); Statement s = c.createStatement()) {
            s.execute("INSERT INTO departamento (nome, sigla) VALUES ('Desenvolvimento', 'DEV'), ('Qualidade', 'QA')");
        }
        repositorio = new FuncionarioRepositorioJdbc(fonte);
    }

    @Test
    void insereERecuperaComIdGerado() {
        Funcionario criado = repositorio.inserir(Funcionario.novo("Maria", 100, 1));

        assertTrue(criado.id() > 0);
        assertEquals(1, repositorio.listar().size());
        assertEquals("Maria", repositorio.listar().get(0).nome());
    }

    @Test
    void removeExistenteERetornaFalsoParaInexistente() {
        Funcionario criado = repositorio.inserir(Funcionario.novo("João", 200, 2));

        assertTrue(repositorio.remover(criado.id()));
        assertFalse(repositorio.remover(criado.id()));
        assertTrue(repositorio.listar().isEmpty());
    }

    @Test
    void consultasDeExistencia() {
        repositorio.inserir(Funcionario.novo("Ana", 300, 1));

        assertTrue(repositorio.existeMatricula(300));
        assertFalse(repositorio.existeMatricula(301));
        assertTrue(repositorio.existeDepartamento(1));
        assertFalse(repositorio.existeDepartamento(99));
    }

    @Test
    void bancoRejeitaMatriculaDuplicadaMesmoSemVerificacaoPrevia() {
        repositorio.inserir(Funcionario.novo("Ana", 400, 1));

        assertThrows(RegraDeNegocioException.class, () -> repositorio.inserir(Funcionario.novo("Outra", 400, 2)));
    }

    @Test
    void bancoRejeitaDepartamentoInexistente() {
        assertThrows(RegraDeNegocioException.class, () -> repositorio.inserir(Funcionario.novo("Zé", 500, 99)));
    }

    @Test
    void sqlInjectionNoNomeEhGravadoComoTextoPuro() {
        String malicioso = "x'); DROP TABLE funcionario; --";
        repositorio.inserir(Funcionario.novo(malicioso, 600, 1));

        assertEquals(malicioso, repositorio.listar().get(0).nome());
    }

    @Test
    void falhaDeConexaoViraRepositorioException() {
        var quebrado = new FuncionarioRepositorioJdbc(() -> {
            throw new java.sql.SQLException("sem conexão");
        });

        assertThrows(RepositorioException.class, quebrado::listar);
    }
}
