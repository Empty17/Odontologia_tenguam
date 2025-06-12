package com.clinica.odontologica.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.odontologica.model.Agendamento;
import com.clinica.odontologica.model.Funcionario;
import com.clinica.odontologica.repository.AgendamentoRepository;
import com.clinica.odontologica.repository.FuncionarioRepository;

@RestController
@RequestMapping("/agendamentos")
@CrossOrigin("*") // Permitir requisições do frontend
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    // Listar todos os médicos
    @GetMapping("/medicos")
    public List<Funcionario> listarMedicos() {
        return funcionarioRepository.findAll();
    }

    // Criar um agendamento
    @PostMapping
    public ResponseEntity<?> criarAgendamento(@RequestBody Agendamento agendamento) {
        // Verifica se o médico existe
        Funcionario medico = funcionarioRepository.findById(agendamento.getMedico().getCpf())
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));
        // Verifica se já existe um agendamento no mesmo horário
        List<Agendamento> agendamentosExistentes = agendamentoRepository
                .findByDataAgendamentoAndMedico(agendamento.getDataAgendamento(), medico);
        if (!agendamentosExistentes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe um agendamento para este horário.");
        }
        agendamento.setMedico(medico);
        Agendamento novoAgendamento = agendamentoRepository.save(agendamento);
        return ResponseEntity.ok(novoAgendamento);
    }
    // Listar agendamentos de um usuário específico
    @GetMapping("/{cpfUsuario}")
    public List<Agendamento> listarAgendamentos(@PathVariable String cpfUsuario) {
        return agendamentoRepository.findByCpfUsuario(cpfUsuario);
    }

    // Atualizar informações de um agendamento
    @PutMapping("/{id}")
    public Agendamento atualizarAgendamento(@PathVariable int id, @RequestBody Agendamento agendamento) {
        Agendamento existente = agendamentoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        Funcionario medico = funcionarioRepository.findById(agendamento.getMedico().getCpf())
            .orElseThrow(() -> new RuntimeException("Médico não encontrado"));

        existente.setMedico(medico);
        existente.setDataAgendamento(agendamento.getDataAgendamento());
        existente.setStatusAgendamento(agendamento.getStatusAgendamento());
        existente.setProblemaOdontologico(agendamento.getProblemaOdontologico());

        return agendamentoRepository.save(existente);
    }


    // Rota para buscar os detalhes de um agendamento
@GetMapping("/agendamentos/{id}")
    public String getAgendamento(@PathVariable int id, Model model) {
        Agendamento agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Agendamento inválido"));
        model.addAttribute("agendamento", agendamento);
        return "agendamentoDetalhes"; // Nome do seu template
    }

    // Cancelar um agendamento
    @PostMapping("/salvarConsulta")
public String salvarConsulta(@RequestParam("idAgendamento") int id,
                             @RequestParam("diagnostico") String diagnostico,
                             @RequestParam("historico") String historico,
                             @RequestParam("tratamento") String tratamento) {
    Optional<Agendamento> optionalAgendamento = agendamentoRepository.findById(id);
    if (optionalAgendamento.isPresent()) {
        Agendamento agendamento = optionalAgendamento.get();
        agendamento.setDiagnostico(diagnostico);
        agendamento.setHistorico(historico);
        agendamento.setTratamento(tratamento);
        
        // Se o status não for "Concluído", então altere para "Concluído"
        if (!"Concluido".equals(agendamento.getStatusAgendamento())) {
            agendamento.setStatusAgendamento("Concluido");
        }
        
        agendamentoRepository.save(agendamento); // Salva o agendamento com os novos dados
    }
    // Redireciona para a página "paginaFuncionario"
    return "redirect:/paginaFuncionario";
}
}