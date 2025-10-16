package com.vls.crud_usuarios_java.service;

import com.vls.crud_usuarios_java.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    public static final List<Usuario> usuarios = new ArrayList<>();
    public static boolean dbloaded = false;

    public UsuarioService() {
        if (!dbloaded) {
            carregarUsuarioDoBanco();
            dbloaded = true;
        }
    }

    //INICIO - CREATE
    public void adicionarUsuario(Usuario usuario){
        String sql = "INSERT INTO usuarios (nome, sobrenome, email, login) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ){
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getSobrenome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getLogin());
            stmt.executeUpdate();

            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    usuario.setId(generatedKeys.getInt(1));
                } else{
                    throw new SQLException("Falha ao obter o id do novo registro");
                }
            }
        } catch (SQLException e){
            showAlert("Operação em Memóriua","Conexão indisponível. O novo usuário foi salvo apenas na memória local ");
            usuario.setId(-(usuarios.size() + 1));
            usuarios.add(usuario);
        }

    }
    //FIM - CREATE

    // INICIO - READ
    public void carregarUsuarioDoBanco() {
        usuarios.clear();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery();) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSobrenome(rs.getString("sobrenome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setLogin(rs.getString("login"));
                usuario.setDataNascimento(rs.getDate("dataNascimento"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setSexo(rs.getString("sexo") != null ? rs.getString("sexo").charAt(0) : ' ');
                usuario.setEndereco(rs.getString("endereco"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

    }

    public List<Usuario> listarUsuarios() {
        return usuarios;
    }
    // FIM - READ

    //INICIO - UPDATE

    public void atualizarUsuario(Usuario usuario){
        for
    }

    //FIM - UPDATE

    public void excluirUsuario(Usuario usuario) {
    }

    public void sincronizarComBanco() {

    }
}
