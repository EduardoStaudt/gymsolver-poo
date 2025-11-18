package model;

// Cliente.java
// Representa um cliente da academia.

public class Cliente {

    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String planoAssinatura; // aqui guardamos apenas o nome do plano
    private String planoTreino;     // aqui guardamos apenas o nome do plano de treino

    public Cliente(int id, String nome, String email, String telefone, String planoAssinatura, String planoTreino) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.planoAssinatura = planoAssinatura;
        this.planoTreino = planoTreino;
    }

    // Getters e setters simples (TableView usa os getters via reflection)

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

    public String getPlanoAssinatura() {
        return planoAssinatura;
    }

    public void setPlanoAssinatura(String planoAssinatura) {
        this.planoAssinatura = planoAssinatura;
    }

    public String getPlanoTreino() {
        return planoTreino;
    }

    public void setPlanoTreino(String planoTreino) {
        this.planoTreino = planoTreino;
    }

    @Override
    public String toString() {
        return nome;
    }
}
