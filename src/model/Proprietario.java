package model;

/**
 * Proprietário da academia.
 * Também apenas estende Funcionario.
 */
public class Proprietario extends Funcionario {

    public Proprietario(int id, String nome, String cargo, String cpf) {
        super(id, nome, cargo, cpf);
    }
}

