package model.bo;

import model.dao.ClienteDAO;
import model.vo.ClienteVO;
import model.vo.PlanoAssinaturaVO;
import model.vo.PlanoTreinoVO;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Business Object para Clientes - Regras de negócio específicas
 */
public class ClienteBO {
    private final ClienteDAO clienteDAO;
    
    public ClienteBO(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }
    
    public ObservableList<ClienteVO> listarTodos() {
        return clienteDAO.listarTodos();
    }
    
    public FilteredList<ClienteVO> listarFiltrados() {
        return new FilteredList<>(clienteDAO.listarTodos());
    }
    
    public ClienteVO criar(String nome, String email, String telefone,
                         PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        if (!validar(nome, email, telefone, planoAssinatura, planoTreino)) {
            throw new IllegalArgumentException("Dados do cliente inválidos");
        }
        
        int novoId = clienteDAO.proximoId();
        ClienteVO cliente = new ClienteVO(novoId, nome.trim(), email.trim(), 
                                        telefone.trim(), planoAssinatura, planoTreino);
        clienteDAO.salvar(cliente);
        return cliente;
    }
    
    public void atualizar(ClienteVO cliente, String nome, String email, String telefone,
                        PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        if (cliente != null && validar(nome, email, telefone, planoAssinatura, planoTreino)) {
            cliente.setNome(nome.trim());
            cliente.setEmail(email.trim());
            cliente.setTelefone(telefone.trim());
            cliente.setPlanoAssinatura(planoAssinatura);
            cliente.setPlanoTreino(planoTreino);
            clienteDAO.atualizar(cliente);
        }
    }
    
    public void excluir(ClienteVO cliente) {
        if (cliente != null) {
            clienteDAO.excluir(cliente);
        }
    }
    
    public boolean validar(String nome, String email, String telefone,
                          PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        if (nome == null || nome.trim().isEmpty()) return false;
        if (email == null || email.trim().isEmpty() || !email.contains("@")) return false;
        if (telefone == null || telefone.trim().isEmpty()) return false;
        if (planoAssinatura == null) return false;
        if (planoTreino == null) return false;
        return true;
    }
    
    public String getErrosValidacao(String nome, String email, String telefone,
                                  PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        StringBuilder erros = new StringBuilder();
        
        if (nome == null || nome.trim().isEmpty()) {
            erros.append("• Informe o nome do cliente.\n");
        }
        
        if (email == null || email.trim().isEmpty()) {
            erros.append("• Informe o e-mail do cliente.\n");
        } else if (!email.contains("@")) {
            erros.append("• Informe um e-mail válido.\n");
        }
        
        if (telefone == null || telefone.trim().isEmpty()) {
            erros.append("• Informe o telefone do cliente.\n");
        }
        
        if (planoAssinatura == null) {
            erros.append("• Selecione um plano de assinatura.\n");
        }
        
        if (planoTreino == null) {
            erros.append("• Selecione um plano de treino.\n");
        }
        
        return erros.toString();
    }
    
    public int total() {
        return clienteDAO.listarTodos().size();
    }
}
