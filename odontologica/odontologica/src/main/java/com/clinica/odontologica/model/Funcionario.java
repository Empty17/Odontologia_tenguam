package com.clinica.odontologica.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Funcionarios")
public class Funcionario {

    @Id
    @Column(length = 14)
    private String cpf;

    private String nome;
    private String email;
    private String senha;
    private String endereco;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    private String telefone;

    @Lob
@Column(name = "foto")
private byte[] foto;

    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

    private String cargo;
    private String especialidade;

    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public byte[] getFoto() {
    return foto;
}

public void setFoto(byte[] foto) {
    this.foto = foto;
}
}