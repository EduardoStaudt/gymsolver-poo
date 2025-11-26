package model.vo;

public class ClienteVO {
    private final int id;
    private String nome;
    private String email;
    private String telefone;
    private PlanoAssinaturaVO planoAssinatura;
    private PlanoTreinoVO planoTreino;

    public ClienteVO(int id, String nome, String email, String telefone, 
                    PlanoAssinaturaVO planoAssinatura, PlanoTreinoVO planoTreino) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.planoAssinatura = planoAssinatura;
        this.planoTreino = planoTreino;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public PlanoAssinaturaVO getPlanoAssinatura() { return planoAssinatura; }
    public void setPlanoAssinatura(PlanoAssinaturaVO planoAssinatura) { this.planoAssinatura = planoAssinatura; }
    public PlanoTreinoVO getPlanoTreino() { return planoTreino; }
    public void setPlanoTreino(PlanoTreinoVO planoTreino) { this.planoTreino = planoTreino; }

    @Override
    public String toString() {
        return nome;
    }
}