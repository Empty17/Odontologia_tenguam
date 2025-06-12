package com.clinica.odontologica.Controller;

import com.clinica.odontologica.model.Funcionario;
import com.clinica.odontologica.model.Agendamento;
import com.clinica.odontologica.model.Usuario;
import com.clinica.odontologica.repository.FuncionarioRepository;
import com.clinica.odontologica.repository.UsuarioRepository;
import com.clinica.odontologica.repository.AgendamentoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public String index(HttpSession session, Model model) {
    String cpf = (String) session.getAttribute("cpfUsuario");

    if (cpf != null) {
        Optional<Usuario> usuario = usuarioRepository.findById(cpf);
        if (usuario.isPresent()) {
            model.addAttribute("nomeUsuario", usuario.get().getNome());
            model.addAttribute("perfilUsuario", "COMUM");
        } else {
            Optional<Funcionario> funcionario = funcionarioRepository.findById(cpf);
            if (funcionario.isPresent()) {
                model.addAttribute("nomeUsuario", funcionario.get().getNome());
                model.addAttribute("perfilUsuario", "FUNCIONARIO");
            }
        }
    } else {
        model.addAttribute("nomeUsuario", null);
    }

    return "index";
}

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/sobrenos")
public String sobrenos(HttpSession session, Model model) {
    String cpf = (String) session.getAttribute("cpfUsuario");

    if (cpf != null) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(cpf);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("usuario", usuario);

            model.addAttribute("nomeUsuario", usuario.getNome());
            model.addAttribute("perfilUsuario", "COMUM");
        }
    }

    return "sobrenos"; // Retorna a página de contato
}

    @GetMapping("/servicos")
public String servicos(HttpSession session, Model model) {
    String cpf = (String) session.getAttribute("cpfUsuario");

    if (cpf != null) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(cpf);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("usuario", usuario);

            model.addAttribute("nomeUsuario", usuario.getNome());
            model.addAttribute("perfilUsuario", "COMUM");
        }
    }

    return "servicos"; // Retorna a página de contato
}

     @GetMapping("/contato")
public String contato(HttpSession session, Model model) {
    String cpf = (String) session.getAttribute("cpfUsuario");

    if (cpf != null) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(cpf);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("usuario", usuario);

            model.addAttribute("nomeUsuario", usuario.getNome());
            model.addAttribute("perfilUsuario", "COMUM");
        }
    }

    return "contato"; // Retorna a página de contato
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

    // Verificar se é um Funcionário
    Optional<Funcionario> funcionario = funcionarioRepository.findById(cpfLimpo);
    if (funcionario.isPresent() && funcionario.get().getSenha().equals(senha)) {
        session.setAttribute("cpfUsuario", cpfLimpo);
        session.setAttribute("perfilUsuario", "FUNCIONARIO");
        return "redirect:/paginaFuncionario";
    }

    // Verificar se é um Usuário
    Optional<Usuario> usuario = usuarioRepository.findById(cpfLimpo);
    if (usuario.isPresent() && usuario.get().getSenha().equals(senha)) {
        session.setAttribute("cpfUsuario", cpfLimpo);
        session.setAttribute("perfilUsuario", "COMUM");
        return "redirect:/perfil";
    }

    // Caso não encontre o CPF ou senha, retornar erro
    model.addAttribute("erro", "CPF ou senha inválidos.");
    return "login";
}

    // =======================
    // CADASTRO
    // =======================
    @PostMapping("/cadastrar")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario, Model model) {
        // Verifica se já existe um usuário com esse CPF
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getCpf());

        // Verifica se já existe um funcionário com esse CPF
        Optional<Funcionario> funcionarioExistente = funcionarioRepository.findById(usuario.getCpf());

        // Se já existe como usuário ou funcionário, exibe erro
        if (usuarioExistente.isPresent() || funcionarioExistente.isPresent()) {
            model.addAttribute("erro", "Já existe um cadastro com este CPF.");
            return "cadastroUsuario"; // Retorna para a tela de cadastro
        }

        // Caso contrário, salva o novo usuário
        usuarioRepository.save(usuario);
        model.addAttribute("sucesso", "Cadastro realizado com sucesso!");
        return "redirect:/login"; // Redireciona para o login após sucesso
    }


    // =======================
    // PERFIL DO USUÁRIO
    // =======================
    @GetMapping("/perfil")
public String perfil(HttpSession session, Model model) {
    String cpf = (String) session.getAttribute("cpfUsuario");

    if (cpf != null) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(cpf);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("usuario", usuario);
            model.addAttribute("nomeUsuario", usuario.getNome());
            model.addAttribute("perfilUsuario", "COMUM");

            List<Agendamento> agendamentos = agendamentoRepository.findByCpfUsuario(cpf);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            List<Map<String, String>> consultasFormatadas = new ArrayList<>();

            for (Agendamento ag : agendamentos) {
                Map<String, String> dados = new HashMap<>();
                dados.put("data", ag.getDataAgendamento().format(formatter));
                dados.put("status", ag.getStatusAgendamento());
                dados.put("problema", ag.getProblemaOdontologico());
                dados.put("idAgendamento", String.valueOf(ag.getIdAgendamento()));
                
                // Adiciona o nome do médico, se houver
                if (ag.getMedico() != null) {
                    dados.put("medico", ag.getMedico().getNome());
                } else {
                    dados.put("medico", "Não atribuído");
                }

                // Adiciona os campos 'diagnostico', 'historico', e 'tratamento'
                dados.put("diagnostico", ag.getDiagnostico() != null ? ag.getDiagnostico() : "Não disponível");
                dados.put("historico", ag.getHistorico() != null ? ag.getHistorico() : "Não disponível");
                dados.put("tratamento", ag.getTratamento() != null ? ag.getTratamento() : "Não disponível");

                consultasFormatadas.add(dados);
            }

            model.addAttribute("consultas", consultasFormatadas);
            return "perfil";
        }
    }

    return "redirect:/login";
}

 @Autowired
    private UsuarioRepository usuarioR;
    @PostMapping("/excluir")
    public String excluirUsuario(@RequestParam("cpf") String cpf) {
        Usuario usuario = usuarioR.findById(cpf).orElse(null);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
        }
        return "redirect:/login"; // Redireciona após a exclusão
    }



@GetMapping("/paginaFuncionario")
public String paginaFuncionario(HttpSession session, Model model) {
    // Obtém o CPF da sessão
    String cpf = (String) session.getAttribute("cpfUsuario");

    // Verifica se o CPF está na sessão
    if (cpf != null) {
        // Busca o funcionário diretamente no banco de dados com o CPF
        Funcionario funcionario = funcionarioRepository.findByCpf(cpf);

        if (funcionario != null) {
            // Formatar a data de admissão
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataAdmissaoFormatada = funcionario.getDataAdmissao().format(formatter);

            // Adiciona os dados do funcionário ao modelo
            model.addAttribute("dataAdmissaoFormatada", dataAdmissaoFormatada);
            model.addAttribute("funcionario", funcionario);

            // Buscar os agendamentos do funcionário com base no CPF (médico.cpf)
            List<Agendamento> agendamentos = agendamentoRepository.findByMedico_Cpf(cpf);
            model.addAttribute("agendamentos", agendamentos); // Adiciona os agendamentos ao modelo

            return "paginaFuncionario"; // Retorna a página do funcionário
        } else {
            model.addAttribute("erro", "Funcionário não encontrado.");
        }
    }

    // Caso o CPF não esteja na sessão ou o funcionário não seja encontrado, redireciona para o login
    return "redirect:/login";
}

    // =======================
    // PÁGINA DO ADMINISTRADOR
    // =======================
    @GetMapping("/paginaAdm")
public String paginaAdministrador(HttpSession session, Model model) {
    String perfil = (String) session.getAttribute("perfilUsuario");

    if ("ADMIN".equals(perfil)) {
        // Recupera todos os funcionários
        model.addAttribute("funcionarios", funcionarioRepository.findAll());

        // Recupera todos os agendamentos
        List<Agendamento> agendamentos = agendamentoRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        List<Map<String, String>> consultasFormatadas = new ArrayList<>();

        for (Agendamento ag : agendamentos) {
            Map<String, String> dados = new HashMap<>();
            dados.put("data", ag.getDataAgendamento().format(formatter));
            dados.put("status", ag.getStatusAgendamento());
            dados.put("problema", ag.getProblemaOdontologico());
            dados.put("idAgendamento", String.valueOf(ag.getIdAgendamento()));
            
            // Adiciona o nome do médico, se houver
            if (ag.getMedico() != null) {
                dados.put("medico", ag.getMedico().getNome());
            } else {
                dados.put("medico", "Não atribuído");
            }

            // Adiciona os campos 'diagnostico', 'historico', e 'tratamento'
            dados.put("diagnostico", ag.getDiagnostico() != null ? ag.getDiagnostico() : "Não disponível");
            dados.put("historico", ag.getHistorico() != null ? ag.getHistorico() : "Não disponível");
            dados.put("tratamento", ag.getTratamento() != null ? ag.getTratamento() : "Não disponível");

            consultasFormatadas.add(dados);
        }

        // Adiciona os agendamentos formatados ao modelo
        model.addAttribute("consultas", consultasFormatadas);
        
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
    public String excluirFuncionario(@RequestParam("cpf") String cpf) {
        // Excluir agendamentos relacionados
        List<Agendamento> agendamentos = agendamentoRepository.findByMedico_Cpf(cpf);
        for (Agendamento agendamento : agendamentos) {
            agendamentoRepository.delete(agendamento);
        }
        // Excluir o funcionário
        Funcionario funcionario = funcionarioRepository.findById(cpf).orElse(null);
        if (funcionario != null) {
            funcionarioRepository.delete(funcionario);
        }
        return "redirect:/paginaAdm";
    }

    // =======================
    // RECUPERAÇÃO DE SENHA
    // =======================
    @PostMapping("/enviarRecuperacaoSenha")
public String enviarRecuperacaoSenha(@RequestParam String email, Model model) {
    // Verificar se é um usuário
    Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
    
    if (usuario.isPresent()) {
        return "redirect:/mudarSenha?email=" + email;
    }

    // Verificar se é um funcionário
    Optional<Funcionario> funcionario = funcionarioRepository.findByEmail(email);

    if (funcionario.isPresent()) {
        return "redirect:/mudarSenhaFuncionario?email=" + email;
    }

    model.addAttribute("erro", "E-mail não encontrado.");
    return "esqueciSenha";
}


    @GetMapping("/mudarSenhaFuncionario")
    public String mostrarTelaMudarSenhaFuncionario(@RequestParam String email, Model model) {
        // Lógica para verificar se o e-mail pertence a um funcionário
        model.addAttribute("email", email);
        return "mudarSenhaFuncionario";  // Certifique-se de que o nome do arquivo .html é "mudarSenhaFuncionario"
    }
    

    @PostMapping("/salvarNovaSenha")
public String salvarNovaSenha(@RequestParam String email,
                               @RequestParam String novaSenha,
                               Model model) {
    // Verificar se é um usuário
    Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

    if (usuario.isPresent()) {
        usuario.get().setSenha(novaSenha);
        usuarioRepository.save(usuario.get());
        return "login";  // Redireciona para login após salvar a nova senha
    }

    // Verificar se é um funcionário
    Optional<Funcionario> funcionario = funcionarioRepository.findByEmail(email);

    if (funcionario.isPresent()) {
        funcionario.get().setSenha(novaSenha);
        funcionarioRepository.save(funcionario.get());
        return "login";  // Redireciona para login após salvar a nova senha
    }

    model.addAttribute("erro", "Erro ao tentar mudar a senha.");
    return "login";  // Caso o e-mail não seja encontrado nem como usuário nem como funcionário
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

    @GetMapping("/sessao/usuario")
    @ResponseBody
    public String getCpfUsuario(HttpSession session) {
        String cpf = (String) session.getAttribute("cpfUsuario");
        return cpf != null ? cpf : "";
    }

    @GetMapping("/sessao/nome")
@ResponseBody
public Map<String, String> getNomeUsuarioLogado(HttpSession session) {
    String cpf = (String) session.getAttribute("cpfUsuario");
    String perfil = (String) session.getAttribute("perfilUsuario");

    Map<String, String> resposta = new HashMap<>();

    if (cpf != null && perfil != null) {
        resposta.put("perfil", perfil);

        switch (perfil) {
            case "FUNCIONARIO":
                funcionarioRepository.findById(cpf).ifPresent(func -> resposta.put("nome", func.getNome()));
                break;
            case "COMUM":
                usuarioRepository.findById(cpf).ifPresent(user -> resposta.put("nome", user.getNome()));
                break;
            case "ADMIN":
                resposta.put("nome", "Administrador");
                break;
        }
    }

    return resposta;
}

@GetMapping("/sessao/dados")
@ResponseBody
public Map<String, String> getDadosSessao(HttpSession session) {
    String cpf = (String) session.getAttribute("cpfUsuario");
    String perfil = (String) session.getAttribute("perfilUsuario");

    Map<String, String> dados = new HashMap<>();

    if (cpf != null && perfil != null) {
        dados.put("cpf", cpf);
        dados.put("perfil", perfil);

        if ("COMUM".equals(perfil)) {
            usuarioRepository.findById(cpf).ifPresent(usuario -> dados.put("nome", usuario.getNome()));
        } else if ("FUNCIONARIO".equals(perfil)) {
            funcionarioRepository.findById(cpf).ifPresent(funcionario -> dados.put("nome", funcionario.getNome()));
        } else if ("ADMIN".equals(perfil)) {
            dados.put("nome", "Administrador");
        }
    }

    return dados;
}

@PostMapping("/usuario/foto/upload")
public String uploadFotoPerfil(@RequestParam("foto") MultipartFile foto,
                               @RequestParam("cpf") String cpf) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(cpf);

    if (usuarioOpt.isPresent()) {
        Usuario usuario = usuarioOpt.get();
        try {
            usuario.setFotoPerfil(foto.getBytes());
            usuarioRepository.save(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return "redirect:/perfil";
}

@GetMapping("/usuario/foto/{cpf}")
public ResponseEntity<byte[]> mostrarFotoPerfil(@PathVariable String cpf) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(cpf);

    if (usuarioOpt.isPresent() && usuarioOpt.get().getFotoPerfil() != null) {
        byte[] imagem = usuarioOpt.get().getFotoPerfil();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagem);
    }

    return ResponseEntity.notFound().build();
}
@PostMapping("/usuario/foto/remover")
public String removerFoto(@RequestParam("cpf") String cpf) {
    usuarioRepository.findById(cpf).ifPresent(usuario -> {
        usuario.setFotoPerfil(null);
        usuarioRepository.save(usuario);
    });

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


