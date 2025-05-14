package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    List<Agendamento> findByCpfUsuario(String cpfUsuario);
}