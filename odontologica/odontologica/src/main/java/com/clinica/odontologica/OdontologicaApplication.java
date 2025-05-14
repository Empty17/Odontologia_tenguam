package com.clinica.odontologica;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OdontologicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdontologicaApplication.class, args);

        try (Connection conexao = Conexao.conectar()) {
            System.out.println("Conectado com sucesso ao banco!");
            criarTabelas(); // chama a criação das tabelas após conexão bem-sucedida
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void criarTabelas() {
        try (Connection conn = Conexao.conectar();
                Statement stmt = conn.createStatement()) {

            // Tabela Funcionarios
            String sqlFuncionarios = """
                        CREATE TABLE IF NOT EXISTS Funcionarios (
                            CPF CHAR(14) PRIMARY KEY NOT NULL,
                            Nome VARCHAR(100) NOT NULL,
                            Email VARCHAR(100) NOT NULL,
                            Senha VARCHAR(100) NOT NULL,
                            Endereco VARCHAR(255) NOT NULL,
                            Data_Nascimento DATE NOT NULL,
                            Telefone VARCHAR(20) NOT NULL,
                            data_admissao DATE NOT NULL,
                            cargo VARCHAR(50) NOT NULL,
                            especialidade VARCHAR(30)
                        );
                    """;

            // Tabela Usuario
            String sqlUsuarios = """
                        CREATE TABLE IF NOT EXISTS Usuario (
                            CPF VARCHAR(14) PRIMARY KEY NOT NULL,
                            Nome VARCHAR(100) NOT NULL,
                            Email VARCHAR(100) NOT NULL,
                            Senha VARCHAR(100) NOT NULL,
                            Endereco VARCHAR(255) NOT NULL,
                            Data_Nascimento DATE NOT NULL,
                            Telefone VARCHAR(20) NOT NULL
                        );
                    """;

            // Tabela Agendamento
            String sqlAgendamento = """
                    REATE TABLE Agendamento (
                        id_agendamento INT AUTO_INCREMENT PRIMARY KEY,
                        medico_cpf CHAR(11),
                        data_agendamento DATETIME,
                        cpf_usuario CHAR(11),
                        status_agendamento VARCHAR(20),
                        FOREIGN KEY (medico_cpf) REFERENCES Funcionarios(CPF),
                        FOREIGN KEY (cpf_usuario) REFERENCES Usuario(CPF)
                    );

                                """;

            // Tabela Consulta
            String sqlConsulta = """
                        CREATE TABLE IF NOT EXISTS Consulta (
                            ID_Consulta INT AUTO_INCREMENT PRIMARY KEY,
                            ID_Agendamento INT,
                            data_consulta DATETIME,
                            Diagnostico TEXT,
                            Historico TEXT,
                            Tratamento TEXT,
                            FOREIGN KEY (ID_Agendamento) REFERENCES Agendamento(ID_Agendamento)
                        );
                    """;

            // Executa as criações
            stmt.execute(sqlFuncionarios);
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlAgendamento);
            stmt.execute(sqlConsulta);

            System.out.println("Tabelas criadas com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
