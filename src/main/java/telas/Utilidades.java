package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Utilidades {

    public static void configurarTela(String titulo, JFrame tela, int largura, int altura, Point posicao, int estado) {
        JPanel painelTopo = new JPanel(new BorderLayout());
        JPanel linhaBotao = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JPanel linhaTitulo = new JPanel(new BorderLayout());

        JButton botaoVoltar = new JButton("←");

        JLabel imagemCoruja = new JLabel(carregarImagem("/imagens/logo_uabjoteca.png", 80, 80));
        JLabel imagemUfrpe = new JLabel(carregarImagem("/imagens/logo_ufrpe.png", 80, 80));

        JLabel textoTitulo = new JLabel(titulo, SwingConstants.CENTER);

        textoTitulo.setForeground(Color.WHITE);
        textoTitulo.setFont(new Font("Arial", Font.BOLD, 80));

        ajustarTamanho(tela, largura, altura, 950, 680, posicao, estado);
        tela.setLayout(new BorderLayout(0, 15));
        tela.add(painelTopo, BorderLayout.NORTH);
        tela.setTitle("UABJoteca: " + titulo);
        tela.setDefaultCloseOperation(EXIT_ON_CLOSE);
        tela.setIconImage(carregarImagem("/imagens/logo_uabjoteca.png", 64, 64).getImage());

        linhaBotao.add(botaoVoltar);
        linhaBotao.setOpaque(false);

        linhaTitulo.add(imagemCoruja, BorderLayout.WEST);
        linhaTitulo.add(textoTitulo);
        linhaTitulo.add(imagemUfrpe, BorderLayout.EAST);
        linhaTitulo.setOpaque(false);
        linhaTitulo.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        painelTopo.add(linhaBotao, BorderLayout.NORTH);
        painelTopo.add(linhaTitulo, BorderLayout.CENTER);

        painelTopo.setBackground(new Color(27, 45, 79));

        botaoVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TelaMenu menu = new TelaMenu(tela.getWidth(), tela.getHeight(), tela.getLocation(), tela.getExtendedState());
                menu.setVisible(true);
                tela.dispose();
            }
        });

        botaoVoltar.setFont(new Font("SansSerif", Font.BOLD, 22));
        botaoVoltar.setPreferredSize(new Dimension(50,30));
        botaoVoltar.setMargin(new Insets(0, 0, 0, 0));
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setFocusPainted(false);
        botaoVoltar.setBorderPainted(false);
        botaoVoltar.setContentAreaFilled(false);
        botaoVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static ImageIcon carregarImagem(String caminho, int largura, int altura) {
        ImageIcon icone = new ImageIcon(Utilidades.class.getResource(caminho));
        return new ImageIcon(icone.getImage().getScaledInstance(largura, altura, 4));
    }

    public static void ajustarTamanho(Window tela, int baseLargura, int baseAltura, int minLargura, int minAltura) {
        ajustarTamanho(tela, baseLargura, baseAltura, minLargura, minAltura, null, JFrame.NORMAL);
    }

    public static void ajustarTamanho(Window tela, int baseLargura, int baseAltura, int minLargura, int minAltura, Point posicao, int estado) {
        Dimension monitor = Toolkit.getDefaultToolkit().getScreenSize();
        double escala = monitor.width / 1920.0;

        int largura = Math.max((int) (baseLargura * escala), minLargura);
        int altura = Math.max((int) (baseAltura * escala), minAltura);

        tela.setSize(largura, altura);
        tela.setMinimumSize(new Dimension(minLargura, minAltura));

        if(estado == JFrame.MAXIMIZED_BOTH) {
            tela.setLocationRelativeTo(null);
            if (tela instanceof JFrame) {
                ((JFrame) tela).setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        } else {
            if (posicao == null) {
                tela.setLocationRelativeTo(null);
            } else {
                tela.setLocation(posicao);
            }
        }
    }

    public static JPanel criarCaixa(String titulo, JTextField campo) {
        JPanel bloco = new JPanel(new BorderLayout(0, 4));
        bloco.setOpaque(false);

        JLabel label = new JLabel(titulo);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));

        bloco.add(label, BorderLayout.NORTH);
        bloco.add(campo, BorderLayout.CENTER);

        return bloco;
    }

    public static void configurarMenuTabela(JTable tabela, JMenuItem... itens) {
        JPopupMenu menuContexto = new JPopupMenu();

        JMenuItem itemTitulo = new JMenuItem("ID: ");
        itemTitulo.setEnabled(false);
        itemTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));

        menuContexto.add(itemTitulo);

        for (JMenuItem item : itens) {
            menuContexto.add(item);
        }

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                verificarLinha(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                verificarLinha(e);
            }

            private void verificarLinha(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int linha = tabela.rowAtPoint(e.getPoint());

                    if (linha >= 0 && linha < tabela.getRowCount()) {
                        tabela.setRowSelectionInterval(linha, linha);

                        Object id = tabela.getValueAt(linha, 0);
                        itemTitulo.setText("ID: " + id);

                        menuContexto.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
    }

    public static boolean confirmarAcao(JFrame tela, String mensagem, String titulo) {
        int resposta = JOptionPane.showConfirmDialog(
                tela,
                mensagem,
                titulo,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return resposta == JOptionPane.YES_OPTION;
    }

    public static void mensagemErro(JFrame tela, String mensagem, String titulo) {
        JOptionPane.showMessageDialog(
                tela,
                mensagem,
                titulo,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void mensagemAviso(JFrame tela, String mensagem, String titulo) {
        JOptionPane.showMessageDialog(
                tela,
                mensagem,
                titulo,
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void mensagemInformacao(JFrame tela, String mensagem, String titulo) {
        JOptionPane.showMessageDialog(
                tela,
                mensagem,
                titulo,
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
