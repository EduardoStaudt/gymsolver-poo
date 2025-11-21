package model;

/**
 * Representa um cliente da academia.
 * Agora o cliente guarda diretamente o objeto PlanoAssinatura e PlanoTreino,
 * não só o nome em String. Isso facilita mostrar os dados e mudar depois.
 */
public class Cliente {

    private int id;
    private String nome;
    private String email;
    private String telefone;

    private PlanoAssinatura planoAssinatura;
    private PlanoTreino planoTreino;

    public Cliente() {
    }

    public Cliente(int id,
                   String nome,
                   String email,
                   String telefone,
                   PlanoAssinatura planoAssinatura,
                   PlanoTreino planoTreino) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.planoAssinatura = planoAssinatura;
        this.planoTreino = planoTreino;
    }

    // ========== GETTERS / SETTERS ==========

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public PlanoAssinatura getPlanoAssinatura() {
        return planoAssinatura;
    }

    public void setPlanoAssinatura(PlanoAssinatura planoAssinatura) {
        this.planoAssinatura = planoAssinatura;
    }

    public PlanoTreino getPlanoTreino() {
        return planoTreino;
    }

    public void setPlanoTreino(PlanoTreino planoTreino) {
        this.planoTreino = planoTreino;
    }

    // usado em ComboBox / debug
    @Override
    public String toString() {
        return nome;
    }
}
