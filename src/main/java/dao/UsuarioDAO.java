package dao;

import java.sql.*;
import java.util.ArrayList;
import conexao.Conexao;
import excecoes.*;
import modelos.*;

public class UsuarioDAO {
    public static void cadastrar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, matricula, tipo_usuario, limite) VALUES (?, ?, ?, ?)";

        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setString(1, usuario.getNome());
            declaracao.setString(2, usuario.getMatricula());
            declaracao.setString(3, usuario.getTipo());
            declaracao.setInt(4, usuario.getLimite());

            declaracao.executeUpdate();
        }
    }

    public static ArrayList<Usuario> listar() throws SQLException {
        ArrayList<Usuario> usuarios = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement("SELECT id, nome, matricula, limite, tipo_usuario FROM usuario ORDER BY id");
                ResultSet rs = declaracao.executeQuery()) {

            while (rs.next()) {
                usuarios.add(montarUsuario(rs));
            }
        }
        return usuarios;
    }
    
    public static ArrayList<Usuario> buscarPorNomeOuMatricula(String termo) throws SQLException {
        String sql = "SELECT id, nome, matricula, limite, tipo_usuario FROM usuario WHERE nome LIKE ? OR matricula LIKE ? ORDER BY id";
        ArrayList<Usuario> usuarios = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setString(1, "%" + termo + "%");
            declaracao.setString(2, "%" + termo + "%");

            try (ResultSet rs = declaracao.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(montarUsuario(rs));
                }
            }
        }
        return usuarios;
    }

    public static Usuario buscarPorId(int id) throws SQLException, RegistroNaoEncontradoException {
        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement("SELECT id, nome, matricula, limite, tipo_usuario FROM usuario WHERE id = ?")) {

            declaracao.setInt(1, id);

            try (ResultSet rs = declaracao.executeQuery()) {
                if (rs.next()) {
                    return montarUsuario(rs);
                }
            }
        }

        throw new RegistroNaoEncontradoException("Não há nenhum usuário com o Id " + id + ".");
    }

    public static void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nome = ?, matricula = ? WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setString(1, usuario.getNome());
            declaracao.setString(2, usuario.getMatricula());
            declaracao.setInt(3, usuario.getId());

            declaracao.executeUpdate();
        }
    }

    public static void atualizarLimite(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET limite = ? WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setInt(1, usuario.getLimite());
            declaracao.setInt(2, usuario.getId());

            declaracao.executeUpdate();
        }
    }

    public static void remover(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setInt(1, id);

            declaracao.executeUpdate();
        }
    }

    private static Usuario montarUsuario(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String matricula = rs.getString("matricula");
        String tipo = rs.getString("tipo_usuario");
        int limite = rs.getInt("limite");

        if("Professor".equalsIgnoreCase(tipo)) {
            return new Professor(id, nome, matricula, limite);
        } 

        return new Aluno(id, nome, matricula, limite);
    }       
}