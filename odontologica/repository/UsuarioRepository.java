package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Usuario findByCpfAndSenha(String cpf, String senha);
    Optional<Usuario> findByEmail(String email);

}

