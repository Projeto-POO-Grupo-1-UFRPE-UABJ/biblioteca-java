package telas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.UsuarioDAO;
import gerenciadores.*;
import modelos.*;
import excecoes.*;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;

public class BibliotecaApp {
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

    private static void listarLivros() throws SQLException {
        ArrayList<Livro> livros = GerenciadorLivros.getLivros();
        if(livros.isEmpty()) {
            System.out.println("\nNenhum livro cadastrado.\n");
            return;
        }
        
        System.out.println("\n===== Livros Cadastrados =====");
        for (Livro livro: livros) {
            System.out.println(GerenciadorLivros.formatarLivro(livro));
            System.out.println("");
        }
    }

    private static void listarUsuarios() throws SQLException {
        ArrayList<Usuario> usuarios = UsuarioDAO.listar();
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


    private static void realizarEmprestimo() {
    try {
        System.out.println("\nRealizando Empréstimo: ");
        Usuario usuario = UsuarioDAO.buscarPorId(lerInteiro("Digite o ID do usuário: "));
        Livro livro = GerenciadorLivros.buscarLivro(lerInteiro("Digite o ID do livro: "));

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