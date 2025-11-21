package model;

/**
 * Plano de treino (Iniciante, Hipertrofia, etc.).
 */
public class PlanoTreino {

    private int id;
    private String nome;
    private String descricao;
    private String objetivo;

    public PlanoTreino() {
    }

    public PlanoTreino(int id, String nome, String descricao, String objetivo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.objetivo = objetivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    @Override
    public String toString() {
        return nome;
    }
}
