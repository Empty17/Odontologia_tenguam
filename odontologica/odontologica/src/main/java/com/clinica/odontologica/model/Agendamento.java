package com.clinica.odontologica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento") // Mapear para a coluna correta no banco
    private int idAgendamento;

    @Column(name = "medico") // Mapear para a coluna correta no banco
    private String medico;

    @Column(name = "data_agendamento") // Mapear para a coluna correta no banco
    private LocalDateTime dataAgendamento;

    @Column(name = "cpf_usuario") // Mapear para a coluna correta no banco
    private String cpfUsuario;

    @Column(name = "status_agendamento") // Mapear para a coluna correta no banco
    private String statusAgendamento;

    // Getters e Setters
    public int getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(int idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public String getMedicoCpf() {
        return medico;
    }

    public void setMedicoCpf(String medicoCpf) {
        this.medico = medicoCpf;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDateTime dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public String getStatusAgendamento() {
        return statusAgendamento;
    }

    public void setStatusAgendamento(String statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }
}
