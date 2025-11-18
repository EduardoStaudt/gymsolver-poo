package model;

// PlanoAssinatura.java
// Representa um plano de assinatura (mensal, trimestral, anual etc.).

public class PlanoAssinatura {

    private int id;
    private String nome;
    private double valorMensal;
    private int duracaoMeses;

    public PlanoAssinatura(int id, String nome, double valorMensal, int duracaoMeses) {
        this.id = id;
        this.nome = nome;
        this.valorMensal = valorMensal;
        this.duracaoMeses = duracaoMeses;
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

    public double getValorMensal() {
        return valorMensal;
    }

    public void setValorMensal(double valorMensal) {
        this.valorMensal = valorMensal;
    }

    public int getDuracaoMeses() {
        return duracaoMeses;
    }

    public void setDuracaoMeses(int duracaoMeses) {
        this.duracaoMeses = duracaoMeses;
    }

    @Override
    public String toString() {
        return nome;
    }
}
