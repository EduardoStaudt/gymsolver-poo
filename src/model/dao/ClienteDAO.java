package model.dao;

import model.vo.ClienteVO;
import javafx.collections.ObservableList;

/**
 * DAO Específico para Clientes
 */
public class ClienteDAO {
    private final AcademiaDAO academiaDAO;
    
    public ClienteDAO(AcademiaDAO academiaDAO) {
        this.academiaDAO = academiaDAO;
    }
    
    public ObservableList<ClienteVO> listarTodos() {
        return academiaDAO.getClientes();
    }
    
    public ClienteVO buscarPorId(int id) {
        return academiaDAO.getClientes().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public void salvar(ClienteVO cliente) {
        academiaDAO.inserirCliente(cliente);
    }
    
    public void excluir(ClienteVO cliente) {
        academiaDAO.excluirCliente(cliente);
    }
    
    public int proximoId() {
        return academiaDAO.proximoIdCliente();
    }
}