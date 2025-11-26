package model.dao;

import model.vo.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * DAO Principal - Gerencia todo o acesso a dados
 */
public class AcademiaDAO {
    private final ObservableList<ClienteVO> clientes;
    private final ObservableList<FuncionarioVO> funcionarios;
    private final ObservableList<PlanoTreinoVO> planosTreino;
    private final ObservableList<PlanoAssinaturaVO> planosAssinatura;

    public AcademiaDAO() {
        this.clientes = FXCollections.observableArrayList();
        this.funcionarios = FXCollections.observableArrayList();
        this.planosTreino = FXCollections.observableArrayList();
        this.planosAssinatura = FXCollections.observableArrayList();
        
        carregarDadosIniciais();
    }

    private void carregarDadosIniciais() {
        // Planos de assinatura
        PlanoAssinaturaVO mensal = new PlanoAssinaturaVO(1, "Mensal", 99.90, 1);
        PlanoAssinaturaVO trimestral = new PlanoAssinaturaVO(2, "Trimestral", 249.90, 3);
        planosAssinatura.addAll(mensal, trimestral);

        // Planos de treino
        PlanoTreinoVO iniciante = new PlanoTreinoVO(1, "Iniciante", "Treino básico 3x por semana", "Adaptação");
        PlanoTreinoVO hipertrofia = new PlanoTreinoVO(2, "Hipertrofia", "Treino pesado 5x por semana", "Hipertrofia");
        planosTreino.addAll(iniciante, hipertrofia);

        // Funcionários
        funcionarios.add(new FuncionarioVO(1, "João Instrutor", "Instrutor", "123.456.789-00"));

        // Clientes
        clientes.add(new ClienteVO(1, "Maria Aluna", "maria@exemplo.com", "(45) 99999-9999", mensal, iniciante));
    }

    // ==================== GETTERS DAS LISTAS ====================
    public ObservableList<ClienteVO> getClientes() { return clientes; }
    public ObservableList<FuncionarioVO> getFuncionarios() { return funcionarios; }
    public ObservableList<PlanoTreinoVO> getPlanosTreino() { return planosTreino; }
    public ObservableList<PlanoAssinaturaVO> getPlanosAssinatura() { return planosAssinatura; }

    // ==================== OPERAÇÕES BÁSICAS ====================
    public void inserirCliente(ClienteVO cliente) {
        if (!clientes.contains(cliente)) {
            clientes.add(cliente);
        }
    }

    public void atualizarCliente(ClienteVO cliente) {
        // Atualização automática pela ObservableList
    }

    public void excluirCliente(ClienteVO cliente) {
        clientes.remove(cliente);
    }

    public void inserirFuncionario(FuncionarioVO funcionario) {
        if (!funcionarios.contains(funcionario)) {
            funcionarios.add(funcionario);
        }
    }

    public void excluirFuncionario(FuncionarioVO funcionario) {
        funcionarios.remove(funcionario);
    }

    public void inserirPlanoTreino(PlanoTreinoVO plano) {
        if (!planosTreino.contains(plano)) {
            planosTreino.add(plano);
        }
    }

    public void inserirPlanoAssinatura(PlanoAssinaturaVO plano) {
        if (!planosAssinatura.contains(plano)) {
            planosAssinatura.add(plano);
        }
    }

    // ==================== GERADORES DE ID ====================
    public int proximoIdCliente() {
        return clientes.stream().mapToInt(ClienteVO::getId).max().orElse(0) + 1;
    }

    public int proximoIdFuncionario() {
        return funcionarios.stream().mapToInt(FuncionarioVO::getId).max().orElse(0) + 1;
    }

    public int proximoIdPlanoTreino() {
        return planosTreino.stream().mapToInt(PlanoTreinoVO::getId).max().orElse(0) + 1;
    }

    public int proximoIdPlanoAssinatura() {
        return planosAssinatura.stream().mapToInt(PlanoAssinaturaVO::getId).max().orElse(0) + 1;
    }
}