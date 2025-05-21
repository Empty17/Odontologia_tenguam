package com.clinica.odontologica.config;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> noCacheFilterRegistration(NoCacheFilter filter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);

        // Páginas onde o cache deve ser desabilitado
        registration.addUrlPatterns(
            "/perfil", 
            "/paginaFuncionario", 
            "/paginaAdm"
        );

        registration.setName("noCacheFilter");
        registration.setOrder(1); // Prioridade do filtro
        return registration;
    }
}