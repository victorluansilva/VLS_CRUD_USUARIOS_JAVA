package com.vls.crud_usuarios_java.controller;

import com.vls.crud_usuarios_java.model.Usuario;
import com.vls.crud_usuarios_java.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class UserListController {

    @FXML
    private TableView<Usuario> tableView;
    @FXML
    private TableColumn<Usuario, String> colNome;
    @FXML
    private TableColumn<Usuario, String> colSobrenome;
    @FXML
    private TableColumn<Usuario, String> colEmail;
    @FXML
    private TableColumn<Usuario, String> colTelefone;
    @FXML
    private TableColumn<Usuario, String> colLogin;
    @FXML
    private TableColumn<Usuario, Void> colAcoes;

    @FXML
    private Label statusLabel;
    @FXML
    private Button syncButton;

    private UsuarioService usuarioService;
    private ObservableList<Usuario> obsUsuario;


    public void initialize(){
        usuarioService = new UsuarioService();
        carregarDadosTabela();
    }

    public void atualizarStatusConexao(){

    }
    @FXML
    public void handleSincronizar(){

    }

    public void carregarDadosTabela(){
        if (usuarioService.isDbloaded()) {
            colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
            colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));

            obsUsuario = FXCollections.observableArrayList(usuarioService.listarUsuarios());
            tableView.setItems(obsUsuario);
        } else{
            System.out.println("Dados não foram carregados.");
        }
    }

    public void adicionarBotoesDeAcao(){
        colAcoes.setCellFactory(pram -> new TableCell<>() {
           private final Button btnEditar = new Button("Editar");
           private final Button btnExcluir = new Button("Excluir");
           private final HBox panel = new HBox(5,btnEditar,btnExcluir);
            {
                btnEditar.setOnAction(event ->{
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    abrirFormularioUsuario(usuario);
                });
                btnExcluir.setOnAction(event ->{
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    usuarioService.excluirUsuario(usuario);
                    carregarDadosTabela();
                });
            }


        });
    }

    @FXML
    public void handleAdicionarUsuario(){

    }

    public void abrirFormularioUsuario(Usuario usuario){

    }

}
