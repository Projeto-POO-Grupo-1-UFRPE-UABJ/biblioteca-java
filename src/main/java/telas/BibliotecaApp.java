package telas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import gerenciadores.*;
import modelos.*;
import excecoes.*;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.util.List;

public class BibliotecaApp {

    private static ArrayList<Usuario> usuarios = new ArrayList<>(List.of(
        new Aluno(1, "Ana Beatriz Silva", "20241100123"),
        new Aluno(2, "Bruno Henrique Costa", "20241100456"),
        new Aluno(3, "Carla Maria Souza", "20231100789"),
        new Aluno(4, "Diego Albuquerque Lima", "20251100234"),
        new Professor(5, "Marcos Vinícius Cavalcanti", "2345678"),
        new Professor(6, "Lúcia Helena Barros", "3456789")
    ));

    private static ArrayList<Livro> livros = new ArrayList<>(List.of(
        new Livro(1, "Cálculo, Volume 1", "James Stewart", "978-85-2161-042-7", 4),
        new Livro(2, "Física Básica, Volume 1: Mecânica", "H. Moysés Nussenzveig", "978-85-2120-118-2", 3),
        new Livro(3, "Algoritmos: Teoria e Prática", "Thomas H. Cormen", "978-85-3520-077-5", 3),
        new Livro(4, "Java: Como Programar", "Paul Deitel", "978-85-7605-215-9", 5),
        new Livro(5, "Dom Casmurro", "Machado de Assis", "978-85-3591-103-9", 2),
        new Livro(6, "Vidas Secas", "Graciliano Ramos", "978-85-0101-506-8", 3),
        new Livro(7, "Sapiens: Uma Breve História da Humanidade", "Yuval Noah Harari", "978-85-3591-220-3", 2),
        new Livro(8, "Casa-Grande & Senzala", "Gilberto Freyre", "978-85-2602-031-3", 1),
        new Livro(9, "Fisiologia Vegetal", "Lincoln Taiz e Eduardo Zeiger", "978-85-8271-154-5", 4),
        new Livro(10, "Redes de Computadores", "Andrew S. Tanenbaum", "978-85-3520-092-8", 3)
    ));

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException, RegistroNaoEncontradoException {

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
        System.out.print("Escolha uma opção: ");
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
            System.out.println("");
        }
    }

    private static void exibirLivro(Livro livro) {
        System.out.println("ID: " + livro.getId() + " | Título: " + livro.getTitulo() + " | Autor: " + livro.getAutor() + 
        " | ISBN: " + livro.getIsbn());
        System.out.println(" | Exemplares Disponíveis: " + livro.getExemplaresDisponiveis() + " | Total de Exemplares: " + livro.getTotalExemplares());
    }

    private static void listarUsuarios() {
        if(usuarios.isEmpty()) {
            System.out.println("\nNenhum usuário cadastrado.\n");
            return;
        }

        System.out.println("\n===== Usuários Cadastrados =====");
        for (Usuario usuario: usuarios) {
            exibirUsuario(usuario);
            System.out.println("");
        }
    }

    private static void exibirUsuario(Usuario usuario) {
        System.out.println("ID: " + usuario.getId() + " | " + usuario.getTipo() + ": " + usuario.getNome()
    	+ " | Matrícula: " + usuario.getMatricula());
    	System.out.println(" | Prazo: " + usuario.getPrazo() + " dias | Multa: R$ " + String.format("%.2f", usuario.getTaxa())
    	+ " por dia | Empréstimos: " + usuario.getLimite() + "/" + usuario.getLimiteMaximo());
    }

    private static Usuario buscarUsuario(int id) throws RegistroNaoEncontradoException {
	    for (Usuario usuario : usuarios) {
	        if (usuario.getId() == id) return usuario;
	    }
	    throw new RegistroNaoEncontradoException("Não há nenhum usuário com o Id " + id + ".");
	}

    private static Livro buscarLivro(int id) throws RegistroNaoEncontradoException {
	    for (Livro livro : livros) {
	        if (livro.getId() == id) return livro;
	    }
	    throw new RegistroNaoEncontradoException("Não há nenhum livro com o Id " + id + ".");
	}

    private static void realizarEmprestimo() {
    try {
        System.out.println("\nRealizando Empréstimo: ");
        Usuario usuario = buscarUsuario(lerInteiro("Digite o ID do usuário: "));
        Livro livro = buscarLivro(lerInteiro("Digite o ID do livro: "));

        Emprestimo e = GerenciadorEmprestimos.realizarEmprestimo(usuario, livro);
        System.out.println("\nEmpréstimo " + e.getId() + " realizado. Devolver até " + e.getDataPrevista() + ".\n");
    } catch (LimiteEmprestimosException | LivroIndisponivelException | RegistroNaoEncontradoException e) {
        System.out.println("\nNão foi possível emprestar:\n" + e.getMessage() + "\n");
    } catch (SQLException e) {
        System.out.println("\nErro ao conectar ao banco de dados: " + e.getMessage() + "\n");
    }
    }

	private static void encerrarEmprestimo() {
	    try {
	        System.out.println("\nEncerrando Empréstimo: ");
	        Emprestimo emprestimo = GerenciadorEmprestimos.buscarEmprestimo(lerInteiro("Digite o ID do empréstimo: "));
	        int id = emprestimo.getId();
	        
	        if (!emprestimo.estaAberto()) {
	            throw new EmprestimoEncerradoException("Não é possível encerrar este empréstimo pois ele já foi encerrado anteriormente na data: " + emprestimo.getDataDevolucao());
	        }
	        
	        System.out.println("\nEmpréstimo de Id " + id + " encontrado:");
	        System.out.println("\nUsuário: " + emprestimo.getUsuario().getNome() 
		    + " | Livro: " + emprestimo.getLivro().getTitulo() 
		    + " | Data prevista: " + emprestimo.getDataPrevista() + "\n");
	        
	        String resposta;
			do {
			    resposta = lerTexto("A devolução foi feita hoje? (s/n) ou 'c' para cancelar: ");
			    if (!resposta.equalsIgnoreCase("s") && !resposta.equalsIgnoreCase("n") && !resposta.equalsIgnoreCase("c")) {
			        System.out.println("\nOpção inválida!\nDigite 's', 'n' ou 'c'.\n");
			    }
			} while (!resposta.equalsIgnoreCase("s") && !resposta.equalsIgnoreCase("n") && !resposta.equalsIgnoreCase("c"));
			
			if (resposta.equalsIgnoreCase("c")) {
			    System.out.println("\nOperação cancelada.\n");
			    return;
			}
			
			double multa;
			if (resposta.equalsIgnoreCase("s")) {
			    multa = GerenciadorEmprestimos.encerrarEmprestimo(id);
			} else {
			    LocalDate data = LocalDate.parse(lerTexto("Data de devolução (AAAA-MM-DD): "));
			    multa = GerenciadorEmprestimos.encerrarEmprestimo(id, data);
			}
	
	        System.out.println("\nEmpréstimo encerrado. " +
	            (multa > 0.0 ? String.format("Multa: R$ %.2f%n", multa) : "Sem multa.\n"));
	
	    } catch (RegistroNaoEncontradoException | EmprestimoEncerradoException | IllegalArgumentException e) {
	        System.out.println("\nNão foi possível encerrar:\n" + e.getMessage() + "\n");
	    } catch (DateTimeParseException e) {
	        System.out.println("\nData inválida.\nUse o formato AAAA-MM-DD.\n");
	    } catch (SQLException e) {
            System.out.println("\nErro ao conectar ao banco de dados: " + e.getMessage() + "\n");
        }
    }


    private static void listarEmprestimos() throws SQLException, RegistroNaoEncontradoException {
		System.out.println("");
        
        switch (lerInteiro("Deseja filtrar os empréstimos?\n(1 - Todos, 2 - Ativos, 3 - Encerrados, 4 - Atrasados): ")) {
            case 1:
            	System.out.println("");
                System.out.println(GerenciadorEmprestimos.listarEmprestimos(FiltroEmprestimos.TODOS));
                break;
            case 2:
            	System.out.println("");
                System.out.println(GerenciadorEmprestimos.listarEmprestimos(FiltroEmprestimos.ATIVOS));
                break;
            case 3:
            	System.out.println("");
                System.out.println(GerenciadorEmprestimos.listarEmprestimos(FiltroEmprestimos.ENCERRADOS));
                break;
            case 4:
            	System.out.println("");
                System.out.println(GerenciadorEmprestimos.listarEmprestimos(FiltroEmprestimos.ATRASADOS));
                break;
            default:
                System.out.println("\nOpção inválida. Mostrando todos os empréstimos.\n");
                System.out.println(GerenciadorEmprestimos.listarEmprestimos(FiltroEmprestimos.TODOS));
        }
    }
}