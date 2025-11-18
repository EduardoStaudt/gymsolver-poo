package model;

// Essa classe é o "banco de dados" em memória da academia.
// Ela guarda listas de Clientes, Funcionários, Planos de Treino e Planos de Assinatura,
// e cuida também de gerar IDs automáticos.

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AcademiaRepository {

    // Listas observáveis (funcionam bem com TableView do JavaFX)
    private ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private ObservableList<Funcionario> funcionarios = FXCollections.observableArrayList();
    private ObservableList<PlanoTreino> planosTreino = FXCollections.observableArrayList();
    private ObservableList<PlanoAssinatura> planosAssinatura = FXCollections.observableArrayList();

    // Contadores simples de ID
    private int proximoIdCliente = 1;
    private int proximoIdFuncionario = 1;
    private int proximoIdPlanoTreino = 1;
    private int proximoIdPlanoAssinatura = 1;

    public AcademiaRepository() {
        // Aqui eu coloco alguns dados de exemplo, só pra não ficar tudo vazio
        PlanoAssinatura planoMensal = new PlanoAssinatura(
                gerarProximoIdPlanoAssinatura(),
                "Mensal",
                99.90,
                1
        );

        PlanoAssinatura planoTrimestral = new PlanoAssinatura(
                gerarProximoIdPlanoAssinatura(),
                "Trimestral",
                249.90,
                3
        );

        planosAssinatura.addAll(planoMensal, planoTrimestral);

        PlanoTreino treinoIniciante = new PlanoTreino(
                gerarProximoIdPlanoTreino(),
                "Iniciante",
                "Treino básico 3x por semana",
                "Adaptação"
        );

        PlanoTreino treinoHipertrofia = new PlanoTreino(
                gerarProximoIdPlanoTreino(),
                "Hipertrofia",
                "Treino pesado 5x por semana",
                "Hipertrofia"
        );

        planosTreino.addAll(treinoIniciante, treinoHipertrofia);

        Funcionario instrutor = new Funcionario(
                gerarProximoIdFuncionario(),
                "João Instrutor",
                "Instrutor",
                "111.222.333-44"
        );

        funcionarios.add(instrutor);

        Cliente clienteExemplo = new Cliente(
                gerarProximoIdCliente(),
                "Maria Aluna",
                "maria@exemplo.com",
                "(45) 99999-9999",
                planoMensal.getNome(),
                treinoIniciante.getNome()
        );

        clientes.add(clienteExemplo);
    }

    // ------------------------------
    // GETTERS das listas
    // ------------------------------

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

    // ------------------------------
    // Geração de IDs
    // ------------------------------

    public int gerarProximoIdCliente() {
        return proximoIdCliente++;
    }

    public int gerarProximoIdFuncionario() {
        return proximoIdFuncionario++;
    }

    public int gerarProximoIdPlanoTreino() {
        return proximoIdPlanoTreino++;
    }

    public int gerarProximoIdPlanoAssinatura() {
        return proximoIdPlanoAssinatura++;
    }

    // ------------------------------
    // Métodos de busca simples
    // ------------------------------

    public Cliente buscarClientePorId(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public Funcionario buscarFuncionarioPorId(int id) {
        for (Funcionario f : funcionarios) {
            if (f.getId() == id) {
                return f;
            }
        }
        return null;
    }

    public PlanoTreino buscarPlanoTreinoPorId(int id) {
        for (PlanoTreino p : planosTreino) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public PlanoAssinatura buscarPlanoAssinaturaPorId(int id) {
        for (PlanoAssinatura p : planosAssinatura) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}

