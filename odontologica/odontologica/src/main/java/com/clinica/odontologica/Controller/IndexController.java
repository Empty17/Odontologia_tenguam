package com.clinica.odontologica.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

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
}
