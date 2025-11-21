package model;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Aplicação principal da academia GymSolver.
 * Tem:
 *  - Tela de login
 *  - Painel principal com menu lateral + abas
 */
public class GymSolverApp extends Application {

    // "Banco de dados" em memória
    private final AcademiaRepository repositorio = new AcademiaRepository();

    // Para conseguir trocar as abas a partir do menu lateral
    private TabPane tabPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Sempre começa mostrando a tela de login
        mostrarTelaLogin(primaryStage);
    }

    // ============================================================
    //                      TELA DE LOGIN
    // ============================================================

    private void mostrarTelaLogin(Stage stage) {
        // Título
        Label lblTitulo = new Label("GymSolver");
        lblTitulo.getStyleClass().add("header-title");

        Label lblSub = new Label("Login do sistema");
        lblSub.getStyleClass().add("header-subtitle");

        // Campos
        Label lblUsuario = new Label("Usuário:");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("admin");

        Label lblSenha = new Label("Senha:");
        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("123");

        // Botões
        Button btnEntrar = new Button("Entrar");
        btnEntrar.getStyleClass().add("button-primary");

        Button btnSair = new Button("Sair");
        btnSair.getStyleClass().add("button-ghost");

        HBox linhaBotoes = new HBox(10, btnSair, btnEntrar);
        linhaBotoes.setAlignment(Pos.CENTER_RIGHT);

        // Formulário em grid
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);
        form.add(lblUsuario, 0, 0);
        form.add(txtUsuario, 1, 0);
        form.add(lblSenha, 0, 1);
        form.add(txtSenha, 1, 1);
        form.add(linhaBotoes, 1, 2);

        // Card central
        VBox card = new VBox(16, lblTitulo, lblSub, form);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(24));
        card.setMaxWidth(380);

        BorderPane root = new BorderPane(card);
        BorderPane.setAlignment(card, Pos.CENTER);
        root.setPadding(new Insets(24));

        Scene cenaLogin = new Scene(root, 520, 340);
        // Aplica CSS (ajuste o caminho se necessário)
        try {
            cenaLogin.getStylesheets().add(
                    getClass().getResource("/view/dark-theme.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Não encontrou CSS (dark-theme.css).");
        }

        stage.setTitle("GymSolver - Login");
        stage.setScene(cenaLogin);
        stage.centerOnScreen();
        stage.show();

        // ===== Ações =====

        btnSair.setOnAction(e -> Platform.exit());

        btnEntrar.setOnAction(e -> {
            String u = txtUsuario.getText().trim();
            String s = txtSenha.getText().trim();

            // Validação simples só para exemplo
            if (u.equals("admin") && s.equals("123")) {
                abrirPainelPrincipal(stage);
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR,
                        "Usuário ou senha inválidos. Use admin / 123.",
                        ButtonType.OK);
                alerta.setHeaderText("Login inválido");
                alerta.initOwner(stage);
                alerta.showAndWait();
            }
        });
    }

    // ============================================================
    //                   PAINEL PRINCIPAL
    // ============================================================

    private void abrirPainelPrincipal(Stage stage) {

        // 1) TabPane central
        tabPane = new TabPane();
        tabPane.getStyleClass().add("main-tab-pane");

        tabPane.getTabs().setAll(
                criarTabClientes(),
                criarTabFuncionarios(),
                criarTabPlanosTreino(),
                criarTabPlanosAssinatura(),
                criarTabSobre()
        );

        // 2) Sidebar (menu lateral)
        VBox sidebar = criarSidebar();

        // Layout principal
        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(tabPane);
        root.setPadding(new Insets(16));

        Scene cena = new Scene(root, 1200, 720);
        try {
            cena.getStylesheets().add(
                    getClass().getResource("/view/dark-theme.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Não encontrou CSS (dark-theme.css).");
        }

        stage.setTitle("GymSolver - Painel da Academia");
        stage.setScene(cena);
        stage.centerOnScreen();
        stage.show();

        // Deixa Clientes como aba inicial
        tabPane.getSelectionModel().select(0);
    }

    // ============================================================
    //                        SIDEBAR
    // ============================================================

    private VBox criarSidebar() {
        Label lblMenu = new Label("MENU");

        ToggleButton btnClientes = new ToggleButton("Clientes");
        ToggleButton btnFuncionarios = new ToggleButton("Funcionários");
        ToggleButton btnPlanosTreino = new ToggleButton("Planos de Treino");
        ToggleButton btnPlanosAssinatura = new ToggleButton("Planos de Assinatura");
        ToggleButton btnSobre = new ToggleButton("Sobre");

        // Classes CSS
        btnClientes.getStyleClass().add("sidebar-button");
        btnFuncionarios.getStyleClass().add("sidebar-button");
        btnPlanosTreino.getStyleClass().add("sidebar-button");
        btnPlanosAssinatura.getStyleClass().add("sidebar-button");
        btnSobre.getStyleClass().add("sidebar-button");

        ToggleGroup grupo = new ToggleGroup();
        btnClientes.setToggleGroup(grupo);
        btnFuncionarios.setToggleGroup(grupo);
        btnPlanosTreino.setToggleGroup(grupo);
        btnPlanosAssinatura.setToggleGroup(grupo);
        btnSobre.setToggleGroup(grupo);

        btnClientes.setSelected(true);

        // Quando clicar no menu, muda a aba do TabPane
        btnClientes.setOnAction(e -> tabPane.getSelectionModel().select(0));
        btnFuncionarios.setOnAction(e -> tabPane.getSelectionModel().select(1));
        btnPlanosTreino.setOnAction(e -> tabPane.getSelectionModel().select(2));
        btnPlanosAssinatura.setOnAction(e -> tabPane.getSelectionModel().select(3));
        btnSobre.setOnAction(e -> tabPane.getSelectionModel().select(4));

        VBox box = new VBox(12, lblMenu,
                btnClientes,
                btnFuncionarios,
                btnPlanosTreino,
                btnPlanosAssinatura,
                btnSobre);
        box.getStyleClass().add("sidebar");
        return box;
    }

    // ============================================================
    //                        TAB CLIENTES
    // ============================================================

    private Tab criarTabClientes() {
        Tab tab = new Tab("Clientes");
        tab.setClosable(false);

        // Tabela
        TableView<Cliente> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(repositorio.getClientes());

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Cliente, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Cliente, String> colPlanoAss = new TableColumn<>("Plano Assinatura");
        colPlanoAss.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getPlanoAssinatura() != null
                                ? c.getValue().getPlanoAssinatura().getNome()
                                : ""
                ));

        TableColumn<Cliente, String> colPlanoTreino = new TableColumn<>("Plano Treino");
        colPlanoTreino.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getPlanoTreino() != null
                                ? c.getValue().getPlanoTreino().getNome()
                                : ""
                ));

        tabela.getColumns().setAll(colId, colNome, colEmail, colTelefone, colPlanoAss, colPlanoTreino);

        // ---------------- Formulário ----------------

        Label lblId = new Label("ID:");
        TextField txtId = new TextField();
        txtId.setEditable(false);
        txtId.setPromptText("Gerado automaticamente");

        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do cliente");

        Label lblEmail = new Label("E-mail:");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("E-mail do cliente");

        Label lblTelefone = new Label("Telefone:");
        TextField txtTelefone = new TextField();
        txtTelefone.setPromptText("Telefone do cliente");

        Label lblPlanoAss = new Label("Plano Assinatura:");
        ComboBox<PlanoAssinatura> cbPlanoAss = new ComboBox<>(repositorio.getPlanosAssinatura());
        cbPlanoAss.setPromptText("Selecione o plano de assinatura");

        Label lblPlanoTreino = new Label("Plano Treino:");
        ComboBox<PlanoTreino> cbPlanoTreino = new ComboBox<>(repositorio.getPlanosTreino());
        cbPlanoTreino.setPromptText("Selecione o plano de treino");

        // Grid de formulário
        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);
        form.add(lblId, 0, 0);
        form.add(txtId, 1, 0);
        form.add(lblNome, 0, 1);
        form.add(txtNome, 1, 1);
        form.add(lblEmail, 0, 2);
        form.add(txtEmail, 1, 2);
        form.add(lblTelefone, 0, 3);
        form.add(txtTelefone, 1, 3);
        form.add(lblPlanoAss, 0, 4);
        form.add(cbPlanoAss, 1, 4);
        form.add(lblPlanoTreino, 0, 5);
        form.add(cbPlanoTreino, 1, 5);

        // Botões
        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        btnNovo.getStyleClass().add("button-ghost");
        btnSalvar.getStyleClass().add("button-primary");
        btnExcluir.getStyleClass().add("button-danger");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);

        // Card em volta do formulário
        VBox cardFormulario = new VBox(16, form, botoes);
        cardFormulario.getStyleClass().add("card");

        VBox layout = new VBox(16, tabela, cardFormulario);
        layout.setPadding(new Insets(8, 0, 0, 0));
        VBox.setVgrow(tabela, Priority.ALWAYS);

        tab.setContent(layout);

        // --------- Comportamento básico ---------

        // Preenche o formulário ao selecionar na tabela
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                txtId.setText(String.valueOf(selecionado.getId()));
                txtNome.setText(selecionado.getNome());
                txtEmail.setText(selecionado.getEmail());
                txtTelefone.setText(selecionado.getTelefone());
                cbPlanoAss.getSelectionModel().select(selecionado.getPlanoAssinatura());
                cbPlanoTreino.getSelectionModel().select(selecionado.getPlanoTreino());
            }
        });

        // Novo: gera próximo ID e limpa campos
        btnNovo.setOnAction(e -> {
            int id = repositorio.proximoIdCliente();
            txtId.setText(String.valueOf(id));
            txtNome.clear();
            txtEmail.clear();
            txtTelefone.clear();
            cbPlanoAss.getSelectionModel().clearSelection();
            cbPlanoTreino.getSelectionModel().clearSelection();
            tabela.getSelectionModel().clearSelection();
        });

        // Salvar (insere ou atualiza)
        btnSalvar.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                String nome = txtNome.getText();
                String email = txtEmail.getText();
                String telefone = txtTelefone.getText();
                PlanoAssinatura pa = cbPlanoAss.getValue();
                PlanoTreino pt = cbPlanoTreino.getValue();

                if (nome.isEmpty()) {
                    mostrarAlerta("Dados inválidos", "Informe o nome do cliente.");
                    return;
                }

                ObservableList<Cliente> lista = repositorio.getClientes();
                Cliente existente = lista.stream()
                        .filter(c -> c.getId() == id)
                        .findFirst()
                        .orElse(null);

                if (existente == null) {
                    Cliente novo = new Cliente(id, nome, email, telefone, pa, pt);
                    repositorio.salvarCliente(novo);
                    tabela.getSelectionModel().select(novo);
                } else {
                    existente.setNome(nome);
                    existente.setEmail(email);
                    existente.setTelefone(telefone);
                    existente.setPlanoAssinatura(pa);
                    existente.setPlanoTreino(pt);
                    tabela.refresh();
                }

            } catch (NumberFormatException ex) {
                mostrarAlerta("ID inválido", "Clique em 'Novo' para gerar um ID antes de salvar.");
            }
        });

        // Excluir
        btnExcluir.setOnAction(e -> {
            Cliente selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                repositorio.removerCliente(selecionado);
                tabela.getSelectionModel().clearSelection();
                btnLimpar.fire();
            }
        });

        // Limpar
        btnLimpar.setOnAction(e -> {
            txtId.clear();
            txtNome.clear();
            txtEmail.clear();
            txtTelefone.clear();
            cbPlanoAss.getSelectionModel().clearSelection();
            cbPlanoTreino.getSelectionModel().clearSelection();
            tabela.getSelectionModel().clearSelection();
        });

        return tab;
    }

    // ============================================================
    //                     TAB FUNCIONÁRIOS
    // ============================================================

    private Tab criarTabFuncionarios() {
        Tab tab = new Tab("Funcionários");
        tab.setClosable(false);

        TableView<Funcionario> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(repositorio.getFuncionarios());

        TableColumn<Funcionario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Funcionario, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Funcionario, String> colCargo = new TableColumn<>("Cargo");
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        TableColumn<Funcionario, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        tabela.getColumns().setAll(colId, colNome, colCargo, colCpf);

        // Form
        Label lblId = new Label("ID:");
        TextField txtId = new TextField();
        txtId.setEditable(false);
        txtId.setPromptText("Gerado automaticamente");

        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField();

        Label lblCargo = new Label("Cargo:");
        TextField txtCargo = new TextField();

        Label lblCpf = new Label("CPF:");
        TextField txtCpf = new TextField();

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);
        form.add(lblId, 0, 0);
        form.add(txtId, 1, 0);
        form.add(lblNome, 0, 1);
        form.add(txtNome, 1, 1);
        form.add(lblCargo, 0, 2);
        form.add(txtCargo, 1, 2);
        form.add(lblCpf, 0, 3);
        form.add(txtCpf, 1, 3);

        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        btnNovo.getStyleClass().add("button-ghost");
        btnSalvar.getStyleClass().add("button-primary");
        btnExcluir.getStyleClass().add("button-danger");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(16, form, botoes);
        card.getStyleClass().add("card");

        VBox layout = new VBox(16, tabela, card);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        tab.setContent(layout);

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antes, f) -> {
            if (f != null) {
                txtId.setText(String.valueOf(f.getId()));
                txtNome.setText(f.getNome());
                txtCargo.setText(f.getCargo());
                txtCpf.setText(f.getCpf());
            }
        });

        btnNovo.setOnAction(e -> {
            int id = repositorio.proximoIdFuncionario();
            txtId.setText(String.valueOf(id));
            txtNome.clear();
            txtCargo.clear();
            txtCpf.clear();
            tabela.getSelectionModel().clearSelection();
        });

        btnSalvar.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                String nome = txtNome.getText();
                String cargo = txtCargo.getText();
                String cpf = txtCpf.getText();

                if (nome.isEmpty()) {
                    mostrarAlerta("Dados inválidos", "Informe o nome do funcionário.");
                    return;
                }

                ObservableList<Funcionario> lista = repositorio.getFuncionarios();
                Funcionario existente = lista.stream()
                        .filter(f -> f.getId() == id)
                        .findFirst()
                        .orElse(null);

                if (existente == null) {
                    Funcionario novo = new Funcionario(id, nome, cargo, cpf);
                    repositorio.salvarFuncionario(novo);
                    tabela.getSelectionModel().select(novo);
                } else {
                    existente.setNome(nome);
                    existente.setCargo(cargo);
                    existente.setCpf(cpf);
                    tabela.refresh();
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("ID inválido", "Clique em 'Novo' para gerar um ID antes de salvar.");
            }
        });

        btnExcluir.setOnAction(e -> {
            Funcionario f = tabela.getSelectionModel().getSelectedItem();
            if (f != null) {
                repositorio.removerFuncionario(f);
                tabela.getSelectionModel().clearSelection();
                btnLimpar.fire();
            }
        });

        btnLimpar.setOnAction(e -> {
            txtId.clear();
            txtNome.clear();
            txtCargo.clear();
            txtCpf.clear();
            tabela.getSelectionModel().clearSelection();
        });

        return tab;
    }

    // ============================================================
    //                   TAB PLANOS DE TREINO
    // ============================================================

    private Tab criarTabPlanosTreino() {
        Tab tab = new Tab("Planos de Treino");
        tab.setClosable(false);

        TableView<PlanoTreino> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(repositorio.getPlanosTreino());

        TableColumn<PlanoTreino, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PlanoTreino, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<PlanoTreino, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<PlanoTreino, String> colObjetivo = new TableColumn<>("Objetivo");
        colObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));

        tabela.getColumns().setAll(colId, colNome, colDescricao, colObjetivo);

        Label lblId = new Label("ID:");
        TextField txtId = new TextField();
        txtId.setEditable(false);

        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do plano de treino");

        Label lblDesc = new Label("Descrição:");
        TextArea txtDesc = new TextArea();
        txtDesc.setPromptText("Descrição do plano (ex.: divisão de treinos, volume, etc.)");
        txtDesc.setPrefRowCount(3);

        Label lblObj = new Label("Objetivo:");
        TextField txtObj = new TextField();
        txtObj.setPromptText("Objetivo (hipertrofia, emagrecimento, etc.)");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);
        form.add(lblId, 0, 0);
        form.add(txtId, 1, 0);
        form.add(lblNome, 0, 1);
        form.add(txtNome, 1, 1);
        form.add(lblDesc, 0, 2);
        form.add(txtDesc, 1, 2);
        form.add(lblObj, 0, 3);
        form.add(txtObj, 1, 3);

        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        btnNovo.getStyleClass().add("button-ghost");
        btnSalvar.getStyleClass().add("button-primary");
        btnExcluir.getStyleClass().add("button-danger");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(16, form, botoes);
        card.getStyleClass().add("card");

        VBox layout = new VBox(16, tabela, card);
        VBox.setVgrow(tabela, Priority.ALWAYS);
        tab.setContent(layout);

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antes, p) -> {
            if (p != null) {
                txtId.setText(String.valueOf(p.getId()));
                txtNome.setText(p.getNome());
                txtDesc.setText(p.getDescricao());
                txtObj.setText(p.getObjetivo());
            }
        });

        btnNovo.setOnAction(e -> {
            int id = repositorio.proximoIdPlanoTreino();
            txtId.setText(String.valueOf(id));
            txtNome.clear();
            txtDesc.clear();
            txtObj.clear();
            tabela.getSelectionModel().clearSelection();
        });

        btnSalvar.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                String nome = txtNome.getText();
                String desc = txtDesc.getText();
                String obj = txtObj.getText();

                if (nome.isEmpty()) {
                    mostrarAlerta("Dados inválidos", "Informe o nome do plano de treino.");
                    return;
                }

                ObservableList<PlanoTreino> lista = repositorio.getPlanosTreino();
                PlanoTreino existente = lista.stream()
                        .filter(p -> p.getId() == id)
                        .findFirst()
                        .orElse(null);

                if (existente == null) {
                    PlanoTreino novo = new PlanoTreino(id, nome, desc, obj);
                    repositorio.salvarPlanoTreino(novo);
                    tabela.getSelectionModel().select(novo);
                } else {
                    existente.setNome(nome);
                    existente.setDescricao(desc);
                    existente.setObjetivo(obj);
                    tabela.refresh();
                }

            } catch (NumberFormatException ex) {
                mostrarAlerta("ID inválido", "Clique em 'Novo' para gerar um ID antes de salvar.");
            }
        });

        btnExcluir.setOnAction(e -> {
            PlanoTreino p = tabela.getSelectionModel().getSelectedItem();
            if (p != null) {
                repositorio.removerPlanoTreino(p);
                tabela.getSelectionModel().clearSelection();
                btnLimpar.fire();
            }
        });

        btnLimpar.setOnAction(e -> {
            txtId.clear();
            txtNome.clear();
            txtDesc.clear();
            txtObj.clear();
            tabela.getSelectionModel().clearSelection();
        });

        return tab;
    }

    // ============================================================
    //                 TAB PLANOS DE ASSINATURA
    // ============================================================

    private Tab criarTabPlanosAssinatura() {
        Tab tab = new Tab("Planos de Assinatura");
        tab.setClosable(false);

        TableView<PlanoAssinatura> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(repositorio.getPlanosAssinatura());

        TableColumn<PlanoAssinatura, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PlanoAssinatura, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<PlanoAssinatura, Double> colValor = new TableColumn<>("Valor mensal (R$)");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorMensal"));

        TableColumn<PlanoAssinatura, Integer> colDuracao = new TableColumn<>("Duração (meses)");
        colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracaoMeses"));

        tabela.getColumns().setAll(colId, colNome, colValor, colDuracao);

        Label lblId = new Label("ID:");
        TextField txtId = new TextField();
        txtId.setEditable(false);

        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField();

        Label lblValor = new Label("Valor mensal (R$):");
        TextField txtValor = new TextField();

        Label lblDuracao = new Label("Duração (meses):");
        TextField txtDuracao = new TextField();

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);
        form.add(lblId, 0, 0);
        form.add(txtId, 1, 0);
        form.add(lblNome, 0, 1);
        form.add(txtNome, 1, 1);
        form.add(lblValor, 0, 2);
        form.add(txtValor, 1, 2);
        form.add(lblDuracao, 0, 3);
        form.add(txtDuracao, 1, 3);

        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        btnNovo.getStyleClass().add("button-ghost");
        btnSalvar.getStyleClass().add("button-primary");
        btnExcluir.getStyleClass().add("button-danger");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(16, form, botoes);
        card.getStyleClass().add("card");

        VBox layout = new VBox(16, tabela, card);
        VBox.setVgrow(tabela, Priority.ALWAYS);
        tab.setContent(layout);

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antes, p) -> {
            if (p != null) {
                txtId.setText(String.valueOf(p.getId()));
                txtNome.setText(p.getNome());
                txtValor.setText(String.valueOf(p.getValorMensal()));
                txtDuracao.setText(String.valueOf(p.getDuracaoMeses()));
            }
        });

        btnNovo.setOnAction(e -> {
            int id = repositorio.proximoIdPlanoAssinatura();
            txtId.setText(String.valueOf(id));
            txtNome.clear();
            txtValor.clear();
            txtDuracao.clear();
            tabela.getSelectionModel().clearSelection();
        });

        btnSalvar.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                String nome = txtNome.getText();
                double valor = Double.parseDouble(txtValor.getText().replace(",", "."));
                int duracao = Integer.parseInt(txtDuracao.getText());

                if (nome.isEmpty()) {
                    mostrarAlerta("Dados inválidos", "Informe o nome do plano de assinatura.");
                    return;
                }

                ObservableList<PlanoAssinatura> lista = repositorio.getPlanosAssinatura();
                PlanoAssinatura existente = lista.stream()
                        .filter(p -> p.getId() == id)
                        .findFirst()
                        .orElse(null);

                if (existente == null) {
                    PlanoAssinatura novo = new PlanoAssinatura(id, nome, valor, duracao);
                    repositorio.salvarPlanoAssinatura(novo);
                    tabela.getSelectionModel().select(novo);
                } else {
                    existente.setNome(nome);
                    existente.setValorMensal(valor);
                    existente.setDuracaoMeses(duracao);
                    tabela.refresh();
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Dados inválidos", "Verifique valor e duração (números).");
            }
        });

        btnExcluir.setOnAction(e -> {
            PlanoAssinatura p = tabela.getSelectionModel().getSelectedItem();
            if (p != null) {
                repositorio.removerPlanoAssinatura(p);
                tabela.getSelectionModel().clearSelection();
                btnLimpar.fire();
            }
        });

        btnLimpar.setOnAction(e -> {
            txtId.clear();
            txtNome.clear();
            txtValor.clear();
            txtDuracao.clear();
            tabela.getSelectionModel().clearSelection();
        });

        return tab;
    }

    // ============================================================
    //                          TAB SOBRE
    // ============================================================

    private Tab criarTabSobre() {
        Tab tab = new Tab("Sobre");
        tab.setClosable(false);

        Label titulo = new Label("GymSolver - Sistema de Gerenciamento de Academia");
        titulo.getStyleClass().add("sobre-title");

        Label texto = new Label(
                "Sistema voltado para a administração de academias, permitindo:\n" +
                        "- Controle de planos de treino e assinatura;\n" +
                        "- Cadastro e acompanhamento de clientes;\n" +
                        "- Gestão de funcionários.\n\n" +
                        "Este é um protótipo acadêmico, com dados armazenados em memória (sem banco de dados)."
        );
        texto.setWrapText(true);

        VBox conteudo = new VBox(12, titulo, texto);
        conteudo.getStyleClass().add("card");
        conteudo.setMaxWidth(800);

        BorderPane root = new BorderPane(conteudo);
        BorderPane.setAlignment(conteudo, Pos.TOP_LEFT);
        root.setPadding(new Insets(16));

        tab.setContent(root);
        return tab;
    }

    // ============================================================
    //                      MÉTODO ÚTIL
    // ============================================================

    private void mostrarAlerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(titulo);
        a.showAndWait();
    }
}
