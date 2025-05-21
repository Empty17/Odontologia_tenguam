drop database clinica;
CREATE DATABASE clinica;
USE clinica;

CREATE TABLE Usuario (
    CPF Varchar(11) PRIMARY KEY not null,
    Nome VARCHAR(100)not null,
    Email VARCHAR(100)not null,
    Senha VARCHAR(100)not null,
    Endereco VARCHAR(255)not null,
    Data_Nascimento DATE not null,
    Telefone VARCHAR(20) not null
);
ALTER TABLE Usuario ADD foto_perfil LONGBLOB;

CREATE TABLE Funcionarios (
    CPF CHAR(11) PRIMARY KEY not null,
    Nome VARCHAR(100) not null,
    Email VARCHAR(100)not null,
    Senha VARCHAR(100)not null,
    Endereco VARCHAR(255)not null,
    Data_Nascimento DATE not null,
    Telefone VARCHAR(20) not null,
    data_admissao DATE not null,
    cargo varchar(50) not null,
    especialidade VARCHAR(30) null
);
ALTER TABLE funcionarios MODIFY foto LONGBLOB;

CREATE TABLE Agendamento (
    ID_Agendamento INT AUTO_INCREMENT PRIMARY KEY,
    Medico CHAR(30),
    CPF_Usuario CHAR(11),
    data_agendamento DATETIME,
    status_agendamento VARCHAR(20),
    Diagnostico TEXT,
    Historico TEXT,
    Tratamento TEXT,
    FOREIGN KEY (Medico) REFERENCES Funcionarios(CPF),
    FOREIGN KEY (CPF_Usuario) REFERENCES Usuario(CPF)    
);
ALTER TABLE agendamento ADD COLUMN problema_odontologico VARCHAR(255);


SELECT * FROM Usuario;
SELECT * FROM Funcionarios;
SELECT * FROM Agendamento;


