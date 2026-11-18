package telas;

import java.util.ArrayList;
import java.util.Scanner;
import gerenciadores.GerenciadorEmprestimos;
import modelos.Aluno;
import modelos.Livro;
import modelos.Professor;
import modelos.Usuario;
import java.time.LocalDate;
import gerenciadores.FiltroEmprestimos;
import java.util.List;

public class BibliotecaApp {
    
     private static ArrayList<Usuario> usuarios = new ArrayList<>(List.of(
        new Aluno(1, "Ana", "2026001"),
        new Aluno(2, "Bruno", "2026002"),
        new Aluno(3, "Carla", "2025001"),
        new Professor(4, "Marcos", "P001"),
        new Professor(5, "Lucia", "P002")
    ));

    private static ArrayList<Livro> livros = new ArrayList<>(List.of(
        new Livro(1, "Java", "Deitel", "111", 5),
        new Livro(2, "POO", "Kalb", "222", 4),
        new Livro(3, "Banco de Dados", "Date", "333", 3),
        new Livro(4, "UML", "Fowler", "444", 1)
    ));


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
                    realizarEmprestimo();
                    break;

                case 5:
                    encerrarEmprestimo();
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
        System.out.println("4 - Realizar Empréstimo");
        System.out.println("5 - Encerrar Empréstimo");
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
        if(usuarios.isEmpty()) {
            System.out.println("\nNenhum usuário cadastrado.\n");
            return;
        }

        System.out.println("\n===== Usuários Cadastrados =====");
        for (Usuario usuario: usuarios) {
            exibirUsuario(usuario);
        }
    }

    private static void exibirUsuario(Usuario usuario) {
        System.out.println("ID: " + usuario.getId() + " | Nome: " + usuario.getNome() + " | Matrícula: " + usuario.getMatricula());
    }

    private static Usuario buscarUsuario(int idUsuario) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == idUsuario) {
                return usuario;
            }
        }
        System.out.println("\nUsuário não encontrado.\n");
        return null;
    }

    private static Livro buscarLivro(int idLivro) {
        for (Livro livro : livros) {
            if (livro.getId() == idLivro) {
                return livro;
            }
        }
        System.out.println("\nLivro não encontrado.\n");
        return null;
    }
       
    private static void realizarEmprestimo() {
        try {
            int idUsuario = lerInteiro("Digite o ID do usuário: ");
            int idLivro = lerInteiro("Digite o ID do livro: ");

            Usuario usuario = buscarUsuario(idUsuario);
            Livro livro = buscarLivro(idLivro);

            if (usuario != null && livro != null) {
                gerenciador.realizarEmprestimo(usuario, livro);
            }
        } catch (Exception e) {
            System.out.println("\nErro ao realizar empréstimo: " + e.getMessage() + "\n");
        }
    }

    private static void encerrarEmprestimo() {
        try {
            int idEmprestimo = lerInteiro("Digite o ID do empréstimo: ");

            System.out.print("A devolução foi feita hoje? (s/n): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("s")) {
                gerenciador.encerrarEmprestimo(idEmprestimo);
            } else if (resposta.equals("n")) {
                String dataDevolucaoStr = lerTexto("Digite a data de devolução (YYYY-MM-DD): ");
                LocalDate dataDevolucao = LocalDate.parse(dataDevolucaoStr);
                gerenciador.encerrarEmprestimo(idEmprestimo, dataDevolucao);
            } else {
                System.out.println("\nOpção inválida. Por favor, digite 's' ou 'n'.\n");
            }


        } catch (Exception e) {
            System.out.println("\nErro ao encerrar empréstimo: " + e.getMessage() + "\n");
        }
    }

    private static void listarEmprestimos() {

        switch (lerInteiro("Deseja filtrar os empréstimos? (1 - Todos, 2 - Ativos, 3 - Encerrados, 4 - Atrasados): ")) {
            case 1:
                System.out.println(gerenciador.listarEmprestimos(FiltroEmprestimos.TODOS));
                break;
            case 2:
                System.out.println(gerenciador.listarEmprestimos(FiltroEmprestimos.ATIVOS));
                break;
            case 3:
                System.out.println(gerenciador.listarEmprestimos(FiltroEmprestimos.ENCERRADOS));
                break;

            case 4:
                System.out.println(gerenciador.listarEmprestimos(FiltroEmprestimos.ATRASADOS));
                break;
            default:
                System.out.println("\nOpção inválida. Mostrando todos os empréstimos.\n");
                System.out.println(gerenciador.listarEmprestimos(FiltroEmprestimos.TODOS));
        }
    }
}