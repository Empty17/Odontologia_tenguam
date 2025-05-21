package com.clinica.odontologica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Agendamento")
    private int idAgendamento;

    @ManyToOne
    @JoinColumn(name = "Medico", referencedColumnName = "CPF")
    private Funcionario medico;

    @Column(name = "CPF_Usuario", nullable = false)
    private String cpfUsuario;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDateTime dataAgendamento;

    @Column(name = "status_agendamento")
    private String statusAgendamento;
    
    @Lob
private String diagnostico;

@Lob
private String historico;

@Lob
private String tratamento;

    @Column(name = "problema_odontologico")
    private String problemaOdontologico;

    @ManyToOne
@JoinColumn(name = "CPF_Usuario", referencedColumnName = "CPF", insertable = false, updatable = false)
private Usuario usuario;

    // Getters e Setters
    public int getIdAgendamento() {
        return idAgendamento;
    }

    public Usuario getUsuario() {
    return usuario;
}

    public void setIdAgendamento(int idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public Funcionario getMedico() {
        return medico;
    }

    public void setMedico(Funcionario medico) {
        this.medico = medico;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDateTime dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public String getStatusAgendamento() {
        return statusAgendamento;
    }

    public void setStatusAgendamento(String statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }

    public String getProblemaOdontologico() {
        return problemaOdontologico;
    }

    public void setProblemaOdontologico(String problemaOdontologico) {
        this.problemaOdontologico = problemaOdontologico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    // Getter e Setter para 'historico'
    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    // Getter e Setter para 'tratamento'
    public String getTratamento() {
        return tratamento;
    }

    public void setTratamento(String tratamento) {
        this.tratamento = tratamento;
    }
}