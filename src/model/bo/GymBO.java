package model.bo;

import model.dao.*;
import model.vo.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Business Object Principal - Orquestra todos os BOs
 */
public class GymBO {
    private final AcademiaDAO academiaDAO;
    private final ClienteBO clienteBO;
    private final FuncionarioBO funcionarioBO;
    private final PlanoTreinoBO planoTreinoBO;
    private final PlanoAssinaturaBO planoAssinaturaBO;
    
    public GymBO() {
        this.academiaDAO = new AcademiaDAO();
        this.clienteBO = new ClienteBO(new ClienteDAO(academiaDAO));
        this.funcionarioBO = new FuncionarioBO(new FuncionarioDAO(academiaDAO));
        this.planoTreinoBO = new PlanoTreinoBO(academiaDAO);
        this.planoAssinaturaBO = new PlanoAssinaturaBO(academiaDAO);
    }
    
    // ==================== DELEGAÇÃO PARA BOs ESPECÍFICOS ====================
    
    // Clientes
    public ObservableList<ClienteVO> getClientes() {
        return clienteBO.listarTodos();
    }
    
    public FilteredList<ClienteVO> getClientesFiltrados() {
        return clienteBO.listarFiltrados();
    }
    
    public ClienteVO criarCliente(String nome, String email, String telefone,
                                PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        return clienteBO.criar(nome, email, telefone, planoAssinatura, planoTreino);
    }
    
    public void atualizarCliente(ClienteVO cliente, String nome, String email, String telefone,
                               PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        clienteBO.atualizar(cliente, nome, email, telefone, planoAssinatura, planoTreino);
    }
    
    public void excluirCliente(ClienteVO cliente) {
        clienteBO.excluir(cliente);
    }
    
    // Funcionários
    public ObservableList<FuncionarioVO> getFuncionarios() {
        return funcionarioBO.listarTodos();
    }
    
    public FilteredList<FuncionarioVO> getFuncionariosFiltrados() {
        return funcionarioBO.listarFiltrados();
    }
    
    public FuncionarioVO criarFuncionario(String nome, String cargo, String cpf) {
        return funcionarioBO.criar(nome, cargo, cpf);
    }
    
    public void atualizarFuncionario(FuncionarioVO funcionario, String nome, String cargo, String cpf) {
        funcionarioBO.atualizar(funcionario, nome, cargo, cpf);
    }
    
    public void excluirFuncionario(FuncionarioVO funcionario) {
        funcionarioBO.excluir(funcionario);
    }
    
    // Planos
    public ObservableList<PlanoTreinoVO> getPlanosTreino() {
        return planoTreinoBO.listarTodos();
    }
    
    public ObservableList<PlanoAssinaturaVO> getPlanosAssinatura() {
        return planoAssinaturaBO.listarTodos();
    }

    public PlanoTreinoVO criarPlanoTreino(String nome, String descricao, String objetivo) {
        return planoTreinoBO.criar(nome, descricao, objetivo);
    }

    public void atualizarPlanoTreino(PlanoTreinoVO plano, String nome, String descricao, String objetivo) {
        planoTreinoBO.atualizar(plano, nome, descricao, objetivo);
    }

    public void excluirPlanoTreino(PlanoTreinoVO plano) {
        planoTreinoBO.excluir(plano);
    }

    public PlanoAssinaturaVO criarPlanoAssinatura(String nome, double valorMensal, int duracaoMeses) {
        return planoAssinaturaBO.criar(nome, valorMensal, duracaoMeses);
    }

    public void atualizarPlanoAssinatura(PlanoAssinaturaVO plano, String nome, double valorMensal, int duracaoMeses) {
        planoAssinaturaBO.atualizar(plano, nome, valorMensal, duracaoMeses);
    }

    public void excluirPlanoAssinatura(PlanoAssinaturaVO plano) {
        planoAssinaturaBO.excluir(plano);
    }
    
    // ==================== VALIDAÇÕES ====================
    
    public boolean validarCliente(String nome, String email, String telefone,
                                PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        return clienteBO.validar(nome, email, telefone, planoAssinatura, planoTreino);
    }
    
    public String getErrosValidacaoCliente(String nome, String email, String telefone,
                                         PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        return clienteBO.getErrosValidacao(nome, email, telefone, planoAssinatura, planoTreino);
    }
    
    // ==================== ESTATÍSTICAS ====================
    
    public int getTotalClientes() {
        return clienteBO.total();
    }
    
    public int getTotalFuncionarios() {
        return funcionarioBO.total();
    }
    
    public int getTotalPlanosTreino() {
        return planoTreinoBO.total();
    }
    
    public int getTotalPlanosAssinatura() {
        return planoAssinaturaBO.total();
    }
}
