package model.bo;

import model.dao.AcademiaDAO;
import model.vo.PlanoTreinoVO;
import javafx.collections.ObservableList;

public class PlanoTreinoBO {
    private final AcademiaDAO academiaDAO;
    
    public PlanoTreinoBO(AcademiaDAO academiaDAO) {
        this.academiaDAO = academiaDAO;
    }
    
    public ObservableList<PlanoTreinoVO> listarTodos() {
        return academiaDAO.getPlanosTreino();
    }
    
    public PlanoTreinoVO criar(String nome, String descricao, String objetivo) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do plano de treino é obrigatório");
        }
        
        int novoId = academiaDAO.proximoIdPlanoTreino();
        PlanoTreinoVO plano = new PlanoTreinoVO(novoId, nome.trim(), descricao, objetivo);
        academiaDAO.inserirPlanoTreino(plano);
        return plano;
    }
    
    public int total() {
        return academiaDAO.getPlanosTreino().size();
    }
}