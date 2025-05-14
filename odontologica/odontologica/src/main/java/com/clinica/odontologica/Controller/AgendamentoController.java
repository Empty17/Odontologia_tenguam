package com.clinica.odontologica.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.odontologica.model.Agendamento;
import com.clinica.odontologica.repository.AgendamentoRepository;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @PostMapping
    public Agendamento criarAgendamento(@RequestBody Agendamento agendamento) {
        return agendamentoRepository.save(agendamento);
    }

    @GetMapping("/{cpfUsuario}")
    public List<Agendamento> listarAgendamentos(@PathVariable String cpfUsuario) {
        return agendamentoRepository.findByCpfUsuario(cpfUsuario);
    }

    @PutMapping("/{id}")
    public Agendamento atualizarAgendamento(@PathVariable int id, @RequestBody Agendamento agendamento) {
        Agendamento agendamentoExistente = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        agendamentoExistente.setMedicoCpf(agendamento.getMedicoCpf());
        agendamentoExistente.setDataAgendamento(agendamento.getDataAgendamento());
        agendamentoExistente.setStatusAgendamento(agendamento.getStatusAgendamento());
        return agendamentoRepository.save(agendamentoExistente);
    }

}