package model;
// GymSolverApp.java
// Classe principal da aplicação JavaFX

import java.util.Objects;
import javafx.scene.paint.Color;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
// OBS: se você estiver usando packages, coloque algo como:
// package br.edu.utfpr.gymsolver;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GymSolverApp extends Application {

    // "Banco de dados" em memória da academia
    private AcademiaRepository repositorio = new AcademiaRepository();

    public static void main(String[] args) {
        launch(args); // dispara a aplicação JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        // Título da janela (barra do Windows)
        primaryStage.setTitle("GymSolver - Painel da Academia");

        // ---------------------------------------------------------------------
        // TABPANE CENTRAL (conteúdo principal)
        // ---------------------------------------------------------------------
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("main-tab-pane");

        tabPane.getTabs().add(criarTabClientes());
        tabPane.getTabs().add(criarTabFuncionarios());
        tabPane.getTabs().add(criarTabPlanosTreino());
        tabPane.getTabs().add(criarTabPlanosAssinatura());
        tabPane.getTabs().add(criarTabSobre());

        // ---------------------------------------------------------------------
        // HEADER (topo), com título do sistema
        // ---------------------------------------------------------------------
        Label titulo = new Label("GymSolver");
        titulo.getStyleClass().add("header-title");

        Label subtitulo = new Label("Painel de gerenciamento da academia");
        subtitulo.getStyleClass().add("header-subtitle");

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox header = new HBox(10, titulo, headerSpacer, subtitulo);
        header.getStyleClass().add("header-bar");

        // ---------------------------------------------------------------------
        // SIDEBAR (menu lateral)
        // ---------------------------------------------------------------------
        VBox sidebar = new VBox(8);
        sidebar.getStyleClass().add("sidebar");

        Label lblMenu = new Label("MENU");
        // o CSS já estiliza qualquer label dentro da sidebar
        ToggleGroup navGroup = new ToggleGroup();

        ToggleButton btnClientes = criarBotaoSidebar("Clientes", navGroup, tabPane, 0);
        ToggleButton btnFuncionarios = criarBotaoSidebar("Funcionários", navGroup, tabPane, 1);
        ToggleButton btnPlanosTreino = criarBotaoSidebar("Planos de Treino", navGroup, tabPane, 2);
        ToggleButton btnPlanosAssinatura = criarBotaoSidebar("Planos de Assinatura", navGroup, tabPane, 3);
        ToggleButton btnSobre = criarBotaoSidebar("Sobre", navGroup, tabPane, 4);

        // Deixa "Clientes" como tela inicial selecionada
        navGroup.selectToggle(btnClientes);

        sidebar.getChildren().addAll(
                lblMenu,
                btnClientes,
                btnFuncionarios,
                btnPlanosTreino,
                btnPlanosAssinatura,
                btnSobre
        );

        // ---------------------------------------------------------------------
        // LAYOUT PRINCIPAL (BorderPane)
        // ---------------------------------------------------------------------
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");

        root.setTop(header);
        root.setLeft(sidebar);
        root.setCenter(tabPane);

        BorderPane.setMargin(tabPane, new Insets(16));
        BorderPane.setMargin(sidebar, new Insets(16, 0, 16, 16));

        Scene scene = new Scene(root, 1200, 700);
        // deixa o fundo da cena escuro (caso algo escape do CSS)
        scene.setFill(Color.web("#050509"));
        // Carrega o CSS do tema dark
        scene.getStylesheets().add(
            Objects.requireNonNull(
                GymSolverApp.class.getResource("/view/dark-theme.css")
        ).toExternalForm()
);


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Cria um botão de menu lateral (sidebar) que, ao ser clicado,
     * seleciona a aba correspondente do TabPane.
     */
    private ToggleButton criarBotaoSidebar(String texto, ToggleGroup grupo, TabPane tabPane, int tabIndex) {
        ToggleButton btn = new ToggleButton(texto);
        btn.setToggleGroup(grupo);
        btn.getStyleClass().add("sidebar-button");
        btn.setMaxWidth(Double.MAX_VALUE); // faz o botão ocupar toda a largura
        btn.setOnAction(e -> tabPane.getSelectionModel().select(tabIndex));
        return btn;
    }


    // -----------------------------------------------------------
    // ABA CLIENTES
    // -----------------------------------------------------------
    private Tab criarTabClientes() {
        Tab tab = new Tab("Clientes");
        tab.setClosable(false);

        // ----------------- TABELA -----------------
        TableView<Cliente> tabela = new TableView<>();
        tabela.setItems(repositorio.getClientes());
        tabela.getStyleClass().add("data-table"); // só pra aplicar o CSS da tabela

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Cliente, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Cliente, String> colPlanoAssinatura = new TableColumn<>("Plano Assinatura");
        colPlanoAssinatura.setCellValueFactory(new PropertyValueFactory<>("planoAssinatura"));

        TableColumn<Cliente, String> colPlanoTreino = new TableColumn<>("Plano Treino");
        colPlanoTreino.setCellValueFactory(new PropertyValueFactory<>("planoTreino"));

        tabela.getColumns().addAll(colId, colNome, colEmail, colTelefone, colPlanoAssinatura, colPlanoTreino);

        // ----------------- FORMULÁRIO -----------------
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do cliente");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("E-mail do cliente");

        TextField txtTelefone = new TextField();
        txtTelefone.setPromptText("Telefone do cliente");

        ComboBox<String> cbPlanoAssinatura = new ComboBox<>();
        cbPlanoAssinatura.setPromptText("Selecione o plano de assinatura");
        atualizarComboPlanosAssinatura(cbPlanoAssinatura);

        ComboBox<String> cbPlanoTreino = new ComboBox<>();
        cbPlanoTreino.setPromptText("Selecione o plano de treino");
        atualizarComboPlanosTreino(cbPlanoTreino);

        // ----------------- BOTÕES -----------------
        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        // classes CSS pros botões (cores)
        btnNovo.getStyleClass().add("button-ghost");
        btnSalvar.getStyleClass().add("button-primary");
        btnExcluir.getStyleClass().add("button-danger");
        btnLimpar.getStyleClass().add("button-ghost");

        // Quando seleciona um cliente na tabela, preenche o form
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, novoSel) -> {
            if (novoSel != null) {
                txtId.setText(String.valueOf(novoSel.getId()));
                txtNome.setText(novoSel.getNome());
                txtEmail.setText(novoSel.getEmail());
                txtTelefone.setText(novoSel.getTelefone());
                cbPlanoAssinatura.setValue(novoSel.getPlanoAssinatura());
                cbPlanoTreino.setValue(novoSel.getPlanoTreino());
            }
        });

        // Ações dos botões (é o mesmo código que você já tinha)
        btnNovo.setOnAction(e -> {
            int novoId = repositorio.gerarProximoIdCliente();
            txtId.setText(String.valueOf(novoId));
            txtNome.clear();
            txtEmail.clear();
            txtTelefone.clear();
            cbPlanoAssinatura.setValue(null);
            cbPlanoTreino.setValue(null);
            tabela.getSelectionModel().clearSelection();
        });

        btnSalvar.setOnAction(e -> {
            String nome = txtNome.getText();
            if (nome == null || nome.isBlank()) {
                mostrarAlerta("Validação", "Nome é obrigatório para o cliente.");
                return;
            }

            int id;
            if (txtId.getText().isBlank()) {
                id = repositorio.gerarProximoIdCliente();
                txtId.setText(String.valueOf(id));
            } else {
                id = Integer.parseInt(txtId.getText());
            }

            String email = txtEmail.getText();
            String telefone = txtTelefone.getText();
            String planoAssinatura = cbPlanoAssinatura.getValue();
            String planoTreino = cbPlanoTreino.getValue();

            Cliente clienteExistente = repositorio.buscarClientePorId(id);
            if (clienteExistente == null) {
                Cliente novo = new Cliente(id, nome, email, telefone,
                        planoAssinatura, planoTreino);
                repositorio.getClientes().add(novo);
            } else {
                clienteExistente.setNome(nome);
                clienteExistente.setEmail(email);
                clienteExistente.setTelefone(telefone);
                clienteExistente.setPlanoAssinatura(planoAssinatura);
                clienteExistente.setPlanoTreino(planoTreino);
                tabela.refresh();
            }
        });

        btnExcluir.setOnAction(e -> {
            Cliente selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                mostrarAlerta("Excluir", "Selecione um cliente para excluir.");
                return;
            }
            repositorio.getClientes().remove(selecionado);
            limparCamposCliente(txtId, txtNome, txtEmail, txtTelefone,
                    cbPlanoAssinatura, cbPlanoTreino, tabela);
        });

        btnLimpar.setOnAction(e -> {
            limparCamposCliente(txtId, txtNome, txtEmail, txtTelefone,
                    cbPlanoAssinatura, cbPlanoTreino, tabela);
        });

        // ----------------- LAYOUT DO FORMULÁRIO -----------------
        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(10));

        formulario.add(new Label("ID:"), 0, 0);
        formulario.add(txtId, 1, 0);

        formulario.add(new Label("Nome:"), 0, 1);
        formulario.add(txtNome, 1, 1);

        formulario.add(new Label("E-mail:"), 0, 2);
        formulario.add(txtEmail, 1, 2);

        formulario.add(new Label("Telefone:"), 0, 3);
        formulario.add(txtTelefone, 1, 3);

        formulario.add(new Label("Plano Assinatura:"), 0, 4);
        formulario.add(cbPlanoAssinatura, 1, 4);

        formulario.add(new Label("Plano Treino:"), 0, 5);
        formulario.add(cbPlanoTreino, 1, 5);

        HBox botoes = new HBox(10, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);

        // ----------------- AQUI ENTRA O "CARD" -----------------
        // Card que envolve formulário + botões, com fundo escuro e bordas arredondadas
        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        // Layout final da aba: tabela em cima, card embaixo
        // Layout final da aba: tabela em cima, card embaixo
        VBox vbox = new VBox(12, tabela, cardFormulario);

        // topo = 0, lados = 16, bottom = 16
        vbox.setPadding(new Insets(0, 16, 16, 16));

        VBox.setVgrow(tabela, Priority.ALWAYS);
        tab.setContent(vbox);


        tab.setContent(vbox);
        return tab;
    }


    private void limparCamposCliente(TextField txtId, TextField txtNome, TextField txtEmail, TextField txtTelefone, ComboBox<String> cbPlanoAssinatura, ComboBox<String> cbPlanoTreino, TableView<Cliente> tabela) {
        txtId.clear();
        txtNome.clear();
        txtEmail.clear();
        txtTelefone.clear();
        cbPlanoAssinatura.setValue(null);
        cbPlanoTreino.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }

    private void atualizarComboPlanosAssinatura(ComboBox<String> combo) {
        ObservableList<String> nomesPlanos = FXCollections.observableArrayList();
        for (PlanoAssinatura p : repositorio.getPlanosAssinatura()) {
            nomesPlanos.add(p.getNome());
        }
        combo.setItems(nomesPlanos);
    }

    private void atualizarComboPlanosTreino(ComboBox<String> combo) {
        ObservableList<String> nomesPlanos = FXCollections.observableArrayList();
        for (PlanoTreino p : repositorio.getPlanosTreino()) {
            nomesPlanos.add(p.getNome());
        }
        combo.setItems(nomesPlanos);
    }

    // -----------------------------------------------------------
    // ABA FUNCIONÁRIOS
    // -----------------------------------------------------------
    private Tab criarTabFuncionarios() {
        Tab tab = new Tab("Funcionários");
        tab.setClosable(false);

        // ----------------- TABELA -----------------
        TableView<Funcionario> tabela = new TableView<>();
        tabela.setItems(repositorio.getFuncionarios());
        tabela.getStyleClass().add("data-table");

        TableColumn<Funcionario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Funcionario, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Funcionario, String> colCargo = new TableColumn<>("Cargo");
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        TableColumn<Funcionario, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        tabela.getColumns().addAll(colId, colNome, colCargo, colCpf);

        // ----------------- FORMULÁRIO -----------------
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do funcionário");

        TextField txtCargo = new TextField();
        txtCargo.setPromptText("Cargo (instrutor, recepcionista, etc.)");

        TextField txtCpf = new TextField();
        txtCpf.setPromptText("CPF do funcionário");

        // ----------------- BOTÕES -----------------
        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        btnNovo.getStyleClass().add("button-ghost");
        btnSalvar.getStyleClass().add("button-primary");
        btnExcluir.getStyleClass().add("button-danger");
        btnLimpar.getStyleClass().add("button-ghost");

        // Preenche o formulário ao selecionar na tabela
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, novoSel) -> {
            if (novoSel != null) {
                txtId.setText(String.valueOf(novoSel.getId()));
                txtNome.setText(novoSel.getNome());
                txtCargo.setText(novoSel.getCargo());
                txtCpf.setText(novoSel.getCpf());
            }
        });

        // Ações dos botões
        btnNovo.setOnAction(e -> {
            int novoId = repositorio.gerarProximoIdFuncionario();
            txtId.setText(String.valueOf(novoId));
            txtNome.clear();
            txtCargo.clear();
            txtCpf.clear();
            tabela.getSelectionModel().clearSelection();
        });

        btnSalvar.setOnAction(e -> {
            String nome = txtNome.getText();
            if (nome == null || nome.isBlank()) {
                mostrarAlerta("Validação", "Nome é obrigatório para o funcionário.");
                return;
            }

            int id;
            if (txtId.getText().isBlank()) {
                id = repositorio.gerarProximoIdFuncionario();
                txtId.setText(String.valueOf(id));
            } else {
                id = Integer.parseInt(txtId.getText());
            }

            String cargo = txtCargo.getText();
            String cpf = txtCpf.getText();

            Funcionario funcExistente = repositorio.buscarFuncionarioPorId(id);
            if (funcExistente == null) {
                Funcionario novo = new Funcionario(id, nome, cargo, cpf);
                repositorio.getFuncionarios().add(novo);
            } else {
                funcExistente.setNome(nome);
                funcExistente.setCargo(cargo);
                funcExistente.setCpf(cpf);
                tabela.refresh();
            }
        });

        btnExcluir.setOnAction(e -> {
            Funcionario selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                mostrarAlerta("Excluir", "Selecione um funcionário para excluir.");
                return;
            }
            repositorio.getFuncionarios().remove(selecionado);
            txtId.clear();
            txtNome.clear();
            txtCargo.clear();
            txtCpf.clear();
            tabela.getSelectionModel().clearSelection();
        });

        btnLimpar.setOnAction(e -> {
            txtId.clear();
            txtNome.clear();
            txtCargo.clear();
            txtCpf.clear();
            tabela.getSelectionModel().clearSelection();
        });

        // ----------------- LAYOUT -----------------
        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(10));

        formulario.add(new Label("ID:"), 0, 0);
        formulario.add(txtId, 1, 0);

        formulario.add(new Label("Nome:"), 0, 1);
        formulario.add(txtNome, 1, 1);

        formulario.add(new Label("Cargo:"), 0, 2);
        formulario.add(txtCargo, 1, 2);

        formulario.add(new Label("CPF:"), 0, 3);
        formulario.add(txtCpf, 1, 3);

        HBox botoes = new HBox(10, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);

        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        // Layout final da aba: tabela em cima, card embaixo
        VBox vbox = new VBox(12, tabela, cardFormulario);

        // topo = 0, lados = 16, bottom = 16
        vbox.setPadding(new Insets(0, 16, 16, 16));

        VBox.setVgrow(tabela, Priority.ALWAYS);
        tab.setContent(vbox);


        tab.setContent(vbox);
        return tab;
    }


    // -----------------------------------------------------------
    // ABA PLANOS DE TREINO
    // -----------------------------------------------------------
    private Tab criarTabPlanosTreino() {
        Tab tab = new Tab("Planos de Treino");
        tab.setClosable(false);

        // ----------------- TABELA -----------------
        TableView<PlanoTreino> tabela = new TableView<>();
        tabela.setItems(repositorio.getPlanosTreino());
        tabela.getStyleClass().add("data-table");

        TableColumn<PlanoTreino, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PlanoTreino, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<PlanoTreino, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<PlanoTreino, String> colObjetivo = new TableColumn<>("Objetivo");
        colObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));

        tabela.getColumns().addAll(colId, colNome, colDescricao, colObjetivo);

        // ----------------- FORMULÁRIO -----------------
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do plano de treino");

        TextArea txtDescricao = new TextArea();
        txtDescricao.setPromptText("Descrição do plano (ex.: divisão de treinos, volume, etc.)");
        txtDescricao.setPrefRowCount(3);

        TextField txtObjetivo = new TextField();
        txtObjetivo.setPromptText("Objetivo (hipertrofia, emagrecimento, etc.)");

        // ----------------- BOTÕES -----------------
        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        btnNovo.getStyleClass().add("button-ghost");
        btnSalvar.getStyleClass().add("button-primary");
        btnExcluir.getStyleClass().add("button-danger");
        btnLimpar.getStyleClass().add("button-ghost");

        // Preenche form ao selecionar na tabela
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, novoSel) -> {
            if (novoSel != null) {
                txtId.setText(String.valueOf(novoSel.getId()));
                txtNome.setText(novoSel.getNome());
                txtDescricao.setText(novoSel.getDescricao());
                txtObjetivo.setText(novoSel.getObjetivo());
            }
        });

        // Ações
        btnNovo.setOnAction(e -> {
            int novoId = repositorio.gerarProximoIdPlanoTreino();
            txtId.setText(String.valueOf(novoId));
            txtNome.clear();
            txtDescricao.clear();
            txtObjetivo.clear();
            tabela.getSelectionModel().clearSelection();
        });

        btnSalvar.setOnAction(e -> {
            String nome = txtNome.getText();
            if (nome == null || nome.isBlank()) {
                mostrarAlerta("Validação", "Nome é obrigatório para o plano de treino.");
                return;
            }

            int id;
            if (txtId.getText().isBlank()) {
                id = repositorio.gerarProximoIdPlanoTreino();
                txtId.setText(String.valueOf(id));
            } else {
                id = Integer.parseInt(txtId.getText());
            }

            String descricao = txtDescricao.getText();
            String objetivo = txtObjetivo.getText();

            PlanoTreino planoExistente = repositorio.buscarPlanoTreinoPorId(id);
            if (planoExistente == null) {
                PlanoTreino novo = new PlanoTreino(id, nome, descricao, objetivo);
                repositorio.getPlanosTreino().add(novo);
            } else {
                planoExistente.setNome(nome);
                planoExistente.setDescricao(descricao);
                planoExistente.setObjetivo(objetivo);
                tabela.refresh();
            }

            // Atualiza combos da aba de Clientes
            atualizarCombosClientes();
        });

        btnExcluir.setOnAction(e -> {
            PlanoTreino selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                mostrarAlerta("Excluir", "Selecione um plano de treino para excluir.");
                return;
            }
            repositorio.getPlanosTreino().remove(selecionado);
            txtId.clear();
            txtNome.clear();
            txtDescricao.clear();
            txtObjetivo.clear();
            tabela.getSelectionModel().clearSelection();

            atualizarCombosClientes();
        });

        btnLimpar.setOnAction(e -> {
            txtId.clear();
            txtNome.clear();
            txtDescricao.clear();
            txtObjetivo.clear();
            tabela.getSelectionModel().clearSelection();
        });

        // ----------------- LAYOUT -----------------
        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(10));

        formulario.add(new Label("ID:"), 0, 0);
        formulario.add(txtId, 1, 0);

        formulario.add(new Label("Nome:"), 0, 1);
        formulario.add(txtNome, 1, 1);

        formulario.add(new Label("Descrição:"), 0, 2);
        formulario.add(txtDescricao, 1, 2);

        formulario.add(new Label("Objetivo:"), 0, 3);
        formulario.add(txtObjetivo, 1, 3);

        HBox botoes = new HBox(10, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);

        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        // Layout final da aba: tabela em cima, card embaixo
        VBox vbox = new VBox(12, tabela, cardFormulario);

        // topo = 0, lados = 16, bottom = 16
        vbox.setPadding(new Insets(0, 16, 16, 16));

        VBox.setVgrow(tabela, Priority.ALWAYS);
        tab.setContent(vbox);


        tab.setContent(vbox);
        return tab;
    }


    // -----------------------------------------------------------
    // ABA PLANOS DE ASSINATURA
    // -----------------------------------------------------------
    private Tab criarTabPlanosAssinatura() {
        Tab tab = new Tab("Planos de Assinatura");
        tab.setClosable(false);

        // ----------------- TABELA -----------------
        TableView<PlanoAssinatura> tabela = new TableView<>();
        tabela.setItems(repositorio.getPlanosAssinatura());
        tabela.getStyleClass().add("data-table");

        TableColumn<PlanoAssinatura, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PlanoAssinatura, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<PlanoAssinatura, Double> colValor = new TableColumn<>("Valor mensal (R$)");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorMensal"));

        TableColumn<PlanoAssinatura, Integer> colDuracao = new TableColumn<>("Duração (meses)");
        colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracaoMeses"));

        tabela.getColumns().addAll(colId, colNome, colValor, colDuracao);

        // ----------------- FORMULÁRIO -----------------
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do plano (Mensal, Trimestral, etc.)");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Valor mensal (R$)");

        TextField txtDuracao = new TextField();
        txtDuracao.setPromptText("Duração em meses");

        // ----------------- BOTÕES -----------------
        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        btnNovo.getStyleClass().add("button-ghost");
        btnSalvar.getStyleClass().add("button-primary");
        btnExcluir.getStyleClass().add("button-danger");
        btnLimpar.getStyleClass().add("button-ghost");

        // Preenche form ao selecionar na tabela
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, novoSel) -> {
            if (novoSel != null) {
                txtId.setText(String.valueOf(novoSel.getId()));
                txtNome.setText(novoSel.getNome());
                txtValor.setText(String.valueOf(novoSel.getValorMensal()));
                txtDuracao.setText(String.valueOf(novoSel.getDuracaoMeses()));
            }
        });

        // Ações
        btnNovo.setOnAction(e -> {
            int novoId = repositorio.gerarProximoIdPlanoAssinatura();
            txtId.setText(String.valueOf(novoId));
            txtNome.clear();
            txtValor.clear();
            txtDuracao.clear();
            tabela.getSelectionModel().clearSelection();
        });

        btnSalvar.setOnAction(e -> {
            String nome = txtNome.getText();
            if (nome == null || nome.isBlank()) {
                mostrarAlerta("Validação", "Nome é obrigatório para o plano de assinatura.");
                return;
            }

            double valor;
            int duracao;
            try {
                valor = Double.parseDouble(txtValor.getText().replace(",", "."));
                duracao = Integer.parseInt(txtDuracao.getText());
            } catch (NumberFormatException ex) {
                mostrarAlerta("Validação", "Valor mensal e duração devem ser numéricos.");
                return;
            }

            int id;
            if (txtId.getText().isBlank()) {
                id = repositorio.gerarProximoIdPlanoAssinatura();
                txtId.setText(String.valueOf(id));
            } else {
                id = Integer.parseInt(txtId.getText());
            }

            PlanoAssinatura planoExistente = repositorio.buscarPlanoAssinaturaPorId(id);
            if (planoExistente == null) {
                PlanoAssinatura novo = new PlanoAssinatura(id, nome, valor, duracao);
                repositorio.getPlanosAssinatura().add(novo);
            } else {
                planoExistente.setNome(nome);
                planoExistente.setValorMensal(valor);
                planoExistente.setDuracaoMeses(duracao);
                tabela.refresh();
            }

            atualizarCombosClientes();
        });

        btnExcluir.setOnAction(e -> {
            PlanoAssinatura selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                mostrarAlerta("Excluir", "Selecione um plano de assinatura para excluir.");
                return;
            }
            repositorio.getPlanosAssinatura().remove(selecionado);
            txtId.clear();
            txtNome.clear();
            txtValor.clear();
            txtDuracao.clear();
            tabela.getSelectionModel().clearSelection();

            atualizarCombosClientes();
        });

        btnLimpar.setOnAction(e -> {
            txtId.clear();
            txtNome.clear();
            txtValor.clear();
            txtDuracao.clear();
            tabela.getSelectionModel().clearSelection();
        });

        // ----------------- LAYOUT -----------------
        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(10));

        formulario.add(new Label("ID:"), 0, 0);
        formulario.add(txtId, 1, 0);

        formulario.add(new Label("Nome:"), 0, 1);
        formulario.add(txtNome, 1, 1);

        formulario.add(new Label("Valor mensal (R$):"), 0, 2);
        formulario.add(txtValor, 1, 2);

        formulario.add(new Label("Duração (meses):"), 0, 3);
        formulario.add(txtDuracao, 1, 3);

        HBox botoes = new HBox(10, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);

        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        // Layout final da aba: tabela em cima, card embaixo
        VBox vbox = new VBox(12, tabela, cardFormulario);

        // topo = 0, lados = 16, bottom = 16
        vbox.setPadding(new Insets(0, 16, 16, 16));

        VBox.setVgrow(tabela, Priority.ALWAYS);
        tab.setContent(vbox);

        tab.setContent(vbox);
        return tab;
    }

        /**
     * Atualiza os combos de planos na aba de Clientes.
     * 
     * Por enquanto deixei vazio só para não dar erro de compilação.
     * Se você quiser, depois podemos guardar uma referência aos ComboBox
     * da aba de clientes e, aqui dentro, chamar atualizarComboPlanosAssinatura(...)
     * e atualizarComboPlanosTreino(...).
     */
    private void atualizarCombosClientes() {
        // TODO: se você quiser que os ComboBox da aba de Clientes
        // atualizem automaticamente quando criar/editar um plano,
        // podemos implementar isso depois.
    }



    // -----------------------------------------------------------
    // ABA SOBRE
    // -----------------------------------------------------------
    // Aba SOBRE
    private Tab criarTabSobre() {
        Tab tab = new Tab("Sobre");
        tab.setClosable(false);

        // Título
        Label lblTitulo = new Label("GymSolver - Sistema de Gerenciamento de Academia");
        lblTitulo.getStyleClass().add("sobre-title");

        // Texto descritivo
        Label lblTexto = new Label(
                "Sistema voltado para a administração de academias, permitindo:\n" +
                "• Controle de planos de treino e assinatura;\n" +
                "• Cadastro e acompanhamento de clientes;\n" +
                "• Gestão de funcionários.\n\n" +
                "Este é um protótipo acadêmico, com dados armazenados em memória (sem banco de dados)."
        );
        lblTexto.setWrapText(true);
        lblTexto.getStyleClass().add("sobre-text");

        // Card com bordas arredondadas (reaproveita a classe .card do CSS)
        VBox cardSobre = new VBox(10, lblTitulo, lblTexto);
        cardSobre.getStyleClass().add("card");
        cardSobre.setMaxWidth(700);          // largura máxima do card
        cardSobre.setFillWidth(true);

        // Container externo só pra dar margem interna em relação à tela
        VBox container = new VBox(cardSobre);
        // topo = 0, direita = 16, baixo = 16, esquerda = 16
        container.setPadding(new Insets(0, 16, 16, 16));
        container.setAlignment(Pos.TOP_LEFT);


        tab.setContent(container);
        return tab;
    }


    // -----------------------------------------------------------
    // UTILITÁRIO DE ALERTA
    // -----------------------------------------------------------
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
