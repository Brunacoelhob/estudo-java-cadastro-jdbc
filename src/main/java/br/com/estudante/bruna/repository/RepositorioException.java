package br.com.estudante.bruna.repository;

/** Falha técnica ao acessar o banco (conexão, SQL). Não é mostrada em detalhe ao usuário final. */
public class RepositorioException extends RuntimeException {

    public RepositorioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
