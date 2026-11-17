package telas;

import java.util.ArrayList;
import java.util.Scanner;
import gerenciadores.GerenciadorEmprestimos;
import modelos.Livro;
import modelos.Usuario;

public class BibliotecaApp {
    
    private static ArrayList<Livro> livros = new ArrayList<>();
    private static ArrayList<Usuario> usuarios = new ArrayList<>();

    private static GerenciadorEmprestimos gerenciador = new GerenciadorEmprestimos();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int opcao;

        do {
            exibirMenu();
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    listarLivros();
                    break;

                case 2:
                    listarUsuarios();
                    break;

                case 3:
                    listarEmprestimos();
                    break;

                case 4:
                    emprestarLivro();
                    break;

                case 5:
                    devolverLivro();
                    break;

                case 0:
                    System.out.println("\nEncerrando o sistema...");
                    break;

                default:
                    System.out.println("\nOpção inválida. Tente novamente.\n");
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n===== Biblioteca =====");
        System.out.println("1 - Listar Livros");
        System.out.println("2 - Listar Usuários");
        System.out.println("3 - Listar Empréstimos");
        System.out.println("4 - Emprestar Livro");
        System.out.println("5 - Devolver Livro");
        System.out.println("0 - Sair");
        System.out.println("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        String linha = scanner.nextLine();
        try {
            return Integer.parseInt(linha.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String lerTexto(String mensagem) {
        while(true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("\nEsse campo não pode ficar vazio. Por favor, insira algo.\n");
        }
    }

    private static int lerInteiro(String mensagem) {
        while(true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linha.trim());
            } catch (NumberFormatException e) {
                System.out.println("\nPor favor, digite um número inteiro.\n");
            }
        }
    }

    private static void listarLivros() {
        if(livros.isEmpty()) {
            System.out.println("\nNenhum livro cadastrado.\n");
            return;
        }
        
        System.out.println("\n===== Livros Cadastrados =====");
        for (Livro livro: livros) {
            exibirLivro(livro);
        }
    }

    private static void exibirLivro(Livro livro) {
        System.out.println("ID: " + livro.getId() + " | Título: " + livro.getTitulo() + " | Autor: " + livro.getAutor() + 
        " | ISBN: " + livro.getIsbn() + " | Exemplares: " + livro.getExemplaresDisponiveis() + " | Total de Exemplares: " + livro.getTotalExemplares());
    }

    private static void listarUsuarios() {
    
    }

    private static void exibirUsuario(Usuario usuario) {

    }

    private static void emprestarLivro() {
    
    }

    private static void devolverLivro() {
    
    }

    private static void listarEmprestimos() {
    
    }
}