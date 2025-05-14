package com.clinica.odontologica.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.odontologica.repository.FuncionarioRepository;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @GetMapping
    public List<MedicoDTO> listarMedicos() {
        return funcionarioRepository.findAll().stream()
            .filter(func -> "MEDICO".equalsIgnoreCase(func.getCargo()))
            .map(func -> new MedicoDTO(func.getCpf(), func.getNome()))
            .collect(Collectors.toList());
    }

    // DTO para enviar apenas o necessário ao front-end
    public record MedicoDTO(String cpf, String nome) {}
}
