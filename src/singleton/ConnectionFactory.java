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
    private Connection connection;             // conexão reaproveitada

    private ConnectionFactory() {
        try {
            // 1) Carrega o driver do SQLite (está no sqlite-jdbc-xxx.jar da pasta lib)
            Class.forName("org.sqlite.JDBC");

            // 2) URL do banco - arquivo gymsolver.db na pasta do projeto
            String url = "jdbc:sqlite:gymsolver.db";

            // 3) Abre a conexão
            this.connection = DriverManager.getConnection(url);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite não encontrado", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
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
        return connection;
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

        try (Connection conn = getInstance().getConnection(); Statement stmt = conn.createStatement()) {

            stmt.execute(sqlUsuario);
            inserirAdminDefault(conn);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar as tabelas do banco", e);
        }
    }

    /**
     * Insere um usuário admin padrão caso a tabela esteja vazia.
     */
    private static void inserirAdminDefault(Connection conn) throws SQLException {
        String sqlCount = "SELECT COUNT(*) FROM usuario";
        try (PreparedStatement ps = conn.prepareStatement(sqlCount); ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                return; // já existe usuário cadastrado
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
}
