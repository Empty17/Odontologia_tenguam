package com.clinica.odontologica.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Agendamento {
       private int idAgendamento;
    private String medicoCpf; // Referência à tabela Funcionarios
    private LocalDate dia;
    private LocalTime horario;
    private String cpfUsuario; // Referência à tabela Usuario
    private String statusAgendamento;
    private String observacao;


}
