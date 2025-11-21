package model;

/**
 * Plano de assinatura (mensal, trimestral, etc.).
 */
public class PlanoAssinatura {

    private int id;
    private String nome;
    private double valorMensal;
    private int duracaoMeses;

    public PlanoAssinatura() {
    }

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
        // isso que aparece nas ComboBox
        return nome;
    }
}
