package model;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * GymSolverApp
 * Aplicação JavaFX para gerenciamento simples de uma academia.
 */
public class GymSolverApp extends Application {

    // "Banco de dados" em memória
    private final AcademiaRepository repositorio = new AcademiaRepository();

    public static void main(String[] args) {
        launch(args); 
    }

    @Override
    public void start(Stage primaryStage) {
        mostrarTelaLogin(primaryStage);
    }

    // ------------------------------------------------------------
    //  TELA DE LOGIN
    // ------------------------------------------------------------
    private void mostrarTelaLogin(Stage stage) {
        Label titulo = new Label("GymSolver");
        titulo.getStyleClass().add("header-title");

        Label subtitulo = new Label("Painel de gerenciamento da academia");
        subtitulo.getStyleClass().add("header-subtitle");

        Label lblUsuario = new Label("Usuário");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("admin");

        Label lblSenha = new Label("Senha");
        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("123");

        Button btnEntrar = new Button("Entrar");
        btnEntrar.getStyleClass().add("button-primary");

        btnEntrar.setOnAction(e -> {
            String u = txtUsuario.getText();
            String s = txtSenha.getText();

            if ("admin".equals(u) && "123".equals(s)) {
                mostrarTelaPrincipal(stage);
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Login inválido");
                alert.setHeaderText("Usuário ou senha incorretos");
                alert.setContentText("Use usuário: admin   |   senha: 123");
                alert.showAndWait();
            }
        });

        VBox form = new VBox(10, lblUsuario, txtUsuario, lblSenha, txtSenha, btnEntrar);
        form.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(16, titulo, subtitulo, form);
        card.getStyleClass().add("card");
        card.setMaxWidth(360);

        VBox root = new VBox(card);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/view/dark-theme.css").toExternalForm());

        stage.setTitle("GymSolver - Login");
        stage.setScene(scene);
        stage.show();
    }

    // ------------------------------------------------------------
    //  TELA PRINCIPAL
    // ------------------------------------------------------------
    private void mostrarTelaPrincipal(Stage stage) {
        // TabPane com as abas principais
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("main-tab-pane");

        Tab tabClientes           = criarTabClientes();
        Tab tabFuncionarios       = criarTabFuncionarios();
        Tab tabPlanosTreino       = criarTabPlanosTreino();
        Tab tabPlanosAssinatura   = criarTabPlanosAssinatura();
        Tab tabSobre              = criarTabSobre();   // agora com o dashboard

        tabPane.getTabs().addAll(
                tabClientes,
                tabFuncionarios,
                tabPlanosTreino,
                tabPlanosAssinatura,
                tabSobre
        );

        // ------------------ Sidebar ------------------
        Label lblMenu = new Label("MENU");
        lblMenu.getStyleClass().add("sidebar-title");

        ToggleGroup menuGroup = new ToggleGroup();

        ToggleButton btnClientes = criarBotaoMenu("Clientes", menuGroup);
        ToggleButton btnFuncionarios = criarBotaoMenu("Funcionários", menuGroup);
        ToggleButton btnPlanosTreino = criarBotaoMenu("Planos de Treino", menuGroup);
        ToggleButton btnPlanosAssinatura = criarBotaoMenu("Planos de Assinatura", menuGroup);
        ToggleButton btnSobre = criarBotaoMenu("Sobre", menuGroup);

        btnClientes.setOnAction(e -> tabPane.getSelectionModel().select(tabClientes));
        btnFuncionarios.setOnAction(e -> tabPane.getSelectionModel().select(tabFuncionarios));
        btnPlanosTreino.setOnAction(e -> tabPane.getSelectionModel().select(tabPlanosTreino));
        btnPlanosAssinatura.setOnAction(e -> tabPane.getSelectionModel().select(tabPlanosAssinatura));
        btnSobre.setOnAction(e -> tabPane.getSelectionModel().select(tabSobre));

        btnClientes.setSelected(true);
        tabPane.getSelectionModel().select(tabClientes);

        VBox sidebar = new VBox(12,
                lblMenu,
                btnClientes,
                btnFuncionarios,
                btnPlanosTreino,
                btnPlanosAssinatura,
                btnSobre
        );
        sidebar.getStyleClass().add("sidebar");

        // ------------------ Header ------------------
        Label logo = new Label("GymSolver");
        logo.getStyleClass().add("header-title");

        Label lblTopo = new Label("Painel de gerenciamento da academia");
        lblTopo.getStyleClass().add("header-subtitle");

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        HBox header = new HBox(logo, espacador, lblTopo);
        header.getStyleClass().add("header-bar");

        // ------------------ Centro (header + abas) ------------------
        VBox conteudoCentral = new VBox(header, tabPane);
        conteudoCentral.getStyleClass().add("content-root");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(conteudoCentral);

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/view/dark-theme.css").toExternalForm());

        stage.setTitle("GymSolver - Painel da Academia");
        stage.setScene(scene);
        stage.show();
    }

    private ToggleButton criarBotaoMenu(String texto, ToggleGroup grupo) {
        ToggleButton btn = new ToggleButton(texto);
        btn.setToggleGroup(grupo);
        btn.getStyleClass().add("sidebar-button");
        return btn;
    }

    // ======================= ABA CLIENTES =======================
    private Tab criarTabClientes() {
        Tab tab = new Tab("Clientes");
        tab.setClosable(false);

        // Lista filtrada para buscar clientes
        FilteredList<Cliente> clientesFiltrados =
                new FilteredList<>(repositorio.getClientes(), c -> true);

        // Tabela
        TableView<Cliente> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(clientesFiltrados);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Cliente, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Cliente, PlanoAssinatura> colPlanoAssinatura = new TableColumn<>("Plano Assinatura");
        colPlanoAssinatura.setCellValueFactory(new PropertyValueFactory<>("planoAssinatura"));

        TableColumn<Cliente, PlanoTreino> colPlanoTreino = new TableColumn<>("Plano Treino");
        colPlanoTreino.setCellValueFactory(new PropertyValueFactory<>("planoTreino"));

        tabela.getColumns().addAll(
                colId, colNome, colEmail, colTelefone, colPlanoAssinatura, colPlanoTreino
        );

        // ----------------- Barra de busca -----------------
        TextField txtBusca = new TextField();
        txtBusca.setPromptText("Buscar cliente por nome ou e-mail...");

        txtBusca.textProperty().addListener((obs, old, novo) -> {
            String filtro = novo == null ? "" : novo.trim().toLowerCase();

            clientesFiltrados.setPredicate(cliente -> {
                if (filtro.isEmpty()) return true;

                String nome = cliente.getNome() == null ? "" : cliente.getNome().toLowerCase();
                String email = cliente.getEmail() == null ? "" : cliente.getEmail().toLowerCase();

                return nome.contains(filtro) || email.contains(filtro);
            });
        });

        Label lblBusca = new Label("Buscar:");
        HBox barraBusca = new HBox(8, lblBusca, txtBusca);
        barraBusca.setAlignment(Pos.CENTER_LEFT);
        barraBusca.setPadding(new Insets(0, 0, 8, 0));

        // ----------------- Formulário -----------------
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do cliente");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("E-mail do cliente");

        TextField txtTelefone = new TextField();
        txtTelefone.setPromptText("Telefone do cliente");

        ComboBox<PlanoAssinatura> cbPlanoAssinatura =
                new ComboBox<>(repositorio.getPlanosAssinatura());
        cbPlanoAssinatura.setPromptText("Selecione o plano de assinatura");

        ComboBox<PlanoTreino> cbPlanoTreino =
                new ComboBox<>(repositorio.getPlanosTreino());
        cbPlanoTreino.setPromptText("Selecione o plano de treino");

        GridPane formulario = new GridPane();
        formulario.setHgap(12);
        formulario.setVgap(8);

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

        formulario.getStyleClass().add("card");

        // ----------------- Botões -----------------
        Button btnNovo = new Button("Novo");
        btnNovo.getStyleClass().add("button-ghost");

        Button btnSalvar = new Button("Salvar");
        btnSalvar.getStyleClass().add("button-primary");

        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("button-danger");
        btnExcluir.setDisable(true);

        Button btnLimpar = new Button("Limpar");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        botoes.setPadding(new Insets(12, 0, 0, 0));

        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        VBox conteudo = new VBox(16, barraBusca, tabela, cardFormulario);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        // ----------------- Comportamento -----------------

        // Quando clicar em uma linha da tabela, preenche o formulário
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                txtId.setText(String.valueOf(selecionado.getId()));
                txtNome.setText(selecionado.getNome());
                txtEmail.setText(selecionado.getEmail());
                txtTelefone.setText(selecionado.getTelefone());
                cbPlanoAssinatura.setValue(selecionado.getPlanoAssinatura());
                cbPlanoTreino.setValue(selecionado.getPlanoTreino());

                btnSalvar.setText("Atualizar");
                btnExcluir.setDisable(false);
            }
        });

        // Botão NOVO: limpa e prepara para cadastro novo
        btnNovo.setOnAction(e -> {
            tabela.getSelectionModel().clearSelection();
            limparFormularioCliente(txtId, txtNome, txtEmail, txtTelefone, cbPlanoAssinatura, cbPlanoTreino);
            btnSalvar.setText("Salvar");
            btnExcluir.setDisable(true);
        });

        // Botão LIMPAR: igual o Novo, mas sem mudar seleção pela lógica de negócio
        btnLimpar.setOnAction(e -> {
            tabela.getSelectionModel().clearSelection();
            limparFormularioCliente(txtId, txtNome, txtEmail, txtTelefone, cbPlanoAssinatura, cbPlanoTreino);
            btnSalvar.setText("Salvar");
            btnExcluir.setDisable(true);
        });

        // Botão SALVAR / ATUALIZAR
        btnSalvar.setOnAction(e -> {
            if (!validarCamposCliente(txtNome, txtEmail, txtTelefone, cbPlanoAssinatura, cbPlanoTreino)) {
                return;
            }

            Cliente selecionado = tabela.getSelectionModel().getSelectedItem();

            if (selecionado == null) {
                // Novo cliente
                repositorio.criarCliente(
                        txtNome.getText().trim(),
                        txtEmail.getText().trim(),
                        txtTelefone.getText().trim(),
                        cbPlanoAssinatura.getValue(),
                        cbPlanoTreino.getValue()
                );
            } else {
                // Atualizar cliente existente
                selecionado.setNome(txtNome.getText().trim());
                selecionado.setEmail(txtEmail.getText().trim());
                selecionado.setTelefone(txtTelefone.getText().trim());
                selecionado.setPlanoAssinatura(cbPlanoAssinatura.getValue());
                selecionado.setPlanoTreino(cbPlanoTreino.getValue());
                tabela.refresh();
            }

            tabela.getSelectionModel().clearSelection();
            limparFormularioCliente(txtId, txtNome, txtEmail, txtTelefone, cbPlanoAssinatura, cbPlanoTreino);
            btnSalvar.setText("Salvar");
            btnExcluir.setDisable(true);
        });

        // Botão EXCLUIR
        btnExcluir.setOnAction(e -> {
            Cliente selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir cliente");
            alert.setHeaderText("Tem certeza que deseja excluir este cliente?");
            alert.setContentText(selecionado.getNome());

            alert.showAndWait().ifPresent(resposta -> {
                if (resposta == ButtonType.OK) {
                    repositorio.getClientes().remove(selecionado);
                    tabela.getSelectionModel().clearSelection();
                    limparFormularioCliente(txtId, txtNome, txtEmail, txtTelefone, cbPlanoAssinatura, cbPlanoTreino);
                    btnSalvar.setText("Salvar");
                    btnExcluir.setDisable(true);
                }
            });
        });

        tab.setContent(conteudo);
        return tab;
    }


    // ======================= ABA FUNCIONÁRIOS =======================
    private Tab criarTabFuncionarios() {
        Tab tab = new Tab("Funcionários");
        tab.setClosable(false);

        // Lista filtrada para busca
        FilteredList<Funcionario> funcionariosFiltrados =
                new FilteredList<>(repositorio.getFuncionarios(), f -> true);

        // Tabela
        TableView<Funcionario> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(funcionariosFiltrados);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Funcionario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Funcionario, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Funcionario, String> colCargo = new TableColumn<>("Cargo");
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        TableColumn<Funcionario, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        tabela.getColumns().addAll(colId, colNome, colCargo, colCpf);

        // ---------- Barra de busca ----------
        TextField txtBusca = new TextField();
        txtBusca.setPromptText("Buscar funcionário por nome ou CPF...");

        txtBusca.textProperty().addListener((obs, old, novo) -> {
            String filtro = novo == null ? "" : novo.trim().toLowerCase();

            funcionariosFiltrados.setPredicate(func -> {
                if (filtro.isEmpty()) return true;

                String nome = func.getNome() == null ? "" : func.getNome().toLowerCase();
                String cpf = func.getCpf() == null ? "" : func.getCpf().toLowerCase();

                return nome.contains(filtro) || cpf.contains(filtro);
            });
        });

        Label lblBusca = new Label("Buscar:");
        HBox barraBusca = new HBox(8, lblBusca, txtBusca);
        barraBusca.setAlignment(Pos.CENTER_LEFT);
        barraBusca.setPadding(new Insets(0, 0, 8, 0));

        // ---------- Formulário ----------
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do funcionário");

        TextField txtCargo = new TextField();
        txtCargo.setPromptText("Cargo (instrutor, recepção, etc.)");

        TextField txtCpf = new TextField();
        txtCpf.setPromptText("CPF do funcionário");

        GridPane formulario = new GridPane();
        formulario.setHgap(12);
        formulario.setVgap(8);

        formulario.add(new Label("ID:"), 0, 0);
        formulario.add(txtId, 1, 0);

        formulario.add(new Label("Nome:"), 0, 1);
        formulario.add(txtNome, 1, 1);

        formulario.add(new Label("Cargo:"), 0, 2);
        formulario.add(txtCargo, 1, 2);

        formulario.add(new Label("CPF:"), 0, 3);
        formulario.add(txtCpf, 1, 3);

        formulario.getStyleClass().add("card");

        // ---------- Botões ----------
        Button btnNovo = new Button("Novo");
        btnNovo.getStyleClass().add("button-ghost");

        Button btnSalvar = new Button("Salvar");
        btnSalvar.getStyleClass().add("button-primary");

        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("button-danger");
        btnExcluir.setDisable(true);

        Button btnLimpar = new Button("Limpar");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        botoes.setPadding(new Insets(12, 0, 0, 0));

        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        VBox conteudo = new VBox(16, barraBusca, tabela, cardFormulario);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        // ---------- Comportamento ----------
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                txtId.setText(String.valueOf(selecionado.getId()));
                txtNome.setText(selecionado.getNome());
                txtCargo.setText(selecionado.getCargo());
                txtCpf.setText(selecionado.getCpf());

                btnSalvar.setText("Atualizar");
                btnExcluir.setDisable(false);
            }
        });

        btnNovo.setOnAction(e -> {
            tabela.getSelectionModel().clearSelection();
            txtId.clear();
            txtNome.clear();
            txtCargo.clear();
            txtCpf.clear();
            btnSalvar.setText("Salvar");
            btnExcluir.setDisable(true);
        });

        btnLimpar.setOnAction(e -> {
            tabela.getSelectionModel().clearSelection();
            txtId.clear();
            txtNome.clear();
            txtCargo.clear();
            txtCpf.clear();
            btnSalvar.setText("Salvar");
            btnExcluir.setDisable(true);
        });

        btnSalvar.setOnAction(e -> {
            Funcionario selecionado = tabela.getSelectionModel().getSelectedItem();

            if (selecionado == null) {
                // Novo
                repositorio.criarFuncionario(
                        txtNome.getText().trim(),
                        txtCargo.getText().trim(),
                        txtCpf.getText().trim()
                );
            } else {
                // Atualizar
                selecionado.setNome(txtNome.getText().trim());
                selecionado.setCargo(txtCargo.getText().trim());
                selecionado.setCpf(txtCpf.getText().trim());
                tabela.refresh();
            }

            tabela.getSelectionModel().clearSelection();
            txtId.clear();
            txtNome.clear();
            txtCargo.clear();
            txtCpf.clear();
            btnSalvar.setText("Salvar");
            btnExcluir.setDisable(true);
        });

        btnExcluir.setOnAction(e -> {
            Funcionario selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) return;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir funcionário");
            alert.setHeaderText("Tem certeza que deseja excluir este funcionário?");
            alert.setContentText(selecionado.getNome());

            alert.showAndWait().ifPresent(resposta -> {
                if (resposta == ButtonType.OK) {
                    repositorio.getFuncionarios().remove(selecionado);
                    tabela.getSelectionModel().clearSelection();
                    txtId.clear();
                    txtNome.clear();
                    txtCargo.clear();
                    txtCpf.clear();
                    btnSalvar.setText("Salvar");
                    btnExcluir.setDisable(true);
                }
            });
        });

        tab.setContent(conteudo);
        return tab;
    }


    // ------------------------------------------------------------
    //  ABA PLANOS DE TREINO
    // ------------------------------------------------------------
    // ======================= ABA PLANOS DE TREINO =======================
    private Tab criarTabPlanosTreino() {
        Tab tab = new Tab("Planos de Treino");
        tab.setClosable(false);

        // Lista filtrada para busca
        FilteredList<PlanoTreino> planosFiltrados =
                new FilteredList<>(repositorio.getPlanosTreino(), p -> true);

        // ---------- Tabela ----------
        TableView<PlanoTreino> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(planosFiltrados);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PlanoTreino, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PlanoTreino, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<PlanoTreino, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<PlanoTreino, String> colObjetivo = new TableColumn<>("Objetivo");
        colObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));

        tabela.getColumns().addAll(colId, colNome, colDescricao, colObjetivo);

        // ---------- BARRA DE BUSCA ----------
        TextField txtBusca = new TextField();
        txtBusca.setPromptText("Buscar plano de treino por nome ou objetivo...");

        txtBusca.textProperty().addListener((obs, old, novo) -> {
            String filtro = (novo == null) ? "" : novo.trim().toLowerCase();

            planosFiltrados.setPredicate(plano -> {
                if (filtro.isEmpty()) return true;

                String nome = plano.getNome() == null ? "" : plano.getNome().toLowerCase();
                String objetivo = plano.getObjetivo() == null ? "" : plano.getObjetivo().toLowerCase();

                return nome.contains(filtro) || objetivo.contains(filtro);
            });
        });

        Label lblBusca = new Label("Buscar:");
        HBox barraBusca = new HBox(8, lblBusca, txtBusca);
        barraBusca.setAlignment(Pos.CENTER_LEFT);
        barraBusca.setPadding(new Insets(0, 0, 8, 0));

        // ---------- FORMULÁRIO ----------
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do plano de treino");

        TextField txtDescricao = new TextField();
        txtDescricao.setPromptText("Descrição do plano (ex.: divisão de treinos, volume, etc.)");

        TextField txtObjetivo = new TextField();
        txtObjetivo.setPromptText("Objetivo (hipertrofia, emagrecimento, etc.)");

        GridPane formulario = new GridPane();
        formulario.setHgap(12);
        formulario.setVgap(8);

        formulario.add(new Label("ID:"), 0, 0);
        formulario.add(txtId, 1, 0);

        formulario.add(new Label("Nome:"), 0, 1);
        formulario.add(txtNome, 1, 1);

        formulario.add(new Label("Descrição:"), 0, 2);
        formulario.add(txtDescricao, 1, 2);

        formulario.add(new Label("Objetivo:"), 0, 3);
        formulario.add(txtObjetivo, 1, 3);

        // ---------- BOTÕES ----------
        Button btnNovo = new Button("Novo");
        btnNovo.getStyleClass().add("button-ghost");

        Button btnSalvar = new Button("Salvar");
        btnSalvar.getStyleClass().add("button-primary");

        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("button-danger");

        Button btnLimpar = new Button("Limpar");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        botoes.setPadding(new Insets(12, 0, 0, 0));

        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        VBox conteudo = new VBox(16, barraBusca, tabela, cardFormulario);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        tab.setContent(conteudo);
        return tab;
    }


    // ------------------------------------------------------------
    //  ABA PLANOS DE ASSINATURA
    // ------------------------------------------------------------
    // ======================= ABA PLANOS DE ASSINATURA =======================
    private Tab criarTabPlanosAssinatura() {
        Tab tab = new Tab("Planos de Assinatura");
        tab.setClosable(false);

        // Lista filtrada para busca
        FilteredList<PlanoAssinatura> assinaturasFiltradas =
                new FilteredList<>(repositorio.getPlanosAssinatura(), p -> true);

        // ---------- Tabela ----------
        TableView<PlanoAssinatura> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(assinaturasFiltradas);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PlanoAssinatura, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PlanoAssinatura, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<PlanoAssinatura, Double> colValorMensal = new TableColumn<>("Valor mensal (R$)");
        colValorMensal.setCellValueFactory(new PropertyValueFactory<>("valorMensal"));

        TableColumn<PlanoAssinatura, Integer> colDuracao = new TableColumn<>("Duração (meses)");
        colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracaoMeses"));

        tabela.getColumns().addAll(colId, colNome, colValorMensal, colDuracao);

        // ---------- BARRA DE BUSCA ----------
        TextField txtBusca = new TextField();
        txtBusca.setPromptText("Buscar plano por nome, valor ou duração...");

        txtBusca.textProperty().addListener((obs, old, novo) -> {
            String filtro = (novo == null) ? "" : novo.trim().toLowerCase();

            assinaturasFiltradas.setPredicate(plano -> {
                if (filtro.isEmpty()) return true;

                String nome = plano.getNome() == null ? "" : plano.getNome().toLowerCase();
                String valor = String.valueOf(plano.getValorMensal()).toLowerCase();
                String duracao = String.valueOf(plano.getDuracaoMeses()).toLowerCase();

                return nome.contains(filtro) || valor.contains(filtro) || duracao.contains(filtro);
            });
        });

        Label lblBusca = new Label("Buscar:");
        HBox barraBusca = new HBox(8, lblBusca, txtBusca);
        barraBusca.setAlignment(Pos.CENTER_LEFT);
        barraBusca.setPadding(new Insets(0, 0, 8, 0));

        // ---------- FORMULÁRIO ----------
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do plano");

        TextField txtValorMensal = new TextField();
        txtValorMensal.setPromptText("Valor mensal (R$)");

        TextField txtDuracao = new TextField();
        txtDuracao.setPromptText("Duração em meses");

        GridPane formulario = new GridPane();
        formulario.setHgap(12);
        formulario.setVgap(8);

        formulario.add(new Label("ID:"), 0, 0);
        formulario.add(txtId, 1, 0);

        formulario.add(new Label("Nome:"), 0, 1);
        formulario.add(txtNome, 1, 1);

        formulario.add(new Label("Valor mensal:"), 0, 2);
        formulario.add(txtValorMensal, 1, 2);

        formulario.add(new Label("Duração (meses):"), 0, 3);
        formulario.add(txtDuracao, 1, 3);

        // ---------- BOTÕES ----------
        Button btnNovo = new Button("Novo");
        btnNovo.getStyleClass().add("button-ghost");

        Button btnSalvar = new Button("Salvar");
        btnSalvar.getStyleClass().add("button-primary");

        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("button-danger");

        Button btnLimpar = new Button("Limpar");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        botoes.setPadding(new Insets(12, 0, 0, 0));

        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        VBox conteudo = new VBox(16, barraBusca, tabela, cardFormulario);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        tab.setContent(conteudo);
        return tab;
    }


    // ------------------------------------------------------------
    //  ABA SOBRE (Dashboard + descrição)
    // ------------------------------------------------------------
    private Tab criarTabSobre() {
        Tab tab = new Tab("Sobre");
        tab.setClosable(false);

        // ---------- Cards do "dashboard" ----------
        Label lblClientesValor = new Label();
        lblClientesValor.getStyleClass().add("metric-value");
        lblClientesValor.textProperty().bind(
                Bindings.size(repositorio.getClientes()).asString()
        );
        Label lblClientesTitulo = new Label("Clientes cadastrados");
        lblClientesTitulo.getStyleClass().add("metric-label");
        VBox cardClientes = new VBox(4, lblClientesValor, lblClientesTitulo);
        cardClientes.getStyleClass().addAll("card", "dashboard-card");

        Label lblFuncValor = new Label();
        lblFuncValor.getStyleClass().add("metric-value");
        lblFuncValor.textProperty().bind(
                Bindings.size(repositorio.getFuncionarios()).asString()
        );
        Label lblFuncTitulo = new Label("Funcionários");
        lblFuncTitulo.getStyleClass().add("metric-label");
        VBox cardFuncionarios = new VBox(4, lblFuncValor, lblFuncTitulo);
        cardFuncionarios.getStyleClass().addAll("card", "dashboard-card");

        Label lblTreinoValor = new Label();
        lblTreinoValor.getStyleClass().add("metric-value");
        lblTreinoValor.textProperty().bind(
                Bindings.size(repositorio.getPlanosTreino()).asString()
        );
        Label lblTreinoTitulo = new Label("Planos de treino");
        lblTreinoTitulo.getStyleClass().add("metric-label");
        VBox cardTreino = new VBox(4, lblTreinoValor, lblTreinoTitulo);
        cardTreino.getStyleClass().addAll("card", "dashboard-card");

        Label lblAssinValor = new Label();
        lblAssinValor.getStyleClass().add("metric-value");
        lblAssinValor.textProperty().bind(
                Bindings.size(repositorio.getPlanosAssinatura()).asString()
        );
        Label lblAssinTitulo = new Label("Planos de assinatura");
        lblAssinTitulo.getStyleClass().add("metric-label");
        VBox cardAssin = new VBox(4, lblAssinValor, lblAssinTitulo);
        cardAssin.getStyleClass().addAll("card", "dashboard-card");

        HBox linhaCards = new HBox(16, cardClientes, cardFuncionarios, cardTreino, cardAssin);
        linhaCards.setAlignment(Pos.TOP_LEFT);

        // ---------- Texto de descrição ----------
        Label titulo = new Label("GymSolver - Sistema de Gerenciamento de Academia");
        titulo.getStyleClass().add("sobre-title");


        Label p1 = new Label("Sistema voltado para a administração de academias, permitindo:");
        p1.getStyleClass().add("sobre-text");

        Label p2 = new Label("- Controle de planos de treino e assinatura;\n" +
                            "- Cadastro e acompanhamento de clientes;\n" +
                            "- Gestão de funcionários."
        );
        p2.getStyleClass().add("sobre-text");

        Label p3 = new Label("Este é um protótipo acadêmico com dados armazenados em memória (sem banco de dados).");
        p3.getStyleClass().add("sobre-text");

        VBox texto = new VBox(6, titulo, p1, p2, p3);

        VBox cardSobre = new VBox(16, linhaCards, texto);
        cardSobre.getStyleClass().add("card");

        VBox root = new VBox(16, cardSobre);
        root.setPadding(new Insets(24));

        tab.setContent(root);
        return tab;
    }

    // ======================= Helpers Clientes =======================

// Validação dos campos de cliente
    private boolean validarCamposCliente(
            TextField txtNome,
            TextField txtEmail,
            TextField txtTelefone,
            ComboBox<PlanoAssinatura> cbPlanoAssinatura,
            ComboBox<PlanoTreino> cbPlanoTreino) {

        StringBuilder erros = new StringBuilder();

        if (txtNome.getText().trim().isEmpty()) {
            erros.append("- Informe o nome do cliente.\n");
        }

        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            erros.append("- Informe o e-mail do cliente.\n");
        } else if (!email.contains("@")) {
            erros.append("- Informe um e-mail válido.\n");
        }

        if (txtTelefone.getText().trim().isEmpty()) {
            erros.append("- Informe o telefone do cliente.\n");
        }

        if (cbPlanoAssinatura.getValue() == null) {
            erros.append("- Selecione um plano de assinatura.\n");
        }

        if (cbPlanoTreino.getValue() == null) {
            erros.append("- Selecione um plano de treino.\n");
        }

        if (erros.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Dados inválidos");
            alert.setHeaderText("Não foi possível salvar o cliente.");
            alert.setContentText(erros.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }

    // Limpar formulário de cliente
    private void limparFormularioCliente(
            TextField txtId,
            TextField txtNome,
            TextField txtEmail,
            TextField txtTelefone,
            ComboBox<PlanoAssinatura> cbPlanoAssinatura,
            ComboBox<PlanoTreino> cbPlanoTreino) {

        txtId.clear();
        txtNome.clear();
        txtEmail.clear();
        txtTelefone.clear();
        cbPlanoAssinatura.getSelectionModel().clearSelection();
        cbPlanoTreino.getSelectionModel().clearSelection();
    }

}
