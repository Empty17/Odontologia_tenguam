package com.clinica.odontologica.model;

import java.time.LocalDateTime;

public class Consulta {
    private int idConsulta;
    private int idAgendamento; // Referência à tabela Agendamento
    private LocalDateTime dataConsulta;
    private String diagnostico;
    private String historico;
    private String tratamento;

    
}
