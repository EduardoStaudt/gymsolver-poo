package model.vo;

public class PlanoTreinoVO {
    private final int id;
    private String nome;
    private String descricao;
    private String objetivo;

    public PlanoTreinoVO(int id, String nome, String descricao, String objetivo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.objetivo = objetivo;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    @Override
    public String toString() {
        return nome;
    }
}