package model.bo;

import model.dao.FuncionarioDAO;
import model.vo.FuncionarioVO;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class FuncionarioBO {
    private final FuncionarioDAO funcionarioDAO;
    
    public FuncionarioBO(FuncionarioDAO funcionarioDAO) {
        this.funcionarioDAO = funcionarioDAO;
    }
    
    public ObservableList<FuncionarioVO> listarTodos() {
        return funcionarioDAO.listarTodos();
    }
    
    public FilteredList<FuncionarioVO> listarFiltrados() {
        return new FilteredList<>(funcionarioDAO.listarTodos());
    }
    
    public FuncionarioVO criar(String nome, String cargo, String cpf) {
        if (!validar(nome, cargo, cpf)) {
            throw new IllegalArgumentException("Dados do funcionário inválidos");
        }
        
        int novoId = funcionarioDAO.proximoId();
        FuncionarioVO funcionario = new FuncionarioVO(novoId, nome.trim(), cargo.trim(), cpf.trim());
        funcionarioDAO.salvar(funcionario);
        return funcionario;
    }
    
    public void atualizar(FuncionarioVO funcionario, String nome, String cargo, String cpf) {
        if (funcionario != null && validar(nome, cargo, cpf)) {
            funcionario.setNome(nome.trim());
            funcionario.setCargo(cargo.trim());
            funcionario.setCpf(cpf.trim());
        }
    }
    
    public void excluir(FuncionarioVO funcionario) {
        if (funcionario != null) {
            funcionarioDAO.excluir(funcionario);
        }
    }
    
    public boolean validar(String nome, String cargo, String cpf) {
        if (nome == null || nome.trim().isEmpty()) return false;
        if (cargo == null || cargo.trim().isEmpty()) return false;
        if (cpf == null || cpf.trim().isEmpty()) return false;
        return true;
    }
    
    public int total() {
        return funcionarioDAO.listarTodos().size();
    }
}