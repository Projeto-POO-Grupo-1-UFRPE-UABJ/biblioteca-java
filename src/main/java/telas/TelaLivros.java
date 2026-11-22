package telas;

import dao.LivroDAO;
import modelos.Livro;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class TelaLivros extends JFrame implements ActionListener {

    LivroDAO livroDAO = new LivroDAO();
    JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
    JPanel painelTabela = new JPanel(new BorderLayout(15, 15));

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

    private JTextField campoTitulo = new JTextField(80);
    private JTextField campoAutor = new JTextField(40);
    private JTextField campoISBN = new JTextField(20);
    private JTextField campoExemplares = new JTextField(5);

    public TelaLivros() throws SQLException {
        Utilidades.configurarTela("Livros", this);

        this.add(painelBotoes, BorderLayout.CENTER);
        this.add(painelTabela, BorderLayout.SOUTH);

        painelBotoes.add(Utilidades.criarCaixa("Título", campoTitulo));
        painelBotoes.add(Utilidades.criarCaixa("Autor", campoAutor));
        painelBotoes.add(Utilidades.criarCaixa("ISBN", campoISBN));
        painelBotoes.add(Utilidades.criarCaixa("Estoque", campoExemplares));

        painelTabela.add(scroll);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(45);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(350);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(350);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(220);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(85);

        tabela.getColumnModel().getColumn(0).setMaxWidth(60);
        tabela.getColumnModel().getColumn(4).setMaxWidth(90);
        tabela.getColumnModel().getColumn(5).setMaxWidth(100);

        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(5).setCellRenderer(centralizado);

        tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Evita disparar duas vezes no mesmo clique
                if (!e.getValueIsAdjusting()) {
                    int linha = tabela.getSelectedRow();

                    // Garante que alguma linha válida está selecionada
                    if (linha != -1) {
                        int id = (int) modeloTabela.getValueAt(linha, 0);
                        String titulo = modeloTabela.getValueAt(linha, 1).toString();
                        String autor = modeloTabela.getValueAt(linha, 2).toString();
                        String isbn = modeloTabela.getValueAt(linha, 3).toString();
                        String estoque = modeloTabela.getValueAt(linha, 4).toString();

                        campoTitulo.setText(titulo);
                        campoAutor.setText(autor);
                        campoISBN.setText(isbn);
                        campoExemplares.setText(estoque);
                    }
                }
            }
        });

        atualizarTabela(livroDAO.listar());
    }


    @Override
    public void actionPerformed(ActionEvent e) {

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
}
