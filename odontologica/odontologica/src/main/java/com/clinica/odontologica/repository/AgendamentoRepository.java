package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.Agendamento;
import com.clinica.odontologica.model.Funcionario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    List<Agendamento> findByCpfUsuario(String cpfUsuario);
    List<Agendamento> findByMedico_Cpf(String cpf);
    List<Agendamento> findByDataAgendamentoAndMedico(LocalDateTime dataAgendamento, Funcionario medico);

    
}