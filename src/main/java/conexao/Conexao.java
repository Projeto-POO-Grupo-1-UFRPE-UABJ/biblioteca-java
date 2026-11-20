package conexao;

import java.sql.*;

public class Conexao {
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    private static final String TABELAUSUARIOS = """
            CREATE TABLE IF NOT EXISTS usuario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                matricula TEXT NOT NULL UNIQUE,
                limite INTEGER NOT NULL,
                tipo_usuario TEXT NOT NULL
            );""";

    private static final String TABELALIVROS = """
            CREATE TABLE IF NOT EXISTS livro (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                autor TEXT NOT NULL,
                isbn TEXT NOT NULL UNIQUE,
                total_exemplares INTEGER NOT NULL,
                exemplares_disponiveis INTEGER NOT NULL
            );""";

    private static final String TABELAEMPRESTIMOS = """
            CREATE TABLE IF NOT EXISTS emprestimo (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_livro INTEGER NOT NULL,
                id_usuario INTEGER NOT NULL,
                data_emprestimo TEXT NOT NULL,
                data_prevista TEXT NOT NULL,
                data_devolucao TEXT DEFAULT NULL,
                multa REAL DEFAULT NULL,
                FOREIGN KEY (id_livro)
                    REFERENCES livro (id)
                    ON DELETE RESTRICT
                    ON UPDATE NO ACTION,
                FOREIGN KEY (id_usuario)
                    REFERENCES usuario (id)
                    ON DELETE RESTRICT
                    ON UPDATE NO ACTION
            );""";

    public static Connection conectar() {
        try {
            Connection conexao = DriverManager.getConnection(URL);
            try (Statement stmt = conexao.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            return conexao;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco SQLite: " + e.getMessage(), e);
        }
    }

    private static void criarTabela(String tabela) {
        try (Connection conexao = conectar();
             Statement stmt = conexao.createStatement()) {
            stmt.execute(tabela);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela no banco SQLite: " + e.getMessage(), e);
        }
    }

    static {
        criarTabela(TABELAUSUARIOS);
        criarTabela(TABELALIVROS);
        criarTabela(TABELAEMPRESTIMOS);
    }
}
