package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Utilidades {

    public static void configurarTela(String titulo, JFrame tela) {
        JPanel painelTopo = new JPanel(new BorderLayout());
        JPanel linhaBotao = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JPanel linhaTitulo = new JPanel(new BorderLayout());

        JButton botaoVoltar = new JButton("←");

        JLabel imagemCoruja = new JLabel(carregarImagem("/imagens/logo_uabjoteca.png", 80, 80));
        JLabel imagemUfrpe = new JLabel(carregarImagem("/imagens/logo_ufrpe.png", 80, 80));

        JLabel textoTitulo = new JLabel(titulo, SwingConstants.CENTER);

        textoTitulo.setForeground(Color.WHITE);
        textoTitulo.setFont(new Font("Arial", Font.BOLD, 80));

        ajustarTamanho(tela, 1150, 800, 950, 680);
        tela.add(painelTopo, BorderLayout.NORTH);
        tela.setTitle("UABJoteca: " + titulo);
        tela.setLocationRelativeTo(null);
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
                TelaMenu menu = new TelaMenu();
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

    public static void ajustarTamanho(JFrame tela) {
        ajustarTamanho(tela, 950, 620, 860, 500);
    }

    public static void ajustarTamanho(JFrame tela, int baseLargura, int baseAltura, int minLargura, int minAltura) {
        Dimension monitor = Toolkit.getDefaultToolkit().getScreenSize();
        double escala = monitor.width / 1920.0;

        int largura = Math.max((int) (baseLargura * escala), minLargura);
        int altura = Math.max((int) (baseAltura * escala), minAltura);

        tela.setSize(largura, altura);
        tela.setMinimumSize(new Dimension(largura, altura));
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
}
