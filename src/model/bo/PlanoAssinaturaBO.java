package model.bo;

import model.dao.AcademiaDAO;
import model.vo.PlanoAssinaturaVO;
import javafx.collections.ObservableList;

public class PlanoAssinaturaBO {
    private final AcademiaDAO academiaDAO;
    
    public PlanoAssinaturaBO(AcademiaDAO academiaDAO) {
        this.academiaDAO = academiaDAO;
    }
    
    public ObservableList<PlanoAssinaturaVO> listarTodos() {
        return academiaDAO.getPlanosAssinatura();
    }
    
    public PlanoAssinaturaVO criar(String nome, double valorMensal, int duracaoMeses) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do plano de assinatura é obrigatório");
        }
        
        if (valorMensal <= 0) {
            throw new IllegalArgumentException("Valor mensal deve ser maior que zero");
        }
        
        int novoId = academiaDAO.proximoIdPlanoAssinatura();
        PlanoAssinaturaVO plano = new PlanoAssinaturaVO(novoId, nome.trim(), valorMensal, duracaoMeses);
        academiaDAO.inserirPlanoAssinatura(plano);
        return plano;
    }
    
    public int total() {
        return academiaDAO.getPlanosAssinatura().size();
    }
}