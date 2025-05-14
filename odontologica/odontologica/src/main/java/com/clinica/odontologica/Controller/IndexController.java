package com.clinica.odontologica.Controller;

import com.clinica.odontologica.model.Funcionario;
import com.clinica.odontologica.model.Agendamento;
import com.clinica.odontologica.model.Usuario;
import com.clinica.odontologica.repository.FuncionarioRepository;
import com.clinica.odontologica.repository.UsuarioRepository;
import com.clinica.odontologica.repository.AgendamentoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    // =======================
    // PÁGINAS ESTÁTICAS
    // =======================
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastroUsuario")
    public String cadastroUsuario() {
        return "cadastroUsuario";
    }

    @GetMapping("/cadastroFuncionario")
    public String cadastroFuncionario() {
        return "cadastroFuncionario";
    }

    @GetMapping("/esqueciSenha")
    public String esqueciSenha() {
        return "esqueciSenha";
    }

    @GetMapping("/mudarSenha")
    public String mudarSenha(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "mudarSenha";
    }

    // =======================
    // AUTENTICAÇÃO
    // =======================
    @PostMapping("/autenticar")
    public String autenticar(@RequestParam String CPF,
                             @RequestParam String senha,
                             Model model,
                             HttpSession session) {

        // Remove a formatação do CPF
        String cpfLimpo = CPF.replaceAll("[^0-9X]", "");

        // Verificação para administrador
        if (cpfLimpo.equals("99999999999") && senha.equals("adm")) {
            session.setAttribute("cpfUsuario", cpfLimpo);
            session.setAttribute("perfilUsuario", "ADMIN");
            return "redirect:/paginaAdm";
        }

        // Busca o usuário no banco
        Optional<Usuario> usuario = usuarioRepository.findById(cpfLimpo);

        if (usuario.isPresent() && usuario.get().getSenha().equals(senha)) {
            session.setAttribute("cpfUsuario", cpfLimpo);
            session.setAttribute("perfilUsuario", "COMUM");
            return "redirect:/perfil";
        }

        model.addAttribute("erro", "CPF ou senha inválidos.");
        return "login";
    }

    // =======================
    // CADASTRO
    // =======================
    @PostMapping("/cadastrar")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario) {
        usuarioRepository.save(usuario);
        return "redirect:/login";
    }

    // =======================
    // PERFIL DO USUÁRIO
    // =======================
    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        String cpf = (String) session.getAttribute("cpfUsuario");

        if (cpf != null) {
            Optional<Usuario> usuario = usuarioRepository.findById(cpf);
            if (usuario.isPresent()) {
                model.addAttribute("usuario", usuario.get());
                // Buscar agendamentos do usuário
                model.addAttribute("agendamentos", agendamentoRepository.findByCpfUsuario(cpf));
                return "perfil";
            }
        }

        return "redirect:/login";
    }

    // =======================
    // PÁGINA DO ADMINISTRADOR
    // =======================
    @GetMapping("/paginaAdm")
    public String paginaAdministrador(HttpSession session, Model model) {
        String perfil = (String) session.getAttribute("perfilUsuario");

        if ("ADMIN".equals(perfil)) {
            model.addAttribute("funcionarios", funcionarioRepository.findAll());
            return "paginaAdm";
        }

        model.addAttribute("erro", "Acesso negado.");
        return "redirect:/login";
    }

    // =======================
    // CADASTRAR FUNCIONÁRIO
    // =======================
    @GetMapping("/cadastroFuncionarioAdm")
    public String cadastroFuncionarioAdm() {
        return "cadastroFuncionarioAdm";
    }

    @PostMapping("/funcionario/cadastrar")
    public String cadastrarFuncionario(@ModelAttribute Funcionario funcionario, Model model) {
        // Verificar se o CPF já está cadastrado
        Optional<Funcionario> funcionarioExistente = funcionarioRepository.findById(funcionario.getCpf());

        if (funcionarioExistente.isPresent()) {
            model.addAttribute("erro", "Funcionário com este CPF já cadastrado.");
            return "cadastroFuncionarioAdm";
        }

        // Caso contrário, salvar o novo funcionário
        funcionarioRepository.save(funcionario);
        model.addAttribute("sucesso", "Funcionário cadastrado com sucesso!");
        return "redirect:/paginaAdm"; // Redirecionar para a página inicial após o cadastro
    }

    // =======================
    // EXCLUIR FUNCIONÁRIO
    // =======================
    @PostMapping("/funcionario/excluir")
    public String excluirFuncionario(@RequestParam String cpf, Model model) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(cpf);

        if (funcionario.isPresent()) {
            funcionarioRepository.delete(funcionario.get());
            model.addAttribute("sucesso", "Funcionário excluído com sucesso.");
        } else {
            model.addAttribute("erro", "Funcionário não encontrado.");
        }

        return "redirect:/paginaAdm";
    }

    // =======================
    // RECUPERAÇÃO DE SENHA
    // =======================
    @PostMapping("/enviarRecuperacaoSenha")
    public String enviarRecuperacaoSenha(@RequestParam String email, Model model) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isPresent()) {
            return "redirect:/mudarSenha?email=" + email;
        }

        model.addAttribute("erro", "E-mail não encontrado.");
        return "esqueciSenha";
    }

    @PostMapping("/salvarNovaSenha")
    public String salvarNovaSenha(@RequestParam String email,
                                  @RequestParam String novaSenha,
                                  Model model) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isPresent()) {
            usuario.get().setSenha(novaSenha);
            usuarioRepository.save(usuario.get());
            return "login";
        }

        model.addAttribute("erro", "Erro ao tentar mudar a senha.");
        return "login";
    }

    // =======================
    // TELA DE AGENDAMENTO
    // =======================
    @GetMapping("/agendamento")
    public String telaAgendamento(HttpSession session, Model model) {
        String cpf = (String) session.getAttribute("cpfUsuario");

        if (cpf != null) {
            Optional<Usuario> usuario = usuarioRepository.findById(cpf);
            if (usuario.isPresent()) {
                model.addAttribute("usuario", usuario.get());
                model.addAttribute("agendamento", new Agendamento());
                return "agendamento";
            }
        }

        return "redirect:/login";
    }

    // =======================
    // SALVAR AGENDAMENTO
    // =======================
    @PostMapping("/agendamento/salvar")
    public String salvarAgendamento(@ModelAttribute Agendamento agendamento,
                                    HttpSession session,
                                    Model model) {

        String cpfUsuario = (String) session.getAttribute("cpfUsuario");

        if (cpfUsuario == null) {
            return "redirect:/login";
        }

        agendamento.setCpfUsuario(cpfUsuario);
        agendamento.setStatusAgendamento("Pendente");
        agendamentoRepository.save(agendamento);

        model.addAttribute("sucesso", "Agendamento realizado com sucesso!");
        return "redirect:/perfil";
    }


    

    // =======================
    // LOGOUT
    // =======================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}