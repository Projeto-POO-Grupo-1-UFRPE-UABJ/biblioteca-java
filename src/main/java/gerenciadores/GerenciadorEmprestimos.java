package gerenciadores;

import java.sql.SQLException;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.time.LocalDate;

import modelos.*;
import excecoes.*;
import dao.*;

public class GerenciadorEmprestimos {
    
    public static ArrayList<Emprestimo> getEmprestimos() throws SQLException, RegistroNaoEncontradoException {
        return EmprestimoDAO.listar();
    }

    public static Emprestimo buscarEmprestimo(int id) throws RegistroNaoEncontradoException, SQLException {
        return EmprestimoDAO.buscarPorId(id);
    }
    
    public static Emprestimo realizarEmprestimo(Usuario usuario, Livro livro) throws LivroIndisponivelException, LimiteEmprestimosException, SQLException {
        Emprestimo emprestimo = new Emprestimo(0, usuario, livro);
        LivroDAO.atualizar(emprestimo.getLivro());
        UsuarioDAO.atualizar(emprestimo.getUsuario());

        return EmprestimoDAO.cadastrar(emprestimo);
    }
    
    public static double encerrarEmprestimo(int id) throws RegistroNaoEncontradoException, EmprestimoEncerradoException, SQLException {
        return encerrarEmprestimo(id, LocalDate.now());
    }
    
    public static double encerrarEmprestimo(int id, LocalDate dataDevolucao) throws RegistroNaoEncontradoException, EmprestimoEncerradoException, SQLException {
        Emprestimo emprestimo = buscarEmprestimo(id);
        double multa = emprestimo.devolver(dataDevolucao);
        EmprestimoDAO.atualizar(emprestimo);
        LivroDAO.atualizar(emprestimo.getLivro());
        UsuarioDAO.atualizar(emprestimo.getUsuario());

        return multa;
    }
    
    public static String listarEmprestimos(FiltroEmprestimos filtro) throws SQLException, RegistroNaoEncontradoException {
        ArrayList<Emprestimo> emprestimos = EmprestimoDAO.listar();
        if (emprestimos.isEmpty()) {
            return "Ainda não há nenhum empréstimo cadastrado.\n\n";
        } else {
            StringBuilder sb = new StringBuilder();
            int encontrados = 0;

            switch(filtro) {
                case TODOS:
                	sb.append("Lista de todos os empréstimos:\n\n");
                	for(Emprestimo emprestimo : emprestimos) {
                		sb.append(formatarEmprestimo(emprestimo));
            		}
                	
                	break;
                
                case ATIVOS:
                	sb.append("Lista de todos os empréstimos ativos:\n\n");
                	for(Emprestimo emprestimo : emprestimos) {
                	    if(emprestimo.estaAberto()) {
                	    	sb.append(formatarEmprestimo(emprestimo));
                            encontrados++;
                	    }
            		}

                	if(encontrados == 0) {
                        sb.append("- Não há nenhum empréstimo ativo.\n\n");
                    }
                	break;
                
                case ENCERRADOS:
                	sb.append("Lista de todos os empréstimos encerrados:\n\n");
                	for(Emprestimo emprestimo : emprestimos) {
                		if(!emprestimo.estaAberto()) {
                	    	sb.append(formatarEmprestimo(emprestimo));
                            encontrados++;
                	    }
            		}
                	if(encontrados == 0) {
                        sb.append("- Não há nenhum empréstimo encerrado.\n\n");
                    }
                	break;

                case ATRASADOS:
                	sb.append("Lista de todos os empréstimos atrasados:\n\n");
                	for(Emprestimo emprestimo : emprestimos) {
                		if(emprestimo.estaAtrasado()) {
                	    	sb.append(formatarEmprestimo(emprestimo));
                            encontrados++;
                	    }
            		}
                	if(encontrados == 0) {
                        sb.append("- Não há nenhum empréstimo atrasado.\n\n");
                    }
                	break;

                default:
                    return "Insira um tipo de filtro válido. (TODOS, ATIVOS, ENCERRADOS, ATRASADOS)\n\n";
  
            }

            sb.append("\n");
            return sb.toString();
        }
    }
    
    public static String formatarEmprestimo(Emprestimo emprestimo) {
        StringBuilder sb = new StringBuilder();

        sb.append("Id: ").append(emprestimo.getId()).append("\n");
        sb.append("- Usuário: ").append(emprestimo.getUsuario().getNome()).append(" (").append(emprestimo.getUsuario().getTipo()).append(")\n");
        sb.append("- Id Usuário: ").append(emprestimo.getUsuario().getId()).append("\n");
        sb.append("- Matrícula Usuário: ").append(emprestimo.getUsuario().getMatricula()).append("\n");
        sb.append("- Livro: ").append(emprestimo.getLivro().getTitulo()).append("\n");
        sb.append("- Autor livro: ").append(emprestimo.getLivro().getAutor()).append("\n");
        sb.append("- ISBN livro: ").append(emprestimo.getLivro().getIsbn()).append("\n");
        sb.append("- Id Livro: ").append(emprestimo.getLivro().getId()).append("\n"); 
        sb.append("- Data do empréstimo: ").append(emprestimo.getDataEmprestimo()).append("\n");
		sb.append("- Data de devolução prevista: ").append(emprestimo.getDataPrevista()).append("\n");
        
		if (!emprestimo.estaAberto()) {
		    sb.append("- Data de devolução: ").append(emprestimo.getDataDevolucao()).append("\n");
		}

        if(emprestimo.estaAtrasado()) {
            sb.append(emprestimo.estaAberto() ? "- Este empréstimo está com " : "- Este empréstimo foi DEVOLVIDO com ").append(emprestimo.calcAtraso()).append(" dias de ATRASO.\n");
            sb.append("- Multa ").append(emprestimo.estaAberto() ? "acumulada (até o momento): R$ " : "total: R$ ").append(String.format("%.2f", emprestimo.calcMulta())).append("\n");
        } else {
            sb.append("- Este empréstimo NÃO está atrasado.\n");
        }
        
        sb.append("- Status: ").append((emprestimo.estaAberto() ? "ATIVO\n" : "ENCERRADO\n"));
        sb.append("\n");

        return sb.toString();
    }

    public static void apagarEmprestimo(int id) throws SQLException, RegistroNaoEncontradoException {
        Emprestimo emprestimo = buscarEmprestimo(id);
        EmprestimoDAO.remover(emprestimo.getId());
    }
}
