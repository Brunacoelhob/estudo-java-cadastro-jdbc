package br.com.estudante.bruna.model;

/** Violação de uma regra de negócio ou dado inválido; a mensagem é segura para mostrar ao usuário. */
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
