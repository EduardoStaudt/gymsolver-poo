package view;

import singleton.ConnectionFactory;
import controller.GymController;
import model.vo.ClienteVO;
import model.vo.FuncionarioVO;
import model.vo.PlanoAssinaturaVO;
import model.vo.PlanoTreinoVO;
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
 * GymSolverApp - View Principal
 * Camada de interface do usuário - Responsável apenas pela apresentação
 */
public class GymSolverApp extends Application {

    private static final String ADMIN_PASSWORD = "321";
    private GymController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // inicializa o banco (cria tabelas e usuário admin, se você fez isso no initDatabase)
        ConnectionFactory.initDatabase();
        this.controller = new GymController();
        mostrarTelaLogin(primaryStage);
    }

    // ==================== TELA DE LOGIN ====================
    private void mostrarTelaLogin(Stage stage) {
        Label titulo = new Label("GymSolver");
        titulo.getStyleClass().add("header-title");

        Label subtitulo = new Label("Painel de gerenciamento da academia");
        subtitulo.getStyleClass().add("header-subtitle");

        Label lblUsuario = new Label("Usuário");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("admin@gym.com");

        Label lblSenha = new Label("Senha");
        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("123");

        Button btnEntrar = new Button("Entrar");
        btnEntrar.getStyleClass().add("button-primary");

        btnEntrar.setOnAction(e -> {
            String email = txtUsuario.getText();
            String senha = txtSenha.getText();

            boolean autenticado = controller.autenticarUsuario(email, senha);

            if (autenticado) {
                mostrarTelaPrincipal(stage);
            } else {
                mostrarAlerta(AlertType.ERROR, "Login inválido",
                        "Usuário ou senha incorretos",
                        "Tente novamente com um usuário cadastrado.");
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
        scene.getStylesheets().add(getClass().getResource("modified-dark-theme.css").toExternalForm());

        stage.setTitle("GymSolver - Login");
        stage.setScene(scene);
        stage.show();
    }

    // ==================== TELA PRINCIPAL ====================
    private void mostrarTelaPrincipal(Stage stage) {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("main-tab-pane");

        Tab tabClientes = criarTabClientes();
        Tab tabFuncionarios = criarTabFuncionarios();
        Tab tabPlanosTreino = criarTabPlanosTreino();
        Tab tabPlanosAssinatura = criarTabPlanosAssinatura();
        Tab tabSobre = criarTabSobre();

        tabPane.getTabs().addAll(tabClientes, tabFuncionarios, tabPlanosTreino, tabPlanosAssinatura, tabSobre);

        // Sidebar
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

        VBox sidebar = new VBox(12, lblMenu, btnClientes, btnFuncionarios, btnPlanosTreino, btnPlanosAssinatura, btnSobre);
        sidebar.getStyleClass().add("sidebar");

        // Header
        Label logo = new Label("GymSolver");
        logo.getStyleClass().add("header-title");

        Label lblTopo = new Label("Painel de gerenciamento da academia");
        lblTopo.getStyleClass().add("header-subtitle");

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        HBox header = new HBox(logo, espacador, lblTopo);
        header.getStyleClass().add("header-bar");

        // Conteúdo Central
        VBox conteudoCentral = new VBox(header, tabPane);
        conteudoCentral.getStyleClass().add("content-root");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(conteudoCentral);

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("modified-dark-theme.css").toExternalForm());

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

    // ==================== ABA CLIENTES ====================
    private Tab criarTabClientes() {
        Tab tab = new Tab("Clientes");
        tab.setClosable(false);

        // Lista filtrada
        FilteredList<ClienteVO> clientesFiltrados = controller.getClientesFiltrados();

        // Tabela
        TableView<ClienteVO> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(clientesFiltrados);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ClienteVO, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<ClienteVO, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<ClienteVO, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<ClienteVO, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<ClienteVO, PlanoAssinaturaVO> colPlanoAssinatura = new TableColumn<>("Plano Assinatura");
        colPlanoAssinatura.setCellValueFactory(new PropertyValueFactory<>("planoAssinatura"));

        TableColumn<ClienteVO, PlanoTreinoVO> colPlanoTreino = new TableColumn<>("Plano Treino");
        colPlanoTreino.setCellValueFactory(new PropertyValueFactory<>("planoTreino"));

        tabela.getColumns().addAll(colId, colNome, colEmail, colTelefone, colPlanoAssinatura, colPlanoTreino);

        // Barra de busca
        TextField txtBusca = new TextField();
        txtBusca.setPromptText("Pesquise aqui...");

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

        // Formulário
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do cliente");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("E-mail do cliente");

        TextField txtTelefone = new TextField();
        txtTelefone.setPromptText("Telefone do cliente");

        ComboBox<PlanoAssinaturaVO> cbPlanoAssinatura = new ComboBox<>(controller.getPlanosAssinatura());
        ComboBox<PlanoTreinoVO> cbPlanoTreino = new ComboBox<>(controller.getPlanosTreino());

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

        // Botões
        Button btnSalvar = new Button("Salvar");
        btnSalvar.getStyleClass().add("button-primary");

        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("button-danger");
        btnExcluir.setDisable(true);

        Button btnLimpar = new Button("Limpar");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        botoes.setPadding(new Insets(12, 0, 0, 0));

        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        VBox conteudo = new VBox(16, barraBusca, tabela, cardFormulario);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        // Comportamento
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherFormularioCliente(selecionado, txtId, txtNome, txtEmail, txtTelefone, cbPlanoAssinatura, cbPlanoTreino);
                btnSalvar.setText("Atualizar");
                btnExcluir.setDisable(false);
            }
        });

        btnLimpar.setOnAction(e -> limparSelecaoCliente(tabela, txtId, txtNome, txtEmail, txtTelefone, cbPlanoAssinatura, cbPlanoTreino, btnSalvar, btnExcluir));

        btnSalvar.setOnAction(e -> salvarCliente(tabela, txtId, txtNome, txtEmail, txtTelefone, cbPlanoAssinatura, cbPlanoTreino, btnSalvar, btnExcluir));

        btnExcluir.setOnAction(e -> excluirCliente(tabela, txtId, txtNome, txtEmail, txtTelefone, cbPlanoAssinatura, cbPlanoTreino, btnSalvar, btnExcluir));

        tab.setContent(conteudo);
        return tab;
    }

    // ==================== ABA FUNCIONÁRIOS ====================
    private Tab criarTabFuncionarios() {
        Tab tab = new Tab("Funcionários");
        tab.setClosable(false);

        FilteredList<FuncionarioVO> funcionariosFiltrados = controller.getFuncionariosFiltrados();

        // Tabela
        TableView<FuncionarioVO> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(funcionariosFiltrados);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<FuncionarioVO, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<FuncionarioVO, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<FuncionarioVO, String> colCargo = new TableColumn<>("Cargo");
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        TableColumn<FuncionarioVO, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        tabela.getColumns().addAll(colId, colNome, colCargo, colCpf);

        // Barra de busca
        TextField txtBusca = new TextField();
        txtBusca.setPromptText("Pesquise aqui...");

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

        // Formulário
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

        // Botões
        Button btnSalvar = new Button("Salvar");
        btnSalvar.getStyleClass().add("button-primary");

        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("button-danger");
        btnExcluir.setDisable(true);

        Button btnLimpar = new Button("Limpar");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        botoes.setPadding(new Insets(12, 0, 0, 0));

        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        VBox conteudo = new VBox(16, barraBusca, tabela, cardFormulario);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        // Comportamento
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

        btnLimpar.setOnAction(e -> limparSelecaoFuncionario(tabela, txtId, txtNome, txtCargo, txtCpf, btnSalvar, btnExcluir));

        btnSalvar.setOnAction(e -> salvarFuncionario(tabela, txtId, txtNome, txtCargo, txtCpf, btnSalvar, btnExcluir));

        btnExcluir.setOnAction(e -> excluirFuncionario(tabela, txtId, txtNome, txtCargo, txtCpf, btnSalvar, btnExcluir));

        tab.setContent(conteudo);
        return tab;
    }

    // ==================== ABA PLANOS DE TREINO ====================
    private Tab criarTabPlanosTreino() {
        Tab tab = new Tab("Planos de Treino");
        tab.setClosable(false);

        FilteredList<PlanoTreinoVO> planosFiltrados = new FilteredList<>(controller.getPlanosTreino(), p -> true);

        // Tabela
        TableView<PlanoTreinoVO> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(planosFiltrados);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PlanoTreinoVO, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PlanoTreinoVO, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<PlanoTreinoVO, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<PlanoTreinoVO, String> colObjetivo = new TableColumn<>("Objetivo");
        colObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));

        tabela.getColumns().addAll(colId, colNome, colDescricao, colObjetivo);

        // Barra de busca
        TextField txtBusca = new TextField();
        txtBusca.setPromptText("Pesquise aqui...");

        txtBusca.textProperty().addListener((obs, old, novo) -> {
            String filtro = novo == null ? "" : novo.trim().toLowerCase();
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

        // Formulário
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do plano");

        TextArea txtDescricao = new TextArea();
        txtDescricao.setPromptText("Descrição do plano");
        txtDescricao.setPrefRowCount(3);

        TextField txtObjetivo = new TextField();
        txtObjetivo.setPromptText("Objetivo (hipertrofia, adaptação, etc.)");

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

        formulario.getStyleClass().add("card");

        // Botões
        Button btnSalvar = new Button("Salvar");
        btnSalvar.getStyleClass().add("button-primary");

        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("button-danger");
        btnExcluir.setDisable(true);

        Button btnLimpar = new Button("Limpar");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        botoes.setPadding(new Insets(12, 0, 0, 0));

        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        VBox conteudo = new VBox(16, barraBusca, tabela, cardFormulario);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        // Comportamento
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherFormularioPlanoTreino(selecionado, txtId, txtNome, txtDescricao, txtObjetivo);
                btnSalvar.setText("Atualizar");
                btnExcluir.setDisable(false);
            }
        });

        btnLimpar.setOnAction(e -> limparSelecaoPlanoTreino(tabela, txtId, txtNome, txtDescricao, txtObjetivo, btnSalvar, btnExcluir));

        btnSalvar.setOnAction(e -> {
            if (!solicitarSenhaAdmin()) return;
            PlanoTreinoVO selecionado = tabela.getSelectionModel().getSelectedItem();
            try {
                if (selecionado == null) {
                    boolean sucesso = controller.criarPlanoTreino(txtNome.getText().trim(), txtDescricao.getText().trim(), txtObjetivo.getText().trim());
                    if (sucesso) {
                        mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Plano de treino criado com sucesso", "");
                    } else {
                        mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível criar o plano de treino", "");
                    }
                } else {
                    controller.atualizarPlanoTreino(selecionado, txtNome.getText().trim(), txtDescricao.getText().trim(), txtObjetivo.getText().trim());
                    tabela.refresh();
                    mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Plano de treino atualizado com sucesso", "");
                }
                limparSelecaoPlanoTreino(tabela, txtId, txtNome, txtDescricao, txtObjetivo, btnSalvar, btnExcluir);
            } catch (IllegalArgumentException ex) {
                mostrarAlerta(AlertType.WARNING, "Dados inválidos", ex.getMessage(), "");
            }
        });

        btnExcluir.setOnAction(e -> {
            PlanoTreinoVO selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) return;
            if (!solicitarSenhaAdmin()) return;

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Excluir plano de treino");
            alert.setHeaderText("Tem certeza que deseja excluir este plano?");
            alert.setContentText(selecionado.getNome());

            alert.showAndWait().ifPresent(resposta -> {
                if (resposta == ButtonType.OK) {
                    boolean sucesso = controller.excluirPlanoTreino(selecionado);
                    if (sucesso) {
                        mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Plano de treino excluído com sucesso", "");
                        limparSelecaoPlanoTreino(tabela, txtId, txtNome, txtDescricao, txtObjetivo, btnSalvar, btnExcluir);
                    } else {
                        mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível excluir o plano de treino", "");
                    }
                }
            });
        });

        tab.setContent(conteudo);
        return tab;
    }

    // ==================== ABA PLANOS DE ASSINATURA ====================
    private Tab criarTabPlanosAssinatura() {
        Tab tab = new Tab("Planos de Assinatura");
        tab.setClosable(false);

        FilteredList<PlanoAssinaturaVO> assinaturasFiltradas = new FilteredList<>(controller.getPlanosAssinatura(), p -> true);

        // Tabela
        TableView<PlanoAssinaturaVO> tabela = new TableView<>();
        tabela.getStyleClass().add("data-table");
        tabela.setItems(assinaturasFiltradas);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PlanoAssinaturaVO, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PlanoAssinaturaVO, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<PlanoAssinaturaVO, Double> colValorMensal = new TableColumn<>("Valor mensal (R$)");
        colValorMensal.setCellValueFactory(new PropertyValueFactory<>("valorMensal"));

        TableColumn<PlanoAssinaturaVO, Integer> colDuracao = new TableColumn<>("Duração (meses)");
        colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracaoMeses"));

        tabela.getColumns().addAll(colId, colNome, colValorMensal, colDuracao);

        // Barra de busca
        TextField txtBusca = new TextField();
        txtBusca.setPromptText("Pesquise aqui...");

        txtBusca.textProperty().addListener((obs, old, novo) -> {
            String filtro = novo == null ? "" : novo.trim().toLowerCase();
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

        // Formulário
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do plano");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Valor mensal (R$)");

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
        formulario.add(txtValor, 1, 2);

        formulario.add(new Label("Duração (meses):"), 0, 3);
        formulario.add(txtDuracao, 1, 3);

        formulario.getStyleClass().add("card");

        // Botões
        Button btnSalvar = new Button("Salvar");
        btnSalvar.getStyleClass().add("button-primary");

        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("button-danger");
        btnExcluir.setDisable(true);

        Button btnLimpar = new Button("Limpar");
        btnLimpar.getStyleClass().add("button-ghost");

        HBox botoes = new HBox(12, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        botoes.setPadding(new Insets(12, 0, 0, 0));

        VBox cardFormulario = new VBox(12, formulario, botoes);
        cardFormulario.getStyleClass().add("card");

        VBox conteudo = new VBox(16, barraBusca, tabela, cardFormulario);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        // Comportamento
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherFormularioPlanoAssinatura(selecionado, txtId, txtNome, txtValor, txtDuracao);
                btnSalvar.setText("Atualizar");
                btnExcluir.setDisable(false);
            }
        });

        btnLimpar.setOnAction(e -> limparSelecaoPlanoAssinatura(tabela, txtId, txtNome, txtValor, txtDuracao, btnSalvar, btnExcluir));

        btnSalvar.setOnAction(e -> {
            if (!solicitarSenhaAdmin()) return;
            PlanoAssinaturaVO selecionado = tabela.getSelectionModel().getSelectedItem();
            try {
                double valor = Double.parseDouble(txtValor.getText().trim());
                int duracao = Integer.parseInt(txtDuracao.getText().trim());
                if (selecionado == null) {
                    boolean sucesso = controller.criarPlanoAssinatura(txtNome.getText().trim(), valor, duracao);
                    if (sucesso) {
                        mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Plano de assinatura criado com sucesso", "");
                    } else {
                        mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível criar o plano de assinatura", "");
                    }
                } else {
                    controller.atualizarPlanoAssinatura(selecionado, txtNome.getText().trim(), valor, duracao);
                    tabela.refresh();
                    mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Plano de assinatura atualizado com sucesso", "");
                }
                limparSelecaoPlanoAssinatura(tabela, txtId, txtNome, txtValor, txtDuracao, btnSalvar, btnExcluir);
            } catch (NumberFormatException ex) {
                mostrarAlerta(AlertType.WARNING, "Dados inválidos", "Valor mensal e duração devem ser números.", "");
            } catch (IllegalArgumentException ex) {
                mostrarAlerta(AlertType.WARNING, "Dados inválidos", ex.getMessage(), "");
            }
        });

        btnExcluir.setOnAction(e -> {
            PlanoAssinaturaVO selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) return;
            if (!solicitarSenhaAdmin()) return;

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Excluir plano de assinatura");
            alert.setHeaderText("Tem certeza que deseja excluir este plano?");
            alert.setContentText(selecionado.getNome());

            alert.showAndWait().ifPresent(resposta -> {
                if (resposta == ButtonType.OK) {
                    boolean sucesso = controller.excluirPlanoAssinatura(selecionado);
                    if (sucesso) {
                        mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Plano de assinatura excluído com sucesso", "");
                        limparSelecaoPlanoAssinatura(tabela, txtId, txtNome, txtValor, txtDuracao, btnSalvar, btnExcluir);
                    } else {
                        mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível excluir o plano de assinatura", "");
                    }
                }
            });
        });

        tab.setContent(conteudo);
        return tab;
    }

    // ==================== ABA SOBRE ====================
    private Tab criarTabSobre() {
        Tab tab = new Tab("Sobre");
        tab.setClosable(false);

        // Cards do dashboard
        Label lblClientesValor = new Label();
        lblClientesValor.getStyleClass().add("metric-value");
        lblClientesValor.textProperty().bind(Bindings.createStringBinding(
            () -> String.valueOf(controller.getTotalClientes())
        ));

        Label lblClientesTitulo = new Label("Clientes cadastrados");
        lblClientesTitulo.getStyleClass().add("metric-label");
        VBox cardClientes = new VBox(4, lblClientesValor, lblClientesTitulo);
        cardClientes.getStyleClass().addAll("card", "dashboard-card");

        Label lblFuncValor = new Label();
        lblFuncValor.getStyleClass().add("metric-value");
        lblFuncValor.textProperty().bind(Bindings.createStringBinding(
            () -> String.valueOf(controller.getTotalFuncionarios())
        ));

        Label lblFuncTitulo = new Label("Funcionários");
        lblFuncTitulo.getStyleClass().add("metric-label");
        VBox cardFuncionarios = new VBox(4, lblFuncValor, lblFuncTitulo);
        cardFuncionarios.getStyleClass().addAll("card", "dashboard-card");

        Label lblTreinoValor = new Label();
        lblTreinoValor.getStyleClass().add("metric-value");
        lblTreinoValor.textProperty().bind(Bindings.createStringBinding(
            () -> String.valueOf(controller.getTotalPlanosTreino())
        ));

        Label lblTreinoTitulo = new Label("Planos de treino");
        lblTreinoTitulo.getStyleClass().add("metric-label");
        VBox cardTreino = new VBox(4, lblTreinoValor, lblTreinoTitulo);
        cardTreino.getStyleClass().addAll("card", "dashboard-card");

        Label lblAssinValor = new Label();
        lblAssinValor.getStyleClass().add("metric-value");
        lblAssinValor.textProperty().bind(Bindings.createStringBinding(
            () -> String.valueOf(controller.getTotalPlanosAssinatura())
        ));

        Label lblAssinTitulo = new Label("Planos de assinatura");
        lblAssinTitulo.getStyleClass().add("metric-label");
        VBox cardAssin = new VBox(4, lblAssinValor, lblAssinTitulo);
        cardAssin.getStyleClass().addAll("card", "dashboard-card");

        HBox linhaCards = new HBox(16, cardClientes, cardFuncionarios, cardTreino, cardAssin);
        linhaCards.setAlignment(Pos.TOP_LEFT);

        // Texto de descrição
        Label titulo = new Label("GymSolver - Sistema de Gerenciamento de Academia");
        titulo.getStyleClass().add("sobre-title");

        Label p1 = new Label("Sistema voltado para a administração de academias, permitindo:");
        p1.getStyleClass().add("sobre-text");

        Label p2 = new Label("- Controle de planos de treino e assinatura;\n" +
                            "- Cadastro e acompanhamento de clientes;\n" +
                            "- Gestão de funcionários."
        );
        p2.getStyleClass().add("sobre-text");

        Label p3 = new Label(
            "Este é um protótipo acadêmico que utiliza JavaFX, arquitetura MVC e integração com banco de dados via JDBC (SQLite)."
        );
        p3.getStyleClass().add("sobre-text");

        VBox texto = new VBox(6, titulo, p1, p2, p3);

        VBox cardSobre = new VBox(16, linhaCards, texto);
        cardSobre.getStyleClass().add("card");

        VBox root = new VBox(16, cardSobre);
        root.setPadding(new Insets(24));

        tab.setContent(root);
        return tab;
    }

    // ==================== MÉTODOS AUXILIARES CLIENTES ====================
    private void preencherFormularioCliente(ClienteVO cliente, TextField txtId, TextField txtNome, 
                                            TextField txtEmail, TextField txtTelefone,
                                            ComboBox<PlanoAssinaturaVO> cbPlanoAssinatura, 
                                            ComboBox<PlanoTreinoVO> cbPlanoTreino) {
        txtId.setText(String.valueOf(cliente.getId()));
        txtNome.setText(cliente.getNome());
        txtEmail.setText(cliente.getEmail());
        txtTelefone.setText(cliente.getTelefone());
        cbPlanoAssinatura.setValue(cliente.getPlanoAssinatura());
        cbPlanoTreino.setValue(cliente.getPlanoTreino());
    }

    private void limparSelecaoCliente(TableView<ClienteVO> tabela, TextField txtId, TextField txtNome,
                                    TextField txtEmail, TextField txtTelefone,
                                    ComboBox<PlanoAssinaturaVO> cbPlanoAssinatura,
                                    ComboBox<PlanoTreinoVO> cbPlanoTreino,
                                    Button btnSalvar, Button btnExcluir) {
        tabela.getSelectionModel().clearSelection();
        limparFormularioCliente(txtId, txtNome, txtEmail, txtTelefone, cbPlanoAssinatura, cbPlanoTreino);
        btnSalvar.setText("Salvar");
        btnExcluir.setDisable(true);
    }

    private void limparFormularioCliente(TextField txtId, TextField txtNome, TextField txtEmail,
                                        TextField txtTelefone, ComboBox<PlanoAssinaturaVO> cbPlanoAssinatura,
                                        ComboBox<PlanoTreinoVO> cbPlanoTreino) {
        txtId.clear();
        txtNome.clear();
        txtEmail.clear();
        txtTelefone.clear();
        cbPlanoAssinatura.getSelectionModel().clearSelection();
        cbPlanoTreino.getSelectionModel().clearSelection();
    }

    private void salvarCliente(TableView<ClienteVO> tabela, TextField txtId, TextField txtNome, TextField txtEmail,
                            TextField txtTelefone, ComboBox<PlanoAssinaturaVO> cbPlanoAssinatura,
                            ComboBox<PlanoTreinoVO> cbPlanoTreino, Button btnSalvar, Button btnExcluir) {
        ClienteVO selecionado = tabela.getSelectionModel().getSelectedItem();

        if (!controller.validarCliente(txtNome.getText(), txtEmail.getText(), txtTelefone.getText(),
                                    cbPlanoAssinatura.getValue(), cbPlanoTreino.getValue())) {
            String erros = controller.getErrosValidacaoCliente(txtNome.getText(), txtEmail.getText(), txtTelefone.getText(),
                                                            cbPlanoAssinatura.getValue(), cbPlanoTreino.getValue());
            mostrarAlerta(AlertType.WARNING, "Dados inválidos", "Não foi possível salvar o cliente", erros);
            return;
        }

        if (selecionado == null) {
            // Novo cliente
            boolean sucesso = controller.criarCliente(txtNome.getText().trim(), txtEmail.getText().trim(),
                                                    txtTelefone.getText().trim(), cbPlanoAssinatura.getValue(),
                                                    cbPlanoTreino.getValue());
            if (sucesso) {
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Cliente criado com sucesso", "");
            } else {
                mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível criar o cliente", "");
            }
        } else {
            // Atualizar cliente
            controller.atualizarCliente(selecionado, txtNome.getText().trim(), txtEmail.getText().trim(),
                                        txtTelefone.getText().trim(), cbPlanoAssinatura.getValue(),
                                        cbPlanoTreino.getValue());
            tabela.refresh();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Cliente atualizado com sucesso", "");
        }

        limparSelecaoCliente(tabela, txtId, txtNome, txtEmail, txtTelefone,
                            cbPlanoAssinatura, cbPlanoTreino, btnSalvar, btnExcluir);
    }

    private void excluirCliente(TableView<ClienteVO> tabela, TextField txtId, TextField txtNome,
                                TextField txtEmail, TextField txtTelefone,
                                ComboBox<PlanoAssinaturaVO> cbPlanoAssinatura,
                                ComboBox<PlanoTreinoVO> cbPlanoTreino,
                                Button btnSalvar, Button btnExcluir) {
        ClienteVO selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) return;

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Excluir cliente");
        alert.setHeaderText("Tem certeza que deseja excluir este cliente?");
        alert.setContentText(selecionado.getNome());

        alert.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                boolean sucesso = controller.excluirCliente(selecionado);
                if (sucesso) {
                    mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Cliente excluído com sucesso", "");
                    limparSelecaoCliente(tabela, txtId, txtNome, txtEmail, txtTelefone, 
                                        cbPlanoAssinatura, cbPlanoTreino, btnSalvar, btnExcluir);
                } else {
                    mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível excluir o cliente", "");
                }
            }
        });
    }

    // ==================== MÉTODOS AUXILIARES FUNCIONÁRIOS ====================
    private void limparSelecaoFuncionario(TableView<FuncionarioVO> tabela, TextField txtId, TextField txtNome,
                                        TextField txtCargo, TextField txtCpf,
                                        Button btnSalvar, Button btnExcluir) {
        tabela.getSelectionModel().clearSelection();
        txtId.clear();
        txtNome.clear();
        txtCargo.clear();
        txtCpf.clear();
        btnSalvar.setText("Salvar");
        btnExcluir.setDisable(true);
    }

    private void salvarFuncionario(TableView<FuncionarioVO> tabela, TextField txtId, TextField txtNome,
                                TextField txtCargo, TextField txtCpf, Button btnSalvar, Button btnExcluir) {
        FuncionarioVO selecionado = tabela.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            // Novo funcionário
            boolean sucesso = controller.criarFuncionario(txtNome.getText().trim(), txtCargo.getText().trim(), txtCpf.getText().trim());
            if (sucesso) {
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Funcionário criado com sucesso", "");
            } else {
                mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível criar o funcionário", "");
            }
        } else {
            // Atualizar funcionário
            controller.atualizarFuncionario(selecionado, txtNome.getText().trim(), txtCargo.getText().trim(), txtCpf.getText().trim());
            tabela.refresh();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Funcionário atualizado com sucesso", "");
        }

        limparSelecaoFuncionario(tabela, txtId, txtNome, txtCargo, txtCpf, btnSalvar, btnExcluir);
    }

    private void excluirFuncionario(TableView<FuncionarioVO> tabela, TextField txtId, TextField txtNome,
                                    TextField txtCargo, TextField txtCpf,
                                    Button btnSalvar, Button btnExcluir) {
        FuncionarioVO selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) return;

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Excluir funcionário");
        alert.setHeaderText("Tem certeza que deseja excluir este funcionário?");
        alert.setContentText(selecionado.getNome());

        alert.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                boolean sucesso = controller.excluirFuncionario(selecionado);
                if (sucesso) {
                    mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Funcionário excluído com sucesso", "");
                    limparSelecaoFuncionario(tabela, txtId, txtNome, txtCargo, txtCpf, btnSalvar, btnExcluir);
                } else {
                    mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível excluir o funcionário", "");
                }
            }
        });
    }

    // ==================== MÉTODOS AUXILIARES PLANOS DE TREINO ====================
    private void preencherFormularioPlanoTreino(PlanoTreinoVO plano, TextField txtId, TextField txtNome,
                                                TextArea txtDescricao, TextField txtObjetivo) {
        txtId.setText(String.valueOf(plano.getId()));
        txtNome.setText(plano.getNome());
        txtDescricao.setText(plano.getDescricao());
        txtObjetivo.setText(plano.getObjetivo());
    }

    private void limparSelecaoPlanoTreino(TableView<PlanoTreinoVO> tabela, TextField txtId, TextField txtNome,
                                          TextArea txtDescricao, TextField txtObjetivo,
                                          Button btnSalvar, Button btnExcluir) {
        tabela.getSelectionModel().clearSelection();
        limparFormularioPlanoTreino(txtId, txtNome, txtDescricao, txtObjetivo);
        btnSalvar.setText("Salvar");
        btnExcluir.setDisable(true);
    }

    private void limparFormularioPlanoTreino(TextField txtId, TextField txtNome, TextArea txtDescricao,
                                             TextField txtObjetivo) {
        txtId.clear();
        txtNome.clear();
        txtDescricao.clear();
        txtObjetivo.clear();
    }

    // ==================== MÉTODOS AUXILIARES PLANOS DE ASSINATURA ====================
    private void preencherFormularioPlanoAssinatura(PlanoAssinaturaVO plano, TextField txtId, TextField txtNome,
                                                    TextField txtValor, TextField txtDuracao) {
        txtId.setText(String.valueOf(plano.getId()));
        txtNome.setText(plano.getNome());
        txtValor.setText(String.valueOf(plano.getValorMensal()));
        txtDuracao.setText(String.valueOf(plano.getDuracaoMeses()));
    }

    private void limparSelecaoPlanoAssinatura(TableView<PlanoAssinaturaVO> tabela, TextField txtId, TextField txtNome,
                                              TextField txtValor, TextField txtDuracao,
                                              Button btnSalvar, Button btnExcluir) {
        tabela.getSelectionModel().clearSelection();
        limparFormularioPlanoAssinatura(txtId, txtNome, txtValor, txtDuracao);
        btnSalvar.setText("Salvar");
        btnExcluir.setDisable(true);
    }

    private void limparFormularioPlanoAssinatura(TextField txtId, TextField txtNome,
                                                 TextField txtValor, TextField txtDuracao) {
        txtId.clear();
        txtNome.clear();
        txtValor.clear();
        txtDuracao.clear();
    }

    // ==================== MÉTODO AUXILIAR DE SENHA ADMIN ====================
    private boolean solicitarSenhaAdmin() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Autorização necessária");
        dialog.setHeaderText("Informe a senha de administrador para continuar");
        dialog.setContentText("Senha:");

        var resposta = dialog.showAndWait();
        if (resposta.isPresent() && ADMIN_PASSWORD.equals(resposta.get().trim())) {
            return true;
        }

        mostrarAlerta(AlertType.ERROR, "Acesso negado", "Senha incorreta ou operação cancelada", "");
        return false;
    }

    // ==================== MÉTODO AUXILIAR GERAL ====================
    private void mostrarAlerta(AlertType tipo, String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}
