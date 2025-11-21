package model;

/**
 * Personal trainer da academia.
 * Por enquanto só estende Funcionario.
 */
public class Personal extends Funcionario {

    public Personal(int id, String nome, String cargo, String cpf) {
        super(id, nome, cargo, cpf);
    }
}

