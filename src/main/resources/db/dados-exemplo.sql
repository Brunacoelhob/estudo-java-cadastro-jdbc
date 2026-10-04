-- Dados de exemplo (opcional). Carregado automaticamente pelo docker compose.
-- SET NAMES: o cliente do entrypoint do MySQL lê o arquivo como latin1 sem isto ("João" viraria "JoÃ£o").
SET NAMES utf8mb4;

INSERT INTO departamento (nome, sigla) VALUES
  ('Desenvolvimento', 'DEV'),
  ('Qualidade', 'QA'),
  ('Engenharia', 'ENG');

INSERT INTO funcionario (nome, matricula, departamento_FK) VALUES
  ('Maria', 2513, 3),
  ('João', 3640, 1),
  ('Martha', 1010, 2);
