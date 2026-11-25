package gerenciadores;

import java.util.ArrayList;
import modelos.Livro;
import excecoes.*;

public class GerenciadorLivros {
    private ArrayList<Livro> livros;


    public GerenciadorLivros() {
        this.livros = new ArrayList<>();
    }

    public GerenciadorLivros(ArrayList<Livro> livros) {
        this.livros = new ArrayList<>(livros);
    }

    public ArrayList<Livro> getLivros() {
        return new ArrayList<>(livros);
    }

    public Livro buscarLivro(int id) throws RegistroNaoEncontradoException {
        for (Livro livro : livros) {
            if (livro.getId() == id) {
                return livro;
            }
        }
        throw new RegistroNaoEncontradoException("Não há nenhum livro com o Id " + id + ".");
    }

    public ArrayList<Livro> buscarLivros(String termo) {
        ArrayList<Livro> livrosEncontrados = new ArrayList<>();
        String procurado = termo.trim().toLowerCase();

        for (Livro livro : livros) {
            if (livro.getTitulo().toLowerCase().contains(procurado) ||
                livro.getAutor().toLowerCase().contains(procurado)) {
                livrosEncontrados.add(livro);
            }
        }
        return livrosEncontrados;
    }

    public Livro cadastrarLivro(String titulo, String autor, String isbn, int totalExemplares) throws OperacaoInvalidaException {
        validarDados(titulo, autor, isbn);

        if (totalExemplares <= 0) {
            throw new OperacaoInvalidaException("O número de exemplares deve ser maior que zero.");
        }

        validarIsbnLivre(isbn, -1);

        Livro livro = new Livro(proximoId(), titulo.trim(), autor.trim(), isbn.trim(), totalExemplares);
        livros.add(livro);
        return livro;
    }

    public void editarLivro(int id, String titulo, String autor, String isbn) throws RegistroNaoEncontradoException, OperacaoInvalidaException {
        Livro livro = buscarLivro(id);

        validarDados(titulo, autor, isbn);
        validarIsbnLivre(isbn, id);

        livro.setTitulo(titulo.trim());
        livro.setAutor(autor.trim());
        livro.setIsbn(isbn.trim());
    }

    public void removerLivro(int id) throws RegistroNaoEncontradoException, OperacaoInvalidaException {
        Livro livro = buscarLivro(id);

        if (livro.getExemplaresDisponiveis() < livro.getTotalExemplares()) {
            throw new OperacaoInvalidaException("Não é possível remover um livro que possui exemplares emprestados.");
        }

        livros.remove(livro);
    }

    public String listarLivros() {
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

    public String listarLivros(String termo) {
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

    public String formatarLivro(Livro livro) {
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

    private int proximoId() {
        int maior = 0;
        for (Livro livro : livros) {
            if (livro.getId() > maior) {
                maior = livro.getId();
            }
        }
        return maior + 1;
    }

    private void validarDados(String titulo, String autor, String isbn) throws OperacaoInvalidaException {
        if (titulo == null || titulo.isBlank() || autor == null || autor.isBlank() || isbn == null || isbn.isBlank()) {
            throw new OperacaoInvalidaException("Título, autor e ISBN são obrigatórios.");
        }
    }

    private void validarIsbnLivre(String isbn, int idIgnorado) throws OperacaoInvalidaException {
        for (Livro livro : livros) {
            if (livro.getId() != idIgnorado && livro.getIsbn().equalsIgnoreCase(isbn.trim())) {
                throw new OperacaoInvalidaException("Já existe um livro cadastrado com o ISBN " + isbn.trim() + ".");
            }
        }
    }
}