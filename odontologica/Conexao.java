package com.clinica.odontologica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
     private static final String URL = "jdbc:mysql://localhost:3306/clinica";
    private static final String USUARIO = "teste";
    private static final String SENHA = "1234";
 
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    
}
