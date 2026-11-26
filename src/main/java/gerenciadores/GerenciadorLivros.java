package gerenciadores;

import java.util.ArrayList;
import java.sql.SQLException;
import modelos.Livro;
import excecoes.*;
import dao.*;

public class GerenciadorLivros {
    public static ArrayList<Livro> getLivros() throws SQLException, RegistroNaoEncontradoException {
        return LivroDAO.listar();
    }

    public static Livro buscarLivro(int id) throws RegistroNaoEncontradoException, SQLException {
        return LivroDAO.buscarPorId(id);
    }

    public static ArrayList<Livro> buscarLivros(String termo) throws SQLException {
        ArrayList<Livro> livrosEncontrados = new ArrayList<>();
        String procurado = termo.trim().toLowerCase();
 
        for (Livro livro : LivroDAO.listar()) {
            if (livro.getTitulo().toLowerCase().contains(procurado) ||
                livro.getAutor().toLowerCase().contains(procurado)) {
                livrosEncontrados.add(livro);
            }
        }
        return livrosEncontrados;
    }

   public static Livro cadastrarLivro(String titulo, String autor, String isbn, int totalExemplares) throws OperacaoInvalidaException, SQLException {
        validarDados(titulo, autor, isbn);
 
        if (totalExemplares <= 0) {
            throw new OperacaoInvalidaException("O número de exemplares deve ser maior que zero.");
        }
 
        validarIsbnLivre(isbn, -1);
 
        Livro livro = new Livro(0, titulo.trim(), autor.trim(), isbn.trim(), totalExemplares);
        LivroDAO.cadastrar(livro);
        return livro;
    }

    public static void editarLivro(int id, String titulo, String autor, String isbn) throws RegistroNaoEncontradoException, OperacaoInvalidaException, SQLException {
        Livro livro = buscarLivro(id);
 
        validarDados(titulo, autor, isbn);
        validarIsbnLivre(isbn, id);
 
        livro.setTitulo(titulo.trim());
        livro.setAutor(autor.trim());
        livro.setIsbn(isbn.trim());
 
        LivroDAO.atualizar(livro);
    }

    public static void removerLivro(int id) throws RegistroNaoEncontradoException, OperacaoInvalidaException, SQLException {
        Livro livro = buscarLivro(id);
 
        if (livro.getExemplaresDisponiveis() < livro.getTotalExemplares()) {
            throw new OperacaoInvalidaException("Não é possível remover um livro que possui exemplares emprestados.");
        }
 
        LivroDAO.remover(id);
    }

    public static String listarLivros() throws SQLException {
        ArrayList<Livro> livros = LivroDAO.listar();
 
        if (livros.isEmpty()) {
            return "Ainda não há nenhum livro cadastrado.";
        }
 
        StringBuilder sb = new StringBuilder();
        sb.append("Lista de Livros:\n");
        for (Livro livro : livros) {
            sb.append(formatarLivro(livro));
        }
 
        return sb.toString();
    }

    public static String listarLivros(String termo) throws SQLException {
        ArrayList<Livro> livrosEncontrados = buscarLivros(termo);
 
        if (livrosEncontrados.isEmpty()) {
            return "Nenhum livro encontrado para \"" + termo + "\".\n\n" +
                   "Dica: Tente pesquisar pelo título ou autor do livro.";
        }
 
        StringBuilder sb = new StringBuilder();
        sb.append("Livros encontrados para \"").append(termo).append("\":\n\n");
        for (Livro livro : livrosEncontrados) {
            sb.append(formatarLivro(livro));
        }
 
        return sb.toString();
    }

    public static String formatarLivro(Livro livro) {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(livro.getId()).append("\n");
        sb.append("Título: ").append(livro.getTitulo()).append("\n");
        sb.append("Autor: ").append(livro.getAutor()).append("\n");
        sb.append("ISBN: ").append(livro.getIsbn()).append("\n");
        sb.append("Exemplares Disponíveis: ").append(livro.getExemplaresDisponiveis()).append("\n");
        sb.append("Total de Exemplares: ").append(livro.getTotalExemplares()).append("\n");
        sb.append("Situação: ").append(livro.getExemplaresDisponiveis() > 0 ? "Disponível" : "Indisponível").append("\n\n");
 
        return sb.toString();
    }

    private static void validarDados(String titulo, String autor, String isbn) throws OperacaoInvalidaException {
        if (titulo == null || titulo.isBlank() || autor == null || autor.isBlank() || isbn == null || isbn.isBlank()) {
            throw new OperacaoInvalidaException("Título, autor e ISBN são obrigatórios.");
        }
    }

    private static void validarIsbnLivre(String isbn, int idIgnorado) throws OperacaoInvalidaException, SQLException {
        for (Livro livro : LivroDAO.listar()) {
            if (livro.getId() != idIgnorado && livro.getIsbn().equalsIgnoreCase(isbn.trim())) {
                throw new OperacaoInvalidaException("Já existe um livro cadastrado com o ISBN " + isbn.trim() + ".");
            }
        }
    }
}