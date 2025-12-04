package controller;

import model.bo.GymBO;
import model.vo.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.dao.UsuarioDAO;
import model.dao.UsuarioDAOImpl;
import java.sql.SQLException;

/**
 * Controller - Camada de interface entre View e Model
 */
public class GymController {
    private final GymBO gymBO;
    private final UsuarioDAO usuarioDAO;  // << novo

    public GymController() {
        this.gymBO = new GymBO();
        this.usuarioDAO = new UsuarioDAOImpl(); // << novo
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

    public boolean criarPlanoTreino(String nome, String descricao, String objetivo) {
        try {
            gymBO.criarPlanoTreino(nome, descricao, objetivo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void atualizarPlanoTreino(PlanoTreinoVO plano, String nome, String descricao, String objetivo) {
        gymBO.atualizarPlanoTreino(plano, nome, descricao, objetivo);
    }

    public boolean excluirPlanoTreino(PlanoTreinoVO plano) {
        try {
            gymBO.excluirPlanoTreino(plano);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean criarPlanoAssinatura(String nome, double valorMensal, int duracaoMeses) {
        try {
            gymBO.criarPlanoAssinatura(nome, valorMensal, duracaoMeses);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void atualizarPlanoAssinatura(PlanoAssinaturaVO plano, String nome, double valorMensal, int duracaoMeses) {
        gymBO.atualizarPlanoAssinatura(plano, nome, valorMensal, duracaoMeses);
    }

    public boolean excluirPlanoAssinatura(PlanoAssinaturaVO plano) {
        try {
            gymBO.excluirPlanoAssinatura(plano);
            return true;
        } catch (Exception e) {
            return false;
        }
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
        // ==================== LOGIN / USUÁRIOS ====================
    /**
     * Autentica um usuário usando o DAO.
     * Retorna true se existir um usuário com esse email/senha.
     */
    public boolean autenticarUsuario(String email, String senha) {
        try {
            var usuario = usuarioDAO.buscarPorEmailSenha(email, senha);
            return usuario != null;
        } catch (SQLException e) {
            // aqui você pode logar/mostrar mensagem se quiser
            e.printStackTrace();
            return false;
        }
    }
}
