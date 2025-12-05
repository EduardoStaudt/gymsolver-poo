package singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Singleton responsável pela conexão JDBC com o banco de dados.
 * Usa SQLite (arquivo gymsolver.db na raiz do projeto).
 *
 * Se um dia quiser trocar para MySQL, basta ajustar driver e URL aqui.
 */
public class ConnectionFactory {

    private static ConnectionFactory instance; // única instância
    private final String url;                  // URL de conexão

    private ConnectionFactory() {
        try {
            // 1) Carrega o driver do SQLite (está no sqlite-jdbc-xxx.jar da pasta lib)
            Class.forName("org.sqlite.JDBC");

            // 2) URL do banco - arquivo gymsolver.db na pasta do projeto
            this.url = "jdbc:sqlite:gymsolver.db";

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite não encontrado", e);
        }
    }

    // Acesso global ao Singleton
    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    // Entrega a Connection para os DAOs
    public Connection getConnection() {
        try {
            // abre uma nova conexão a cada chamada; try-with-resources nos DAOs fecha corretamente
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    /**
     * Cria as tabelas necessárias, caso ainda não existam.
     * Aqui definimos a tabela USUARIO com uma coluna de data.
     */
    public static void initDatabase() {
        String sqlUsuario = """
                CREATE TABLE IF NOT EXISTS usuario (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    senha TEXT NOT NULL,
                    data_cadastro TEXT NOT NULL  -- formato ISO: YYYY-MM-DD
                );
                """;

        String sqlPlanoAssinatura = """
                CREATE TABLE IF NOT EXISTS plano_assinatura (
                    id INTEGER PRIMARY KEY,
                    nome TEXT NOT NULL,
                    valor_mensal REAL NOT NULL,
                    duracao_meses INTEGER NOT NULL
                );
                """;

        String sqlPlanoTreino = """
                CREATE TABLE IF NOT EXISTS plano_treino (
                    id INTEGER PRIMARY KEY,
                    nome TEXT NOT NULL,
                    descricao TEXT,
                    objetivo TEXT
                );
                """;

        String sqlFuncionario = """
                CREATE TABLE IF NOT EXISTS funcionario (
                    id INTEGER PRIMARY KEY,
                    nome TEXT NOT NULL,
                    cargo TEXT,
                    cpf TEXT
                );
                """;

        String sqlCliente = """
                CREATE TABLE IF NOT EXISTS cliente (
                    id INTEGER PRIMARY KEY,
                    nome TEXT NOT NULL,
                    email TEXT,
                    telefone TEXT,
                    plano_assinatura_id INTEGER,
                    plano_treino_id INTEGER,
                    FOREIGN KEY (plano_assinatura_id) REFERENCES plano_assinatura(id),
                    FOREIGN KEY (plano_treino_id) REFERENCES plano_treino(id)
                );
                """;

        try (Connection conn = getInstance().getConnection(); Statement stmt = conn.createStatement()) {

            stmt.execute(sqlUsuario);
            stmt.execute(sqlPlanoAssinatura);
            stmt.execute(sqlPlanoTreino);
            stmt.execute(sqlFuncionario);
            stmt.execute(sqlCliente);
            inserirAdminDefault(conn);
            inserirDadosBasicos(conn);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar as tabelas do banco", e);
        }
    }

    /**
     * Garante a existência de um usuário admin padrão (idempotente).
     */
    private static void inserirAdminDefault(Connection conn) throws SQLException {
        String sqlExiste = "SELECT 1 FROM usuario WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlExiste)) {
            ps.setString(1, "admin@gym.com");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return; // admin já cadastrado
                }
            }
        }

        String sqlInsert = "INSERT INTO usuario (nome, email, senha, data_cadastro) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.setString(1, "Administrador");
            ps.setString(2, "admin@gym.com");
            ps.setString(3, "123");
            ps.setString(4, LocalDate.now().toString());
            ps.executeUpdate();
        }
    }

    /**
     * Preenche tabelas de planos/funcionários/clientes com dados iniciais se estiverem vazias.
     */
    private static void inserirDadosBasicos(Connection conn) throws SQLException {
        // Planos de assinatura
        String sqlCountAssin = "SELECT COUNT(*) FROM plano_assinatura";
        try (PreparedStatement ps = conn.prepareStatement(sqlCountAssin); ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement ins = conn.prepareStatement(
                        "INSERT INTO plano_assinatura (id, nome, valor_mensal, duracao_meses) VALUES (?, ?, ?, ?)")) {
                    ins.setInt(1, 1); ins.setString(2, "Mensal"); ins.setDouble(3, 99.90); ins.setInt(4, 1); ins.executeUpdate();
                    ins.setInt(1, 2); ins.setString(2, "Trimestral"); ins.setDouble(3, 249.90); ins.setInt(4, 3); ins.executeUpdate();
                }
            }
        }

        // Planos de treino
        String sqlCountTreino = "SELECT COUNT(*) FROM plano_treino";
        try (PreparedStatement ps = conn.prepareStatement(sqlCountTreino); ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement ins = conn.prepareStatement(
                        "INSERT INTO plano_treino (id, nome, descricao, objetivo) VALUES (?, ?, ?, ?)")) {
                    ins.setInt(1, 1); ins.setString(2, "Iniciante"); ins.setString(3, "Treino básico 3x por semana"); ins.setString(4, "Adaptação"); ins.executeUpdate();
                    ins.setInt(1, 2); ins.setString(2, "Hipertrofia"); ins.setString(3, "Treino pesado 5x por semana"); ins.setString(4, "Hipertrofia"); ins.executeUpdate();
                }
            }
        }

        // Funcionário exemplo
        String sqlCountFunc = "SELECT COUNT(*) FROM funcionario";
        try (PreparedStatement ps = conn.prepareStatement(sqlCountFunc); ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement ins = conn.prepareStatement(
                        "INSERT INTO funcionario (id, nome, cargo, cpf) VALUES (?, ?, ?, ?)")) {
                    ins.setInt(1, 1); ins.setString(2, "João Instrutor"); ins.setString(3, "Instrutor"); ins.setString(4, "123.456.789-00"); ins.executeUpdate();
                }
            }
        }

        // Cliente exemplo
        String sqlCountCliente = "SELECT COUNT(*) FROM cliente";
        try (PreparedStatement ps = conn.prepareStatement(sqlCountCliente); ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement ins = conn.prepareStatement(
                        "INSERT INTO cliente (id, nome, email, telefone, plano_assinatura_id, plano_treino_id) VALUES (?, ?, ?, ?, ?, ?)")) {
                    ins.setInt(1, 1); ins.setString(2, "Maria Aluna"); ins.setString(3, "maria@exemplo.com");
                    ins.setString(4, "(45) 99999-9999"); ins.setInt(5, 1); ins.setInt(6, 1); ins.executeUpdate();
                }
            }
        }
    }
}
