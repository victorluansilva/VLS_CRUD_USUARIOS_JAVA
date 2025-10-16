package com.vls.crud_usuarios_java.service;

import com.vls.crud_usuarios_java.model.Usuario;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuario.getId()){
                usuarios.set(i,usuario);
                break;
            }
        }
        if (usuario.getId() > 0){
            showAlert("Operação em Memória", "Usuário atualizado na sessão local.");
            return;
        }

        String sql = "UPDATE usuarios SET nome = ?, sobrenome = ?, email = ?, login = ? WHERE id = ?";
        try(Connection conn = DatabaseService.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1,usuario.getNome());
            stmt.setString(2,usuario.getSobrenome());
            stmt.setString(3,usuario.getEmail());
            stmt.setString(4,usuario.getEndereco());
            stmt.setInt(5,usuario.getId());
            stmt.executeUpdate();
        }catch (SQLException e){
            showAlert("Operação em Memória", "Usuário atualizado na sessão local.");
        }

    }

    //FIM - UPDATE

    public void excluirUsuario(Usuario usuario) {
    }

    //METODO PARA SINCRONIZAR COM O BANCO

    public void sincronizarComBanco() {
        if (!DatabaseService.testarConexao()){
            showAlert("Sincronização falhou","Não foi possível reconectar com o banco de dados.");
            return;
        }
        List<Usuario> usuariosParaSync = usuarios.stream().
                filter(u -> u.getId() < 0).collect(Collectors.toList());

        if (!usuariosParaSync.isEmpty()){
            String sql = "INSERT INTO usuarios (nome, sobrenome, email, login) VALUES (?,?,?,?)";
            int okCount = 0;
            try (Connection conn = DatabaseService.getConnection()) {
                for (Usuario user : usuariosParaSync) {
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, user.getNome());
                        stmt.setString(2, user.getSobrenome());
                        stmt.setString(3, user.getEmail());
                        stmt.setString(4, user.getLogin());
                        stmt.executeUpdate();
                        okCount++;
                    }
                }
                showAlert("Sincronizado!", okCount + "usuarios foram sincronizados no banco de dados.");
            } catch (SQLException e) {
                showAlert("Erro na sincronização", "Nem todos os dados foram salvos.");
                e.printStackTrace();
            }
        }else{
            showAlert("Reconectado","Conexão com o banco foi reestabelecida.");
        }
        carregarUsuarioDoBanco();
    }

    public void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
