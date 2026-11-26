
package gerenciadores;

import java.util.ArrayList;
import java.sql.SQLException;
import modelos.*;
import excecoes.*;
import dao.*;

public class GerenciadorUsuarios {

    public static ArrayList<Usuario> getUsuarios() throws SQLException {
        return UsuarioDAO.listar();
    }

    public static Usuario buscarUsuario(int id) throws RegistroNaoEncontradoException, SQLException {
        return UsuarioDAO.buscarPorId(id);
    }

    public static ArrayList<Usuario> buscarUsuarios(String termo) throws SQLException {
        ArrayList<Usuario> usuariosEncontrados = new ArrayList<>();
        String procurado = termo.trim().toLowerCase();

        for (Usuario usuario : UsuarioDAO.listar()) {
            if (usuario.getNome().toLowerCase().contains(procurado) ||
                usuario.getMatricula().toLowerCase().contains(procurado)) {

                usuariosEncontrados.add(usuario);
            }
        }

        return usuariosEncontrados;
    }

    public static Usuario cadastrarUsuario(String nome, String matricula, String tipo)
            throws OperacaoInvalidaException, SQLException {

        validarDados(nome, matricula);
        validarTipo(tipo);
        validarMatriculaLivre(matricula, -1);

        Usuario usuario;

        if ("Professor".equalsIgnoreCase(tipo.trim())) {
            usuario = new Professor(0, nome.trim(), matricula.trim());
        } else {
            usuario = new Aluno(0, nome.trim(), matricula.trim());
        }

        UsuarioDAO.cadastrar(usuario);
        return usuario;
    }

    public static void editarUsuario(int id, String nome, String matricula)
            throws RegistroNaoEncontradoException, OperacaoInvalidaException, SQLException {

        Usuario usuario = buscarUsuario(id);

        validarDados(nome, matricula);
        validarMatriculaLivre(matricula, id);

        usuario.setNome(nome.trim());
        usuario.setMatricula(matricula.trim());

        UsuarioDAO.atualizar(usuario);
    }

    public static void removerUsuario(int id)
            throws RegistroNaoEncontradoException, OperacaoInvalidaException, SQLException {

        Usuario usuario = buscarUsuario(id);

        if (usuario.getLimite() > 0) {
            throw new OperacaoInvalidaException(
                "Não é possível remover um usuário que possui empréstimos ativos."
            );
        }

        UsuarioDAO.remover(id);
    }

    public static String listarUsuarios() throws SQLException {
        ArrayList<Usuario> usuarios = UsuarioDAO.listar();

        if (usuarios.isEmpty()) {
            return "Ainda não há nenhum usuário cadastrado.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Lista de Usuários:\n\n");

        for (Usuario usuario : usuarios) {
            sb.append(formatarUsuario(usuario));
        }

        return sb.toString();
    }

    public static String listarUsuarios(String termo) throws SQLException {
        ArrayList<Usuario> usuariosEncontrados = buscarUsuarios(termo);

        if (usuariosEncontrados.isEmpty()) {
            return "Nenhum usuário encontrado para \"" + termo + "\".\n\n" +
                   "Dica: Tente pesquisar pelo nome ou matrícula do usuário.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Usuários encontrados para \"").append(termo).append("\":\n\n");

        for (Usuario usuario : usuariosEncontrados) {
            sb.append(formatarUsuario(usuario));
        }

        return sb.toString();
    }

    public static String formatarUsuario(Usuario usuario) {
        StringBuilder sb = new StringBuilder();

        sb.append("Id: ").append(usuario.getId()).append("\n");
        sb.append("Nome: ").append(usuario.getNome()).append("\n");
        sb.append("Matrícula: ").append(usuario.getMatricula()).append("\n");
        sb.append("Tipo: ").append(usuario.getTipo()).append("\n");
        sb.append("Empréstimos Ativos: ").append(usuario.getLimite()).append("\n");
        sb.append("Limite Máximo: ").append(usuario.getLimiteMaximo()).append("\n\n");

        return sb.toString();
    }

    private static void validarDados(String nome, String matricula)
            throws OperacaoInvalidaException {

        if (nome == null || nome.isBlank() ||
            matricula == null || matricula.isBlank()) {

            throw new OperacaoInvalidaException(
                "Nome e matrícula são obrigatórios."
            );
        }
    }

    private static void validarTipo(String tipo)
            throws OperacaoInvalidaException {

        if (tipo == null ||
            (!tipo.trim().equalsIgnoreCase("Aluno") &&
             !tipo.trim().equalsIgnoreCase("Professor"))) {

            throw new OperacaoInvalidaException(
                "O tipo de usuário deve ser Aluno ou Professor."
            );
        }
    }

    private static void validarMatriculaLivre(String matricula, int idIgnorado)
            throws OperacaoInvalidaException, SQLException {

        for (Usuario usuario : UsuarioDAO.listar()) {
            if (usuario.getId() != idIgnorado &&
                usuario.getMatricula().equalsIgnoreCase(matricula.trim())) {

                throw new OperacaoInvalidaException(
                    "Já existe um usuário cadastrado com a matrícula " +
                    matricula.trim() + "."
                );
            }
        }
    }
}