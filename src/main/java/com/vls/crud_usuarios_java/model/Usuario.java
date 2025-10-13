package com.vls.crud_usuarios_java.model;
import java.util.Date;

public class Usuario extends Pessoa{
    private int id;
    private String login;
    private String senha;
    private String email;

    public Usuario() {
        super();
        this.id = 0;
        this.login = "";
        this.senha = "";
        this.email = "";
    }

    public Usuario(String nome, Date dataNasc) {
        super(nome, dataNasc);
        this.id = 0;
        this.login = "";
        this.senha = "";
        this.email = "";
    }

    public Usuario(String nome, String sobrenome, Date dataNasc, String telefone, char sexo, String endereco, String login, String senha, String email) {
        super(nome, sobrenome, dataNasc, telefone, sexo, endereco);
        this.id = 0;
        this.login = login;
        this.senha = senha;
        this.email = email;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEqual(String senha){
        return senha.equals(this.senha);
    }

}
