Projeto Integrador - Senac 3° semestre

# 🦷 Clínica Odontológica - Sistema de Agendamento

Este é um sistema backend desenvolvido em Java para gerenciamento de uma clínica odontológica. Ele permite realizar agendamentos, gerenciar pacientes, profissionais, entre outras funcionalidades relacionadas à administração de uma clínica.

## 🚀 Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Maven
- REST APIs
- MySQL (ou outro banco de dados relacional)
- JPA / Hibernate

## 📁 Estrutura do Projeto

```
odontologica/
├── src/main/java/com/clinica/odontologica/
│   ├── Controller/         # Controladores REST
│   ├── config/             # Configurações da aplicação (ex: filtros, CORS)
│   ├── model/              # Entidades do banco de dados
│   ├── repository/         # Interfaces de acesso aos dados
│   ├── service/            # Lógica de negócio
│   └── OdontologicaApplication.java  # Classe principal
├── pom.xml                 # Configurações do Maven
```


## 📌 Funcionalidades Principais

- Cadastro e login de usuários
- Agendamento de consultas odontológicas
- Gerenciamento de pacientes e profissionais
- Interface RESTful para integração com frontend

