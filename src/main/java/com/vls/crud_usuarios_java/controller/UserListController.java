package com.vls.crud_usuarios_java.controller;

import com.vls.crud_usuarios_java.MainApplication;
import com.vls.crud_usuarios_java.model.Usuario;
import com.vls.crud_usuarios_java.service.DatabaseService;
import com.vls.crud_usuarios_java.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UserListController {

    @FXML
    private TableView<Usuario> tableView;
    @FXML
    private TableColumn<Usuario, String> colNome;
    @FXML
    private TableColumn<Usuario, String> colIdade;
    @FXML
    private TableColumn<Usuario, String> colLogin;
    @FXML
    private TableColumn<Usuario, Void> colAcoes;

    @FXML
    private Label statusLabel;
    @FXML
    private Button syncButton;

    private UsuarioService usuarioService;
    private ObservableList<Usuario> obsUsuarios;


    public void initialize(){
        usuarioService = new UsuarioService();
        carregarDadosTabela();
    }

    private void atualizarStatusConexao(){
        boolean isConnected = DatabaseService.testarConexao();
        if (isConnected){
            statusLabel.setText("DB Status: connected");
            statusLabel.setStyle("-fx-text-fill: green");
            syncButton.setText("refresh");
            syncButton.setDisable(true);
            syncButton.setVisible(false);
        } else{
            statusLabel.setText("DB Status: offline");
            statusLabel.setStyle("-fx-text-fill: red");
            syncButton.setText("retry");
            syncButton.setDisable(false);
            syncButton.setVisible(true);
        }
    }
    @FXML
    private void handleSincronizar(){
        usuarioService.sincronizarComBanco();
        carregarDadosTabela();

    }

    private void carregarDadosTabela(){
            colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
            colIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));

            obsUsuarios = FXCollections.observableArrayList(usuarioService.listarUsuarios());
            tableView.setItems(obsUsuarios);
            adicionarBotoesDeAcao();
            atualizarStatusConexao();
    }

    private void adicionarBotoesDeAcao() {
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox pane = new HBox(5, btnEditar, btnExcluir);

            {
                btnEditar.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    abrirFormularioUsuario(usuario);
                });
                btnExcluir.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    usuarioService.excluirUsuario(usuario);
                    carregarDadosTabela();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @FXML
    private void handleAdicionarUsuario() {
        abrirFormularioUsuario(null);
    }

    private void abrirFormularioUsuario(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("user-form-view.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            UserFormController controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setStage(stage);

            stage.showAndWait();

            carregarDadosTabela();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
