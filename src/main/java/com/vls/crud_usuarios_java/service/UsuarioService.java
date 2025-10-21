package com.vls.crud_usuarios_java.service;

import com.vls.crud_usuarios_java.model.Usuario;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService {

    private static final List<Usuario> usuarios = new ArrayList<>();
    private static boolean dbLoaded = false;

    public UsuarioService() {
        if (!dbLoaded) {
            carregarUsuariosDoBanco();
            dbLoaded = true;
        }
    }

    private void carregarUsuariosDoBanco() {
        usuarios.clear();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSobrenome(rs.getString("sobrenome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setLogin(rs.getString("login"));
                usuario.setDataNascimento(rs.getDate("dataNascimento").toLocalDate());
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setSexo(rs.getString("sexo") != null ? rs.getString("sexo").charAt(0) : ' ');
                usuario.setEndereco(rs.getString("endereco"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarios;
    }

    public void adicionarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios(nome, sobrenome, email, login) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getSobrenome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getLogin());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter o ID do usuário criado.");
                }
            }
            usuarios.add(usuario);

        } catch (SQLException e) {
            showAlert("Operação em Memória", "Conexão indisponível. O novo usuário foi salvo apenas nesta sessão.");
            usuario.setId(-(usuarios.size() + 1));
            usuarios.add(usuario);
        }
    }

    public void atualizarUsuario(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuario.getId()) {
                usuarios.set(i, usuario);
                break;
            }
        }
        if (usuario.getId() < 0) {
            showAlert("Operação em Memória", "Usuário atualizado na sessão. Sincronize para salvar no banco.");
            return;
        }

        String sql = "UPDATE usuarios SET nome = ?, sobrenome = ?, email = ?, login = ? WHERE id = ?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getSobrenome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getLogin());
            stmt.setInt(5, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Operação em Memória", "Conexão indisponível. O usuário foi atualizado apenas nesta sessão.");
        }
    }

    public void excluirUsuario(Usuario usuario) {
        usuarios.remove(usuario);
        if (usuario.getId() < 0) {
            showAlert("Operação em Memória", "Usuário offline removido da sessão.");
            return;
        }

        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Operação em Memória", "Conexão indisponível. O usuário foi excluído apenas desta sessão.");
        }
    }

    public void sincronizarComBanco() {
        if (!DatabaseService.testarConexao()) {
            showAlert("Sincronização Falhou", "Não foi possível reconectar ao banco de dados.");
            return;
        }
        List<Usuario> usuariosParaSincronizar = usuarios.stream()
                .filter(u -> u.getId() < 0)
                .collect(Collectors.toList());
        if (!usuariosParaSincronizar.isEmpty()) {
            String sql = "INSERT INTO usuarios(nome, sobrenome, email, login) VALUES(?, ?, ?, ?)";
            int sucessoCount = 0;
            try (Connection conn = DatabaseService.getConnection()) {
                for (Usuario usuario : usuariosParaSincronizar) {
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, usuario.getNome());
                        stmt.setString(2, usuario.getSobrenome());
                        stmt.setString(3, usuario.getEmail());
                        stmt.setString(4, usuario.getLogin());
                        stmt.executeUpdate();
                        sucessoCount++;
                    }
                }
                showAlert("Sincronização", sucessoCount + " usuário(s) foram salvos no banco de dados.");
            } catch (SQLException e) {
                showAlert("Erro na Sincronização", "Ocorreu um erro. Nem todos os dados podem ter sido salvos.");
                e.printStackTrace();
            }
        } else {
            showAlert("Reconectado", "Conexão com o banco de dados restabelecida.");
        }
        carregarUsuariosDoBanco();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
