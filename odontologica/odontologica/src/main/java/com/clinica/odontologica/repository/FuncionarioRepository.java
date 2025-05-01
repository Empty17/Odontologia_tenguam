package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {
    // O JpaRepository já oferece os métodos básicos de CRUD
}
