package model.dao;

import model.vo.Usuario;
import singleton.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação concreta de UsuarioDAO usando JDBC + SQLite.
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, data_cadastro) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            // LocalDate -> TEXT (YYYY-MM-DD)
            ps.setString(4, usuario.getDataCadastro().toString());

            ps.executeUpdate();
        }
    }

    @Override
    public Usuario buscarPorEmailSenha(String email, String senha) throws SQLException {
        String sql = "SELECT id, nome, email, data_cadastro FROM usuario WHERE email = ? AND senha = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setDataCadastro(LocalDate.parse(rs.getString("data_cadastro")));
                    return u;
                }
            }
        }
        return null; // não achou
    }

    @Override
    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, email, data_cadastro FROM usuario";
        List<Usuario> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setDataCadastro(LocalDate.parse(rs.getString("data_cadastro")));
                lista.add(u);
            }
        }
        return lista;
    }
}
