# Sistema de Cadastro DB Java

Este projeto é um sistema simples de cadastro de funcionários utilizando **Java**, **JDBC** e **MySQL**.  
Ele permite **listar**, **cadastrar** e **deletar** funcionários no banco de dados, tudo via console.

---

## 🚀 Funcionalidades

✅ Listar funcionários  
✅ Cadastrar novo funcionário  
✅ Deletar funcionário por ID  

---

## 🛠 Tecnologias usadas

- Java 21 (ou compatível)
- JDBC (com MySQL Connector)
- MySQL
- IntelliJ IDEA (ou outra IDE)

---

## 💾 Configuração do Banco de Dados

Execute o script `model.sql` no seu MySQL:

```sql
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
