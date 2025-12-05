package model.dao;

import model.vo.FuncionarioVO;
import javafx.collections.ObservableList;

public class FuncionarioDAO {
    private final AcademiaDAO academiaDAO;
    
    public FuncionarioDAO(AcademiaDAO academiaDAO) {
        this.academiaDAO = academiaDAO;
    }
    
    public ObservableList<FuncionarioVO> listarTodos() {
        return academiaDAO.getFuncionarios();
    }
    
    public void salvar(FuncionarioVO funcionario) {
        academiaDAO.inserirFuncionario(funcionario);
    }

    public void atualizar(FuncionarioVO funcionario) {
        academiaDAO.atualizarFuncionario(funcionario);
    }
    
    public void excluir(FuncionarioVO funcionario) {
        academiaDAO.excluirFuncionario(funcionario);
    }
    
    public int proximoId() {
        return academiaDAO.proximoIdFuncionario();
    }
}
