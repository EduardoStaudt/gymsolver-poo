package model.dao;

import model.vo.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface UsuarioDAO {

    void inserir(Usuario usuario) throws SQLException;

    Usuario buscarPorEmailSenha(String email, String senha) throws SQLException;

    List<Usuario> listarTodos() throws SQLException;
}
