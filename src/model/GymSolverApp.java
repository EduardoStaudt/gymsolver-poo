package model;
// GymSolverApp.java
// Classe principal da aplicação JavaFX

// OBS: se você estiver usando packages, coloque algo como:
// package br.edu.utfpr.gymsolver;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        primaryStage.setTitle("GymSolver - Sistema de Gerenciamento de Academia");

        // TabPane será o container com as abas principais
        TabPane tabPane = new TabPane();

        tabPane.getTabs().add(criarTabClientes());
        tabPane.getTabs().add(criarTabFuncionarios());
        tabPane.getTabs().add(criarTabPlanosTreino());
        tabPane.getTabs().add(criarTabPlanosAssinatura());

        // Aba "Sobre" apenas para mostrar o que o sistema faz
        Tab tabSobre = new Tab("Sobre", criarPainelSobre());
        tabSobre.setClosable(false);
        tabPane.getTabs().add(tabSobre);

        Scene scene = new Scene(tabPane, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // -----------------------------------------------------------
    // ABA CLIENTES
    // -----------------------------------------------------------
    private Tab criarTabClientes() {
        Tab tab = new Tab("Clientes");
        tab.setClosable(false);

        // Tabela
        TableView<Cliente> tabela = new TableView<>();
        tabela.setItems(repositorio.getClientes());

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

        // Formulário
        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true); // não deixo o usuário digitar ID

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

        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        // Ao selecionar um cliente na tabela, preenchermos o formulário para edição
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

        // Botão Novo: apenas gera um novo ID e limpa os campos
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

        // Botão Salvar: se ID está vazio, gera um novo; se já existe, atualiza
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

            // se já existe, atualiza; senão, adiciona
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
                tabela.refresh(); // força atualização visual
            }
        });

        // Botão Excluir
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

        // Botão Limpar
        btnLimpar.setOnAction(e -> {
            limparCamposCliente(txtId, txtNome, txtEmail, txtTelefone,
                    cbPlanoAssinatura, cbPlanoTreino, tabela);
        });

        // Layout do formulário (grid simples)
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

        VBox vbox = new VBox(10, tabela, formulario, botoes);
        vbox.setPadding(new Insets(10));

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

        TableView<Funcionario> tabela = new TableView<>();
        tabela.setItems(repositorio.getFuncionarios());

        TableColumn<Funcionario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Funcionario, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Funcionario, String> colCargo = new TableColumn<>("Cargo");
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        TableColumn<Funcionario, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        tabela.getColumns().addAll(colId, colNome, colCargo, colCpf);

        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do funcionário");

        TextField txtCargo = new TextField();
        txtCargo.setPromptText("Cargo (instrutor, recepcionista...)");

        TextField txtCpf = new TextField();
        txtCpf.setPromptText("CPF");

        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, novoSel) -> {
            if (novoSel != null) {
                txtId.setText(String.valueOf(novoSel.getId()));
                txtNome.setText(novoSel.getNome());
                txtCargo.setText(novoSel.getCargo());
                txtCpf.setText(novoSel.getCpf());
            }
        });

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

            Funcionario existente = repositorio.buscarFuncionarioPorId(id);
            if (existente == null) {
                Funcionario novo = new Funcionario(id, nome, cargo, cpf);
                repositorio.getFuncionarios().add(novo);
            } else {
                existente.setNome(nome);
                existente.setCargo(cargo);
                existente.setCpf(cpf);
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

        VBox vbox = new VBox(10, tabela, formulario, botoes);
        vbox.setPadding(new Insets(10));

        tab.setContent(vbox);
        return tab;
    }

    // -----------------------------------------------------------
    // ABA PLANOS DE TREINO
    // -----------------------------------------------------------
    private Tab criarTabPlanosTreino() {
        Tab tab = new Tab("Planos de Treino");
        tab.setClosable(false);

        TableView<PlanoTreino> tabela = new TableView<>();
        tabela.setItems(repositorio.getPlanosTreino());

        TableColumn<PlanoTreino, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PlanoTreino, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<PlanoTreino, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<PlanoTreino, String> colObjetivo = new TableColumn<>("Objetivo");
        colObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));

        tabela.getColumns().addAll(colId, colNome, colDescricao, colObjetivo);

        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do plano de treino");

        TextField txtDescricao = new TextField();
        txtDescricao.setPromptText("Descrição resumida");

        TextField txtObjetivo = new TextField();
        txtObjetivo.setPromptText("Ex: Hipertrofia, Emagrecimento...");

        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, novoSel) -> {
            if (novoSel != null) {
                txtId.setText(String.valueOf(novoSel.getId()));
                txtNome.setText(novoSel.getNome());
                txtDescricao.setText(novoSel.getDescricao());
                txtObjetivo.setText(novoSel.getObjetivo());
            }
        });

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

            PlanoTreino existente = repositorio.buscarPlanoTreinoPorId(id);
            if (existente == null) {
                PlanoTreino novo = new PlanoTreino(id, nome, descricao, objetivo);
                repositorio.getPlanosTreino().add(novo);
            } else {
                existente.setNome(nome);
                existente.setDescricao(descricao);
                existente.setObjetivo(objetivo);
                tabela.refresh();
            }
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
        });

        btnLimpar.setOnAction(e -> {
            txtId.clear();
            txtNome.clear();
            txtDescricao.clear();
            txtObjetivo.clear();
            tabela.getSelectionModel().clearSelection();
        });

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

        VBox vbox = new VBox(10, tabela, formulario, botoes);
        vbox.setPadding(new Insets(10));

        tab.setContent(vbox);
        return tab;
    }

    // -----------------------------------------------------------
    // ABA PLANOS DE ASSINATURA
    // -----------------------------------------------------------
    private Tab criarTabPlanosAssinatura() {
        Tab tab = new Tab("Planos de Assinatura");
        tab.setClosable(false);

        TableView<PlanoAssinatura> tabela = new TableView<>();
        tabela.setItems(repositorio.getPlanosAssinatura());

        TableColumn<PlanoAssinatura, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PlanoAssinatura, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<PlanoAssinatura, Double> colValor = new TableColumn<>("Valor Mensal");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorMensal"));

        TableColumn<PlanoAssinatura, Integer> colDuracao = new TableColumn<>("Duração (meses)");
        colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracaoMeses"));

        tabela.getColumns().addAll(colId, colNome, colValor, colDuracao);

        TextField txtId = new TextField();
        txtId.setPromptText("ID (gerado automaticamente)");
        txtId.setDisable(true);

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do plano (Ex: Mensal, Trimestral)");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Valor mensal (R$)");

        TextField txtDuracao = new TextField();
        txtDuracao.setPromptText("Duração em meses");

        Button btnNovo = new Button("Novo");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, novoSel) -> {
            if (novoSel != null) {
                txtId.setText(String.valueOf(novoSel.getId()));
                txtNome.setText(novoSel.getNome());
                txtValor.setText(String.valueOf(novoSel.getValorMensal()));
                txtDuracao.setText(String.valueOf(novoSel.getDuracaoMeses()));
            }
        });

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
            } catch (Exception ex) {
                mostrarAlerta("Validação", "Valor mensal inválido.");
                return;
            }

            try {
                duracao = Integer.parseInt(txtDuracao.getText());
            } catch (Exception ex) {
                mostrarAlerta("Validação", "Duração em meses inválida.");
                return;
            }

            int id;
            if (txtId.getText().isBlank()) {
                id = repositorio.gerarProximoIdPlanoAssinatura();
                txtId.setText(String.valueOf(id));
            } else {
                id = Integer.parseInt(txtId.getText());
            }

            PlanoAssinatura existente = repositorio.buscarPlanoAssinaturaPorId(id);
            if (existente == null) {
                PlanoAssinatura novo = new PlanoAssinatura(id, nome, valor, duracao);
                repositorio.getPlanosAssinatura().add(novo);
            } else {
                existente.setNome(nome);
                existente.setValorMensal(valor);
                existente.setDuracaoMeses(duracao);
                tabela.refresh();
            }

            // Atualizar combobox na aba Clientes com os novos planos
            // (simples: só será atualizado ao abrir o sistema de novo ou podemos
            //  chamar atualizarComboPlanosAssinatura manualmente se passássemos a referência)
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
        });

        btnLimpar.setOnAction(e -> {
            txtId.clear();
            txtNome.clear();
            txtValor.clear();
            txtDuracao.clear();
            tabela.getSelectionModel().clearSelection();
        });

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(10));

        formulario.add(new Label("ID:"), 0, 0);
        formulario.add(txtId, 1, 0);

        formulario.add(new Label("Nome:"), 0, 1);
        formulario.add(txtNome, 1, 1);

        formulario.add(new Label("Valor Mensal:"), 0, 2);
        formulario.add(txtValor, 1, 2);

        formulario.add(new Label("Duração (meses):"), 0, 3);
        formulario.add(txtDuracao, 1, 3);

        HBox botoes = new HBox(10, btnNovo, btnSalvar, btnExcluir, btnLimpar);
        botoes.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(10, tabela, formulario, botoes);
        vbox.setPadding(new Insets(10));

        tab.setContent(vbox);
        return tab;
    }

    // -----------------------------------------------------------
    // ABA SOBRE
    // -----------------------------------------------------------
    private VBox criarPainelSobre() {
        Label titulo = new Label("GymSolver - Sistema de Gerenciamento de Academia");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label descricao = new Label(
                "Sistema voltado para a administração de academias, permitindo:\n" +
                "- Controle de planos de treino e assinatura;\n" +
                "- Cadastro e acompanhamento de clientes;\n" +
                "- Gestão de funcionários.\n\n" +
                "Este é um protótipo acadêmico, com dados armazenados em memória (sem banco de dados)."
        );
        descricao.setWrapText(true);

        VBox vbox = new VBox(10, titulo, descricao);
        vbox.setPadding(new Insets(20));
        return vbox;
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
