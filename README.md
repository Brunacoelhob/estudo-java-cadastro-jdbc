# Cadastro de Funcionários (Java + JDBC + MySQL)

[![CI](https://github.com/Brunacoelhob/estudo-java-cadastro-jdbc/actions/workflows/ci.yml/badge.svg)](https://github.com/Brunacoelhob/estudo-java-cadastro-jdbc/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/java-21-orange)
![License](https://img.shields.io/badge/license-MIT-blue)

Aplicação de console para **listar, cadastrar e remover funcionários** em um banco MySQL, usando JDBC puro. Projeto de estudo organizado em camadas, com testes automatizados e execução via Docker.

## Arquitetura

```
console/Menu  ──►  service/FuncionarioServico  ──►  repository/FuncionarioRepositorio (interface)
 (entrada/saída)     (regras de negócio)                 └─ FuncionarioRepositorioJdbc (SQL)
```

- **Menu**: só conversa com o usuário; entrada/saída injetadas (por isso é testável).
- **Serviço**: valida nome, matrícula positiva e única, departamento existente.
- **Repositório**: SQL 100% parametrizado (`PreparedStatement`), conexões fechadas com *try-with-resources*.
- **Banco**: `UNIQUE` na matrícula e chave estrangeira garantem as regras mesmo sob concorrência.

## O que foi melhorado em relação à primeira versão

| Antes | Agora |
|---|---|
| Senha do banco escrita no código | Configuração por variáveis de ambiente (`DB_PASSWORD`) |
| Conexões nunca fechadas | try-with-resources em todo acesso |
| `InputMismatchException` derrubava o programa | Entrada inválida pergunta de novo |
| Erros com `printStackTrace` | Mensagens amigáveis, sem vazar detalhe técnico |
| Sem validação | Nome, matrícula e departamento validados + restrições no banco |
| JAR do driver versionado e `.idea` | Maven gerencia dependências |
| Sem testes | 24 testes (unidade, banco H2 em modo MySQL, console) |

## Como executar

### Com Docker (recomendado)

```bash
cp .env.example .env            # defina DB_PASSWORD no arquivo .env
docker compose up -d db         # MySQL com schema e dados de exemplo
docker compose run --rm app     # abre o menu
```

### Sem Docker

Requer Java 21, Maven e um MySQL com o schema aplicado (`src/main/resources/db/schema.sql`).

```bash
export DB_PASSWORD=sua_senha     # opcional: DB_USER (padrão root) e DB_URL
mvn package
java -jar target/cadastro-funcionarios.jar
```

## Testes

```bash
mvn verify
```

## Licença

[MIT](LICENSE)
