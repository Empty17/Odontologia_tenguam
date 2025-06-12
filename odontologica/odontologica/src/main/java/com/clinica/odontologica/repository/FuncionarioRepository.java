package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.Funcionario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {
    Funcionario findByCpf(String cpf);
    Optional<Funcionario> findByEmail(String email);
}