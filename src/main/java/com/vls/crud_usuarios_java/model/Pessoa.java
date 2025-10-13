package com.vls.crud_usuarios_java.model;
import java.util.Date;

public class Pessoa {

    //ATRIBUTOS COM VISIBILIDADE PRIVADA
    private String nome;
    private String sobrenome;
    private Date dataNascimento;
    private int idade;
    private String telefone;
    private char sexo;
    private String endereco;

    //CONSTRUTORES
    public Pessoa() {
        this.nome = "";
        this.sobrenome = "";
        this.idade = 0;
        this.dataNascimento = new Date();
        this.telefone = "";
        this.sexo = ' ';
        this.endereco = "";
    }

    public Pessoa(String nome, Date dataNascimento) {
        this.nome = nome;
        this.sobrenome = "";
        this.idade = 0;
        this.dataNascimento = dataNascimento;
        this.telefone = "";
        this.sexo = ' ';
        this.endereco = "";
    }

    public Pessoa(String nome, String sobrenome, Date dataNasc, String telefone, char sexo, String endereco) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNasc;
        this.telefone = telefone;
        this.sexo = sexo;
        this.endereco = endereco;
    }
    //GETTES E SETTERS (ENCAPSULAMENTO)

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

}