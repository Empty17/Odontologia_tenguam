package com.clinica.odontologica.Controller;

import com.clinica.odontologica.model.Usuario;
import com.clinica.odontologica.repository.UsuarioRepository;
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

        Optional<Usuario> usuario = usuarioRepository.findById(CPF);

        if (usuario.isPresent() && usuario.get().getSenha().equals(senha)) {
            session.setAttribute("cpfUsuario", CPF);
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
                return "perfil";
            }
        }

        return "redirect:/login";
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
}
