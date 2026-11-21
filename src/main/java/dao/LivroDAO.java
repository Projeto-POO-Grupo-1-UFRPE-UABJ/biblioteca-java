package dao;

import java.sql.*;
import conexao.Conexao;
import modelos.Livro;
import java.util.ArrayList;

public class LivroDAO {
    public void cadastrar(Livro livro) throws SQLException {
        String sql = "INSERT INTO livro (titulo, autor, isbn, total_exemplares, exemplares_disponiveis) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setString(1, livro.getTitulo());
            declaracao.setString(2, livro.getAutor());
            declaracao.setString(3, livro.getIsbn());
            declaracao.setInt(4, livro.getTotalExemplares());
            declaracao.setInt(5, livro.getExemplaresDisponiveis());

            declaracao.executeUpdate();
        }
    }

    public ArrayList<Livro> listar() throws SQLException {
        String sql = "SELECT id, titulo, autor, isbn, total_exemplares, exemplares_disponiveis FROM livro";
        ArrayList<Livro> livro = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement(sql);
                ResultSet rs = declaracao.executeQuery()) {

            while (rs.next()) {
                Livro l = new Livro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"),
                        rs.getString("isbn"), rs.getInt("total_exemplares"), rs.getInt("exemplares_disponiveis"));
                livro.add(l);
            }
        }
        return livro;
    }

    public ArrayList<Livro> buscarPorTitulo(String termo) throws SQLException {
        String sql = "SELECT id, titulo, autor, isbn, total_exemplares, exemplares_disponiveis FROM livro WHERE titulo LIKE ?";
        ArrayList<Livro> livros = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setString(1, "%" + termo + "%");

            try (ResultSet rs = declaracao.executeQuery()) {
                while (rs.next()) {
                    livros.add(new Livro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"),
                            rs.getString("isbn"), rs.getInt("total_exemplares"), rs.getInt("exemplares_disponiveis")));
                }
            }
        }
        return livros;
    }

    public void atualizar(Livro livro) throws SQLException {
        String sql = "UPDATE livro SET titulo = ?, autor = ?, isbn = ?, total_exemplares = ?, exemplares_disponiveis = ? WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setString(1, livro.getTitulo());
            declaracao.setString(2, livro.getAutor());
            declaracao.setString(3, livro.getIsbn());
            declaracao.setInt(4, livro.getTotalExemplares());
            declaracao.setInt(5, livro.getExemplaresDisponiveis());
            declaracao.setInt(6, livro.getId());

            declaracao.executeUpdate();
        }
    }

    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM livro WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setInt(1, id);
            declaracao.executeUpdate();
        }
    }
}