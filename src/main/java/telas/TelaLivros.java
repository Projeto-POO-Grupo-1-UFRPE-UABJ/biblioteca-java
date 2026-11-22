package telas;

import dao.LivroDAO;
import excecoes.RegistroNaoEncontradoException;
import modelos.Livro;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class TelaLivros extends JFrame implements ActionListener {
    LivroDAO livroDAO = new LivroDAO();

    JPanel linha1Caixas = new JPanel(new BorderLayout(5, 0));
    JPanel linha2Caixas = new JPanel(new BorderLayout(5, 0));
    JPanel linha2DireitaCaixas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
    JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
    JPanel painelMeio = new JPanel(new GridLayout(3, 1, 0, 5));
    JPanel painelTabela = new JPanel(new BorderLayout(15, 15));
    JPanel painelConteudo = new JPanel(new BorderLayout(0, 10));

    String[] colunas = {"ID", "Título", "Autor", "ISBN", "Estoque", "Disponíveis"};
    DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
    JTable tabela = new JTable(modeloTabela);
    JScrollPane scroll = new JScrollPane(tabela);

    private final JTextField campoTitulo = new JTextField(80);
    private final JTextField campoAutor = new JTextField(40);
    private final JTextField campoISBN = new JTextField(20);
    private final JTextField campoExemplares = new JTextField(5);

    private final JMenuItem itemEditar = new JMenuItem("Editar");
    private final JMenuItem itemRemover = new JMenuItem("Remover");
    private final JMenuItem itemListarEmprestimos = new JMenuItem("Listar Empréstimos");

    private final JButton botaoCadastrar = new JButton("Cadastrar");
    private final JButton botaoLimpar = new JButton("Limpar");
    private final JButton botaoBuscar = new JButton("Buscar");

    public TelaLivros() throws SQLException {
        this(1150, 800, null, JFrame.NORMAL);
    }

    public TelaLivros(int largura, int altura, Point posicao, int estado) throws SQLException {
        Utilidades.configurarTela("Livros", this, largura, altura, posicao, estado);

        this.add(painelConteudo, BorderLayout.CENTER);
        painelConteudo.add(new JPanel(), BorderLayout.NORTH);
        painelConteudo.add(painelMeio, BorderLayout.NORTH);
        painelConteudo.add(painelTabela, BorderLayout.CENTER);

        linha1Caixas.add(new JPanel(), BorderLayout.WEST);
        linha1Caixas.add(Utilidades.criarCaixa("Título", campoTitulo), BorderLayout.CENTER);
        linha1Caixas.add(new JPanel(), BorderLayout.EAST);

        linha2Caixas.add(new JPanel(), BorderLayout.WEST);
        linha2Caixas.add(Utilidades.criarCaixa("Autor", campoAutor), BorderLayout.CENTER);
        linha2DireitaCaixas.add(Utilidades.criarCaixa("ISBN", campoISBN));
        linha2DireitaCaixas.add(Utilidades.criarCaixa("Estoque", campoExemplares));
        linha2Caixas.add(linha2DireitaCaixas, BorderLayout.EAST);

        painelBotoes.add(botaoCadastrar);
        botaoCadastrar.addActionListener(this);
        painelBotoes.add(botaoLimpar);
        botaoLimpar.addActionListener(this);
        painelBotoes.add(botaoBuscar);
        botaoBuscar.addActionListener(this);

        painelMeio.add(linha1Caixas);
        painelMeio.add(linha2Caixas);
        painelMeio.add(painelBotoes);

        painelTabela.add(scroll);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(45);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(450);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(250);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(220);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(85);

        tabela.getColumnModel().getColumn(0).setMaxWidth(60);
        tabela.getColumnModel().getColumn(4).setMaxWidth(90);
        tabela.getColumnModel().getColumn(5).setMaxWidth(100);

        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(5).setCellRenderer(centralizado);

        tabela.setRowHeight(28);
        tabela.setIntercellSpacing(new Dimension(8, 1));
        atualizarTabela(livroDAO.listar());

        itemEditar.addActionListener(this);
        itemRemover.addActionListener(this);
        itemListarEmprestimos.addActionListener(this);

        Utilidades.configurarMenuTabela(tabela, itemEditar, itemRemover, itemListarEmprestimos);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass() == JMenuItem.class) {
            int linha = tabela.getSelectedRow();
            if (linha == -1) {
                return;
            }

            int idLivro = (int) modeloTabela.getValueAt(linha, 0);

            if(e.getSource() == itemEditar) {
                editar(idLivro);

            } else if(e.getSource() == itemRemover) {
                if(Utilidades.confirmarAcao(this, "Tem certeza que deseja apagar o livro de ID " + idLivro + "?", "Confirmar Exclusão")) {
                    try {
                        remover(idLivro);
                    } catch (SQLException ex) {
                        if (ex.getMessage() != null && ex.getMessage().contains("FOREIGN KEY constraint failed")) {
                            Utilidades.mensagemAviso(
                                    this,
                                    "Este livro não pode ser removido porque está vinculado a um empréstimo registrado.",
                                    "Operação não permitida"
                            );
                        } else {
                            Utilidades.mensagemErro(
                                    this,
                                    "Erro ao conectar com o banco de dados: " + ex.getMessage(),
                                    "Erro de Conexão"
                            );
                        }
                    }
                }
            } else if(e.getSource() == itemListarEmprestimos) {
                //todo: listar emrpestimos que tem o livro
                System.out.println("lista dos emprestimos do livro tal");

            }
        }

        if (e.getSource() == botaoCadastrar){
            //todo: implementar método de cadastramento
            System.out.println("cadastrando o livro tal...");

        } else if (e.getSource() == botaoLimpar) {
            campoTitulo.setText("");
            campoAutor.setText("");
            campoISBN.setText("");
            campoExemplares.setText("");

        } else if (e.getSource() == botaoBuscar) {
            //todo: implementar método de busca que vai voltar a linha selecionada
            // por meio daquele método lá tabela.setRowSelectionInterval
            System.out.println("abrindo popup para inserir informações para retornar");
        }
    }

    private void atualizarTabela(ArrayList<Livro> listaLivros) {
        modeloTabela.setRowCount(0);

        for (Livro livro : listaLivros) {
            Object[] linha = {
                    livro.getId(),
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getIsbn(),
                    livro.getTotalExemplares(),
                    livro.getExemplaresDisponiveis()
            };
            modeloTabela.addRow(linha);
        }
    }

    private void cadastrar(Livro livro) throws SQLException {
        // TODO: implemntar a logica de cadastro quando o GerenciadorLivros for criado
        //  atualizar o resto tbm para deixar de usar o DAO diretamente
        atualizarTabela(livroDAO.listar());
    }

    private void remover(int id) throws SQLException {
        livroDAO.remover(id);
        Utilidades.mensagemInformacao(this, "O livro foi removido com sucesso!", "SUCESSO");
    }

    private void editar(int idLivro) {
        int linha = tabela.getSelectedRow();

        if (linha != -1) {
            String titulo = modeloTabela.getValueAt(linha, 1).toString();
            String autor = modeloTabela.getValueAt(linha, 2).toString();
            String isbn = modeloTabela.getValueAt(linha, 3).toString();
            String estoque = modeloTabela.getValueAt(linha, 4).toString();

            JDialog popup = new JDialog(this, "Editando livro de ID " + idLivro, true);
            Utilidades.ajustarTamanho(popup, 500, 380, 420, 320);
            popup.setLocationRelativeTo(this);
            popup.setLayout(new BorderLayout(10, 10));

            JTextField campoTitulo = new JTextField(titulo);
            JTextField campoAutor = new JTextField(autor);
            JTextField campoISBN = new JTextField(isbn);
            JTextField campoEstoque = new JTextField(String.valueOf(estoque));

            JPanel campos = new JPanel(new GridLayout(4, 1, 0, 8));
            campos.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
            campos.add(Utilidades.criarCaixa("Título", campoTitulo));
            campos.add(Utilidades.criarCaixa("Autor", campoAutor));
            campos.add(Utilidades.criarCaixa("ISBN", campoISBN));
            campos.add(Utilidades.criarCaixa("Estoque", campoEstoque));

            JButton botaoSalvar = new JButton("Salvar");
            JButton botaoCancelar = new JButton("Cancelar");

            JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

            botoes.add(botaoSalvar);
            botoes.add(botaoCancelar);

            botaoCancelar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    popup.dispose();
                }
            });

            botaoSalvar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        //todo: falta verificar se o novo estoque é menor que os disponiveis para não acabar dando problema
                        int disponiveis = livroDAO.buscarPorId(idLivro).getExemplaresDisponiveis();
                        livroDAO.atualizar(new Livro(idLivro, campoTitulo.getText(), campoAutor.getText(), campoISBN.getText(), Integer.parseInt(campoEstoque.getText()), disponiveis));
                        atualizarTabela(livroDAO.listar());
                        popup.dispose();
                        Utilidades.mensagemInformacao(TelaLivros.this, "Livro atualizado com sucesso!", "Sucesso");

                    } catch (SQLException ex) {
                        Utilidades.mensagemErro(TelaLivros.this, "Erro ao atualizar: " + ex.getMessage(), "Erro");

                    } catch (RegistroNaoEncontradoException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            popup.add(campos, BorderLayout.CENTER);
            popup.add(botoes, BorderLayout.SOUTH);
            popup.setVisible(true);
        }
    }

}
