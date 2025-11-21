package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * "Banco de dados" em memória da academia.
 * Guarda listas de clientes, funcionários, planos de treino e planos de assinatura.
 */
public class AcademiaRepository {

    private final ObservableList<Cliente> clientes =
            FXCollections.observableArrayList();

    private final ObservableList<Funcionario> funcionarios =
            FXCollections.observableArrayList();

    private final ObservableList<PlanoTreino> planosTreino =
            FXCollections.observableArrayList();

    private final ObservableList<PlanoAssinatura> planosAssinatura =
            FXCollections.observableArrayList();

    public AcademiaRepository() {
        // ---- Planos de assinatura de exemplo ----
        PlanoAssinatura mensal = new PlanoAssinatura(1, "Mensal", 99.90, 1);
        PlanoAssinatura trimestral = new PlanoAssinatura(2, "Trimestral", 249.90, 3);

        planosAssinatura.addAll(mensal, trimestral);

        // ---- Planos de treino de exemplo ----
        PlanoTreino iniciante = new PlanoTreino(
                1,
                "Iniciante",
                "Treino básico 3x por semana",
                "Adaptação"
        );

        PlanoTreino hipertrofia = new PlanoTreino(
                2,
                "Hipertrofia",
                "Treino pesado 5x por semana",
                "Hipertrofia"
        );

        planosTreino.addAll(iniciante, hipertrofia);

        // ---- Funcionário de exemplo ----
        funcionarios.add(new Funcionario(
                1,
                "João Instrutor",
                "Instrutor",
                "123.456.789-00"
        ));

        // ---- Cliente de exemplo (liga tudo) ----
        clientes.add(new Cliente(
                1,
                "Maria Aluna",
                "maria@exemplo.com",
                "(45) 99999-9999",
                mensal,
                iniciante
        ));
    }

    // ================== LISTAS (TableView / ComboBox) ==================

    public ObservableList<Cliente> getClientes() {
        return clientes;
    }

    public ObservableList<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public ObservableList<PlanoTreino> getPlanosTreino() {
        return planosTreino;
    }

    public ObservableList<PlanoAssinatura> getPlanosAssinatura() {
        return planosAssinatura;
    }

    // ================== PRÓXIMOS IDs ==================
    // Versão com "Id" (provavelmente o que o GymSolverApp usa)

    public int proximoIdCliente() {
        return clientes.stream()
                .mapToInt(Cliente::getId)
                .max()
                .orElse(0) + 1;
    }

    public int proximoIdFuncionario() {
        return funcionarios.stream()
                .mapToInt(Funcionario::getId)
                .max()
                .orElse(0) + 1;
    }

    public int proximoIdPlanoTreino() {
        return planosTreino.stream()
                .mapToInt(PlanoTreino::getId)
                .max()
                .orElse(0) + 1;
    }

    public int proximoIdPlanoAssinatura() {
        return planosAssinatura.stream()
                .mapToInt(PlanoAssinatura::getId)
                .max()
                .orElse(0) + 1;
    }

    // Versão com "l" (proximol...), só pra garantir compatibilidade
    // Se não estiver usando, não tem problema, mas assim nada quebra.

    public int proximolCliente() {
        return proximoIdCliente();
    }

    public int proximolFuncionario() {
        return proximoIdFuncionario();
    }

    public int proximolPlanoTreino() {
        return proximoIdPlanoTreino();
    }

    public int proximolPlanoAssinatura() {
        return proximoIdPlanoAssinatura();
    }

    // ================== CRUD SIMPLES ==================

    public void salvarCliente(Cliente c) {
        if (!clientes.contains(c)) {
            clientes.add(c);
        }
    }

    public void removerCliente(Cliente c) {
        clientes.remove(c);
    }

    public void salvarFuncionario(Funcionario f) {
        if (!funcionarios.contains(f)) {
            funcionarios.add(f);
        }
    }

    public void removerFuncionario(Funcionario f) {
        funcionarios.remove(f);
    }

    public void salvarPlanoTreino(PlanoTreino p) {
        if (!planosTreino.contains(p)) {
            planosTreino.add(p);
        }
    }

    public void removerPlanoTreino(PlanoTreino p) {
        planosTreino.remove(p);
    }

    public void salvarPlanoAssinatura(PlanoAssinatura p) {
        if (!planosAssinatura.contains(p)) {
            planosAssinatura.add(p);
        }
    }

    public void removerPlanoAssinatura(PlanoAssinatura p) {
        planosAssinatura.remove(p);
    }
}
