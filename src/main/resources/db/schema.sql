-- Esquema do banco (compatível com MySQL 8 e H2 em modo MySQL, usado nos testes).
-- Idempotente: pode ser executado várias vezes sem apagar dados.

CREATE TABLE IF NOT EXISTS departamento (
  idDepartamento INT NOT NULL AUTO_INCREMENT,
  nome  VARCHAR(45) NOT NULL,
  sigla VARCHAR(45) NOT NULL,
  PRIMARY KEY (idDepartamento),
  CONSTRAINT UQ_Departamento_Sigla UNIQUE (sigla)
);

CREATE TABLE IF NOT EXISTS funcionario (
  idFuncionario   INT NOT NULL AUTO_INCREMENT,
  nome            VARCHAR(45) NOT NULL,
  matricula       INT NOT NULL,
  departamento_FK INT NOT NULL,
  PRIMARY KEY (idFuncionario),
  -- A matrícula é única: a regra vale no banco, não só no código (protege contra concorrência).
  CONSTRAINT UQ_Funcionario_Matricula UNIQUE (matricula),
  CONSTRAINT FK_Departamento FOREIGN KEY (departamento_FK) REFERENCES departamento(idDepartamento)
);
