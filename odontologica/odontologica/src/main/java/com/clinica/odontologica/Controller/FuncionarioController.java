package com.clinica.odontologica.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // Importante: usar @Controller
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.clinica.odontologica.model.Agendamento;
import com.clinica.odontologica.model.Funcionario;
import com.clinica.odontologica.repository.AgendamentoRepository;
import com.clinica.odontologica.repository.FuncionarioRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @GetMapping("/{cpf}")
    public String exibirPerfilFuncionario(@PathVariable String cpf, Model model) {
        Funcionario funcionario = funcionarioRepository.findById(cpf).orElse(null);
        if (funcionario == null) {
            model.addAttribute("erro", "Funcionário não encontrado.");
            return "erro";
        }

        List<Agendamento> agendamentos = agendamentoRepository.findByMedico_Cpf(cpf);
        model.addAttribute("funcionario", funcionario);
        model.addAttribute("agendamentos", agendamentos);
        return "perfilFuncionario";
    }

    @PostMapping("/foto/upload")
    public String uploadFoto(@RequestParam("cpf") String cpf,
                             @RequestParam("foto") MultipartFile file) {
        Funcionario funcionario = funcionarioRepository.findByCpf(cpf);
        if (funcionario != null && !file.isEmpty()) {
            try {
                funcionario.setFoto(file.getBytes());
                funcionarioRepository.save(funcionario);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/paginaFuncionario";
    }

    @PostMapping("/foto/remover")
    public String removerFoto(@RequestParam("cpf") String cpf) {
        Funcionario funcionario = funcionarioRepository.findByCpf(cpf);
        if (funcionario != null) {
            funcionario.setFoto(null);
            funcionarioRepository.save(funcionario);
        }
        return "redirect:/paginaFuncionario";
    }

    

    @GetMapping("/foto/{cpf}")
    public void exibirFoto(@PathVariable String cpf, HttpServletResponse response) {
        Funcionario funcionario = funcionarioRepository.findByCpf(cpf);
        if (funcionario != null && funcionario.getFoto() != null) {
            try {
                response.setContentType("image/jpeg");
                response.getOutputStream().write(funcionario.getFoto());
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}