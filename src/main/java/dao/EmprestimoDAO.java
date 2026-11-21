package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import conexao.Conexao;
import excecoes.RegistroNaoEncontradoException;
import modelos.*;

public class EmprestimoDAO {
    private static final String SELECT = """
            SELECT e.id, e.data_emprestimo, e.data_prevista, e.data_devolucao, e.multa,
                   u.id AS id_usuario, u.nome, u.matricula, u.limite, u.tipo_usuario,
                   l.id AS id_livro, l.titulo, l.autor, l.isbn, l.total_exemplares, l.exemplares_disponiveis
            FROM emprestimo e
            JOIN usuario u ON u.id = e.id_usuario
            JOIN livro l ON l.id = e.id_livro""";

    public void cadastrar(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimo (id_livro, id_usuario, data_emprestimo, data_prevista) VALUES (?, ?, ?, ?)";
        
        try (Connection conexao = Conexao.conectar();
             PreparedStatement declaracao = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            declaracao.setInt(1, emprestimo.getLivro().getId());
            declaracao.setInt(2, emprestimo.getUsuario().getId());
            declaracao.setString(3, emprestimo.getDataEmprestimo().toString());
            declaracao.setString(4, emprestimo.getDataPrevista().toString());

            declaracao.executeUpdate();

            try (ResultSet chavesGeradas = declaracao.getGeneratedKeys()) {
                if (chavesGeradas.next()) {
                    emprestimo.setId(chavesGeradas.getInt(1));
                }
            }
        }
    }

    public ArrayList<Emprestimo> listar() throws SQLException {
        ArrayList<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement declaracao = conexao.prepareStatement(SELECT);
             ResultSet rs = declaracao.executeQuery()) {

            while (rs.next()) {
                emprestimos.add(montarEmprestimo(rs));
            }
        }
        return emprestimos;
    }

    public Emprestimo buscarPorId(int id) throws SQLException, RegistroNaoEncontradoException {
        try (Connection conexao = Conexao.conectar();
             PreparedStatement declaracao = conexao.prepareStatement(SELECT + " WHERE e.id = ?")) {

            declaracao.setInt(1, id);

            try (ResultSet rs = declaracao.executeQuery()) {
                if (rs.next()) {
                    return montarEmprestimo(rs);
                }
            }
        }
        throw new RegistroNaoEncontradoException("Não há nenhum empréstimo com o Id " + id + ".");
    }

    public void atualizar(Emprestimo emprestimo) throws SQLException {
        String sql = "UPDATE emprestimo SET data_devolucao = ?, multa = ? WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            if (emprestimo.estaAberto()) {
                declaracao.setNull(1, Types.VARCHAR);
                declaracao.setNull(2, Types.REAL);
            } else {
                declaracao.setString(1, emprestimo.getDataDevolucao().toString());
                declaracao.setDouble(2, emprestimo.calcMulta());
            }

            declaracao.setInt(3, emprestimo.getId());

            declaracao.executeUpdate();
        }
    }

    private Emprestimo montarEmprestimo(ResultSet rs) throws SQLException {
        int limite = rs.getInt("limite");
        Usuario usuario;

        // Proteção contra nulos e case-insensitive
        if ("Professor".equalsIgnoreCase(rs.getString("tipo_usuario"))) {
            usuario = new Professor(rs.getInt("id_usuario"), rs.getString("nome"), rs.getString("matricula"), limite);
        } else {
            usuario = new Aluno(rs.getInt("id_usuario"), rs.getString("nome"), rs.getString("matricula"), limite);
        }

        Livro livro = new Livro(
            rs.getInt("id_livro"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("isbn"),
            rs.getInt("total_exemplares"),
            rs.getInt("exemplares_disponiveis")
        );

        String devolucao = rs.getString("data_devolucao");

        return new Emprestimo(
            rs.getInt("id"),
            usuario,
            livro,
            LocalDate.parse(rs.getString("data_emprestimo")),
            LocalDate.parse(rs.getString("data_prevista")),
            devolucao == null ? null : LocalDate.parse(devolucao)
        );
    }
}
