package com.clinica.odontologica.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // Importação da classe Model
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clinica.odontologica.model.Usuario;
import com.clinica.odontologica.repository.UsuarioRepository;

import java.util.Optional;  // Importação do Optional

@Controller
public class IndexController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/index")
    public String index() {
        return "index"; // retorna index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // retorna login.html
    }

    @GetMapping("/cadastroUsuario")
    public String cadastroUsuario() {
        return "cadastroUsuario"; // retorna cadastroUsuario.html
    }

    @GetMapping("/cadastroFuncionario")
    public String cadastroFuncionario() {
        return "cadastroFuncionario"; // retorna cadastroFuncionario.html
    }

    // Método de cadastro de usuário
    @PostMapping("/cadastrar")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario) {
        usuarioRepository.save(usuario);
        return "redirect:/login"; // redireciona para tela de login após cadastro
    }

    // Método de autenticação
    @PostMapping("/autenticar")
    public String autenticar(@RequestParam String CPF,
                             @RequestParam String senha,
                             Model model) {
        Optional<Usuario> usuario = usuarioRepository.findById(CPF);

        if (usuario.isPresent() && usuario.get().getSenha().equals(senha)) {
            return "redirect:/perfil"; // Redireciona para a página perfil
        } else {
            model.addAttribute("erro", "CPF ou senha inválidos."); // Adiciona erro ao model
            return "login"; // Retorna à página de login com a mensagem de erro
        }
    }

    // Método de perfil
    @GetMapping("/perfil")
    public String perfil() {
        return "perfil"; // retorna perfil.html
    }

    // Página para solicitar recuperação de senha
    @GetMapping("/esqueciSenha")
    public String esqueciSenha() {
        return "esqueciSenha"; // Página para o usuário inserir o e-mail
    }

    // Envia link para recuperação de senha
    @PostMapping("/enviarRecuperacaoSenha")
    public String enviarRecuperacaoSenha(@RequestParam String email, Model model) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        
        if (usuario.isPresent()) {
            // Se o e-mail for válido, redireciona para a tela de mudança de senha
            return "redirect:/mudarSenha?email=" + email;
        } else {
            // Caso o e-mail não seja encontrado
            model.addAttribute("erro", "E-mail não encontrado.");
            return "esqueciSenha"; // Retorna para a página de "Esqueci a Senha"
        }
    }

    // Página para o usuário mudar a senha
    @GetMapping("/mudarSenha")
    public String mudarSenha(@RequestParam String email, Model model) {
        model.addAttribute("email", email); // Passa o e-mail para a tela de mudar senha
        return "mudarSenha"; // Página para mudar senha
    }

    // Salva a nova senha no banco de dados
    @PostMapping("/salvarNovaSenha")
    public String salvarNovaSenha(@RequestParam String email, @RequestParam String novaSenha, Model model) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        
        if (usuario.isPresent()) {
            // Altera a senha do usuário
            usuario.get().setSenha(novaSenha);
            usuarioRepository.save(usuario.get());
            return "login"; // Redireciona para a tela de login
        } else {
            model.addAttribute("erro", "Erro ao tentar mudar a senha.");
            return "login"; // Caso ocorra algum erro, retorna à tela de login
        }
    }
}
