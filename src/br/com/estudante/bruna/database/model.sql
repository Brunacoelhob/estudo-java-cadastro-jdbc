DROP DATABASE IF EXISTS db_java;
CREATE DATABASE db_java;
USE db_java;

CREATE TABLE departamento (
  idDepartamento INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(45) NOT NULL,
  sigla VARCHAR(45) NOT NULL,
  PRIMARY KEY (idDepartamento)
);

CREATE TABLE funcionario (
  idFuncionario INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(45) NOT NULL,
  matricula INT NOT NULL,
  departamento_FK INT,
  PRIMARY KEY (idFuncionario),
  CONSTRAINT FK_Departamento FOREIGN KEY (departamento_FK) REFERENCES departamento(idDepartamento)
);

INSERT INTO departamento (nome, sigla) VALUES
('Desenvolvimento', 'DEV'),
('Qualidade', 'QA'),
('Engenharia', 'ENG');

INSERT INTO funcionario (nome, matricula, departamento_FK) VALUES
('Maria', 2513, 3),
('João', 3640, 1),
('Martha', 1010, 2);

SELECT * FROM funcionario;
