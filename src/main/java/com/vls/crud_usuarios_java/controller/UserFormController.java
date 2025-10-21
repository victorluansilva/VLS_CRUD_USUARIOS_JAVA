package com.vls.crud_usuarios_java.controller;

import com.vls.crud_usuarios_java.model.Usuario;
import com.vls.crud_usuarios_java.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserFormController {

    @FXML private Label titleLabel;
    @FXML private TextField nomeField;
    @FXML private TextField sobrenomeField;
    @FXML private TextField emailField;
    @FXML private TextField loginField;
    @FXML private DatePicker dataNascimentoPicker;
    @FXML private TextField telefoneField;
    @FXML private ChoiceBox<String> sexoChoiceBox;
    @FXML private TextField enderecoField;

    private Stage stage;
    private Usuario usuario;
    private UsuarioService usuarioService;

    public void initialize() {
        usuarioService = new UsuarioService();
        sexoChoiceBox.setItems(FXCollections.observableArrayList("Masculino", "Feminino"));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            titleLabel.setText("Editar Usuário");
            nomeField.setText(usuario.getNome());
            sobrenomeField.setText(usuario.getSobrenome());
            emailField.setText(usuario.getEmail());
            loginField.setText(usuario.getLogin());
            if (usuario.getDataNascimento() != null) {
                dataNascimentoPicker.setValue(usuario.getDataNascimento());
            }
            telefoneField.setText(usuario.getTelefone());
            sexoChoiceBox.setValue(usuario.getSexo() == 'M' ? "Masculino" : "Feminino");
            enderecoField.setText(usuario.getEndereco());
        } else {
            titleLabel.setText("Adicionar Usuário");
        }
    }

    @FXML
    private void handleSalvar() {
        boolean isNew = (usuario == null);
        if (isNew) {
            usuario = new Usuario();
        }

        usuario.setNome(nomeField.getText());
        usuario.setSobrenome(sobrenomeField.getText());
        usuario.setEmail(emailField.getText());
        usuario.setLogin(loginField.getText());
        if (dataNascimentoPicker.getValue() != null) {
            usuario.setDataNascimento(dataNascimentoPicker.getValue());
        }
        usuario.setTelefone(telefoneField.getText());
        if (sexoChoiceBox.getValue() != null) {
            usuario.setSexo(sexoChoiceBox.getValue().equals("Masculino") ? 'M' : 'F');
        }
        usuario.setEndereco(enderecoField.getText());

        if (isNew) {
            usuarioService.adicionarUsuario(usuario);
        } else {
            usuarioService.atualizarUsuario(usuario);
        }

        stage.close();
    }
}