package model.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.vo.ClienteVO;
import model.vo.FuncionarioVO;
import model.vo.PlanoAssinaturaVO;
import model.vo.PlanoTreinoVO;
import singleton.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO Principal - Gerencia todo o acesso a dados, sincronizando listas observáveis com o SQLite.
 */
public class AcademiaDAO {
    private final ObservableList<ClienteVO> clientes;
    private final ObservableList<FuncionarioVO> funcionarios;
    private final ObservableList<PlanoTreinoVO> planosTreino;
    private final ObservableList<PlanoAssinaturaVO> planosAssinatura;

    public AcademiaDAO() {
        this.clientes = FXCollections.observableArrayList();
        this.funcionarios = FXCollections.observableArrayList();
        this.planosTreino = FXCollections.observableArrayList();
        this.planosAssinatura = FXCollections.observableArrayList();

        carregarDoBanco();
    }

    private void carregarDoBanco() {
        planosAssinatura.clear();
        planosTreino.clear();
        funcionarios.clear();
        clientes.clear();

        // carrega planos primeiro para mapear em clientes
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, nome, valor_mensal, duracao_meses FROM plano_assinatura");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    planosAssinatura.add(new PlanoAssinaturaVO(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getDouble("valor_mensal"),
                            rs.getInt("duracao_meses")
                    ));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT id, nome, descricao, objetivo FROM plano_treino");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    planosTreino.add(new PlanoTreinoVO(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            rs.getString("objetivo")
                    ));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT id, nome, cargo, cpf FROM funcionario");
                ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    funcionarios.add(new FuncionarioVO(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cargo"),
                            rs.getString("cpf")
                    ));
                }
            }

            Map<Integer, PlanoAssinaturaVO> planosAssinMap = new HashMap<>();
            for (PlanoAssinaturaVO p : planosAssinatura) {
                planosAssinMap.put(p.getId(), p);
            }
            Map<Integer, PlanoTreinoVO> planosTreinoMap = new HashMap<>();
            for (PlanoTreinoVO p : planosTreino) {
                planosTreinoMap.put(p.getId(), p);
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT id, nome, email, telefone, plano_assinatura_id, plano_treino_id FROM cliente");
                ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PlanoAssinaturaVO planoAssin = planosAssinMap.get(rs.getInt("plano_assinatura_id"));
                    PlanoTreinoVO planoTreino = planosTreinoMap.get(rs.getInt("plano_treino_id"));
                    clientes.add(new ClienteVO(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("telefone"),
                            planoAssin,
                            planoTreino
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar dados do banco", e);
        }
    }

    // ==================== GETTERS DAS LISTAS ====================
    public ObservableList<ClienteVO> getClientes() { return clientes; }
    public ObservableList<FuncionarioVO> getFuncionarios() { return funcionarios; }
    public ObservableList<PlanoTreinoVO> getPlanosTreino() { return planosTreino; }
    public ObservableList<PlanoAssinaturaVO> getPlanosAssinatura() { return planosAssinatura; }

    // ==================== OPERAÇÕES BÁSICAS ====================
    public void inserirCliente(ClienteVO cliente) {
        String sql = "INSERT INTO cliente (id, nome, email, telefone, plano_assinatura_id, plano_treino_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cliente.getId());
            ps.setString(2, cliente.getNome());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getTelefone());
            ps.setObject(5, cliente.getPlanoAssinatura() != null ? cliente.getPlanoAssinatura().getId() : null);
            ps.setObject(6, cliente.getPlanoTreino() != null ? cliente.getPlanoTreino().getId() : null);
            ps.executeUpdate();
            clientes.add(cliente);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente", e);
        }
    }

    public void atualizarCliente(ClienteVO cliente) {
        String sql = "UPDATE cliente SET nome = ?, email = ?, telefone = ?, plano_assinatura_id = ?, plano_treino_id = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getTelefone());
            ps.setObject(4, cliente.getPlanoAssinatura() != null ? cliente.getPlanoAssinatura().getId() : null);
            ps.setObject(5, cliente.getPlanoTreino() != null ? cliente.getPlanoTreino().getId() : null);
            ps.setInt(6, cliente.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
        }
    }

    public void excluirCliente(ClienteVO cliente) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cliente.getId());
            ps.executeUpdate();
            clientes.remove(cliente);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente", e);
        }
    }

    public void inserirFuncionario(FuncionarioVO funcionario) {
        String sql = "INSERT INTO funcionario (id, nome, cargo, cpf) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, funcionario.getId());
            ps.setString(2, funcionario.getNome());
            ps.setString(3, funcionario.getCargo());
            ps.setString(4, funcionario.getCpf());
            ps.executeUpdate();
            funcionarios.add(funcionario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir funcionário", e);
        }
    }

    public void atualizarFuncionario(FuncionarioVO funcionario) {
        String sql = "UPDATE funcionario SET nome = ?, cargo = ?, cpf = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, funcionario.getNome());
            ps.setString(2, funcionario.getCargo());
            ps.setString(3, funcionario.getCpf());
            ps.setInt(4, funcionario.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar funcionário", e);
        }
    }

    public void excluirFuncionario(FuncionarioVO funcionario) {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, funcionario.getId());
            ps.executeUpdate();
            funcionarios.remove(funcionario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir funcionário", e);
        }
    }

    public void inserirPlanoTreino(PlanoTreinoVO plano) {
        String sql = "INSERT INTO plano_treino (id, nome, descricao, objetivo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, plano.getId());
            ps.setString(2, plano.getNome());
            ps.setString(3, plano.getDescricao());
            ps.setString(4, plano.getObjetivo());
            ps.executeUpdate();
            planosTreino.add(plano);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir plano de treino", e);
        }
    }

    public void atualizarPlanoTreino(PlanoTreinoVO plano) {
        String sql = "UPDATE plano_treino SET nome = ?, descricao = ?, objetivo = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plano.getNome());
            ps.setString(2, plano.getDescricao());
            ps.setString(3, plano.getObjetivo());
            ps.setInt(4, plano.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar plano de treino", e);
        }
    }

    public void excluirPlanoTreino(PlanoTreinoVO plano) {
        String sql = "DELETE FROM plano_treino WHERE id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, plano.getId());
            ps.executeUpdate();
            planosTreino.remove(plano);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir plano de treino", e);
        }
    }

    public void inserirPlanoAssinatura(PlanoAssinaturaVO plano) {
        String sql = "INSERT INTO plano_assinatura (id, nome, valor_mensal, duracao_meses) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, plano.getId());
            ps.setString(2, plano.getNome());
            ps.setDouble(3, plano.getValorMensal());
            ps.setInt(4, plano.getDuracaoMeses());
            ps.executeUpdate();
            planosAssinatura.add(plano);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir plano de assinatura", e);
        }
    }

    public void atualizarPlanoAssinatura(PlanoAssinaturaVO plano) {
        String sql = "UPDATE plano_assinatura SET nome = ?, valor_mensal = ?, duracao_meses = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plano.getNome());
            ps.setDouble(2, plano.getValorMensal());
            ps.setInt(3, plano.getDuracaoMeses());
            ps.setInt(4, plano.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar plano de assinatura", e);
        }
    }

    public void excluirPlanoAssinatura(PlanoAssinaturaVO plano) {
        String sql = "DELETE FROM plano_assinatura WHERE id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, plano.getId());
            ps.executeUpdate();
            planosAssinatura.remove(plano);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir plano de assinatura", e);
        }
    }

    // ==================== GERADORES DE ID ====================
    public int proximoIdCliente() {
        return clientes.stream().mapToInt(ClienteVO::getId).max().orElse(0) + 1;
    }

    public int proximoIdFuncionario() {
        return funcionarios.stream().mapToInt(FuncionarioVO::getId).max().orElse(0) + 1;
    }

    public int proximoIdPlanoTreino() {
        return planosTreino.stream().mapToInt(PlanoTreinoVO::getId).max().orElse(0) + 1;
    }

    public int proximoIdPlanoAssinatura() {
        return planosAssinatura.stream().mapToInt(PlanoAssinaturaVO::getId).max().orElse(0) + 1;
    }
}
