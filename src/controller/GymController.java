package controller;

import model.bo.GymBO;
import model.vo.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Controller - Camada de interface entre View e Model
 */
public class GymController {
    private final GymBO gymBO;
    
    public GymController() {
        this.gymBO = new GymBO();
    }
    
    // ==================== CLIENTES ====================
    public ObservableList<ClienteVO> getClientes() {
        return gymBO.getClientes();
    }
    
    public FilteredList<ClienteVO> getClientesFiltrados() {
        return gymBO.getClientesFiltrados();
    }
    
    public boolean criarCliente(String nome, String email, String telefone,
                              PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        try {
            gymBO.criarCliente(nome, email, telefone, planoAssinatura, planoTreino);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void atualizarCliente(ClienteVO cliente, String nome, String email, String telefone,
                               PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        gymBO.atualizarCliente(cliente, nome, email, telefone, planoAssinatura, planoTreino);
    }
    
    public boolean excluirCliente(ClienteVO cliente) {
        try {
            gymBO.excluirCliente(cliente);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // ==================== FUNCIONÁRIOS ====================
    public ObservableList<FuncionarioVO> getFuncionarios() {
        return gymBO.getFuncionarios();
    }
    
    public FilteredList<FuncionarioVO> getFuncionariosFiltrados() {
        return gymBO.getFuncionariosFiltrados();
    }
    
    public boolean criarFuncionario(String nome, String cargo, String cpf) {
        try {
            gymBO.criarFuncionario(nome, cargo, cpf);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void atualizarFuncionario(FuncionarioVO funcionario, String nome, String cargo, String cpf) {
        gymBO.atualizarFuncionario(funcionario, nome, cargo, cpf);
    }
    
    public boolean excluirFuncionario(FuncionarioVO funcionario) {
        try {
            gymBO.excluirFuncionario(funcionario);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // ==================== PLANOS ====================
    public ObservableList<PlanoTreinoVO> getPlanosTreino() {
        return gymBO.getPlanosTreino();
    }
    
    public ObservableList<PlanoAssinaturaVO> getPlanosAssinatura() {
        return gymBO.getPlanosAssinatura();
    }
    
    // ==================== VALIDAÇÕES ====================
    public boolean validarCliente(String nome, String email, String telefone,
                                PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        return gymBO.validarCliente(nome, email, telefone, planoAssinatura, planoTreino);
    }
    
    public String getErrosValidacaoCliente(String nome, String email, String telefone,
                                         PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        return gymBO.getErrosValidacaoCliente(nome, email, telefone, planoAssinatura, planoTreino);
    }
    
    // ==================== ESTATÍSTICAS ====================
    public int getTotalClientes() {
        return gymBO.getTotalClientes();
    }
    
    public int getTotalFuncionarios() {
        return gymBO.getTotalFuncionarios();
    }
    
    public int getTotalPlanosTreino() {
        return gymBO.getTotalPlanosTreino();
    }
    
    public int getTotalPlanosAssinatura() {
        return gymBO.getTotalPlanosAssinatura();
    }
}