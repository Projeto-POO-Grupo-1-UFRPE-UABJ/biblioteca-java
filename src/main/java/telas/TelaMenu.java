package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaMenu extends JFrame implements ActionListener {
    private final JPanel painelTopo = new JPanel(new BorderLayout());
    private final JPanel painelTitulo = new JPanel(new GridLayout(2, 1));
    private JPanel painelMeio = new JPanel(new GridLayout(2, 1));
    private JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));

    private JLabel textoBemVindo = new JLabel("Seja bem-vindo à", SwingConstants.CENTER);
    private JLabel textoUabjoteca = new JLabel("<html><font color='#cb3400'>U</font><font color='#003e89'>A</font><font color='#15ae2d'>B</font><font color='#91089e'>J</font>OTECA</html>", SwingConstants.CENTER);

    private JLabel textoInicial = new JLabel("Menu Inicial", SwingConstants.CENTER);

    private JLabel imagemCoruja = new JLabel(Utilidades.carregarImagem("/imagens/logo_uabjoteca.png", 150, 150));
    private JLabel imagemUfrpe = new JLabel(Utilidades.carregarImagem("/imagens/logo_ufrpe.png", 150, 150));

    private JButton botaoUsuarios = criarBotao("Usuários");
    private JButton botaoEmprestimos = criarBotao("Empréstimos");
    private JButton botaoLivros = criarBotao("Livros");

    public TelaMenu() {
        this.setTitle("UABJoteca");
        Utilidades.ajustarTamanho(this);
        this.setIconImage(Utilidades.carregarImagem("/imagens/logo_uabjoteca.png", 64, 64).getImage());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.add(painelTopo, BorderLayout.NORTH);
        this.add(painelMeio, BorderLayout.CENTER);

        textoBemVindo.setFont(new Font("Arial", Font.BOLD, 38));
        textoUabjoteca.setFont(new Font("Arial", Font.BOLD, 80));
        textoInicial.setFont(new Font("Arial", Font.BOLD, 30));

        textoBemVindo.setForeground(Color.BLACK);
        textoUabjoteca.setForeground(Color.BLACK);
        textoInicial.setForeground(Color.BLACK);


        painelTitulo.add(textoBemVindo);
        painelTitulo.add(textoUabjoteca);

        painelTopo.add(imagemCoruja, BorderLayout.WEST);
        painelTopo.add(painelTitulo, BorderLayout.CENTER);
        painelTopo.add(imagemUfrpe, BorderLayout.EAST);

        painelMeio.add(textoInicial);
        painelMeio.add(painelBotoes);

        painelBotoes.add(botaoUsuarios);
        painelBotoes.add(botaoEmprestimos);
        painelBotoes.add(botaoLivros);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == botaoUsuarios) {
            TelaUsuarios menuUsuarios = new TelaUsuarios();
            menuUsuarios.setVisible(true);
            this.dispose();

        } else if(e.getSource() == botaoEmprestimos) {
            TelaEmprestimos menuEmprestimos = new TelaEmprestimos();
            menuEmprestimos.setVisible(true);
            this.dispose();

        } else if(e.getSource() == botaoLivros) {
            TelaLivros menuLivros = new TelaLivros();
            menuLivros.setVisible(true);
            this.dispose();
        }
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);

        botao.setPreferredSize(new Dimension(190, 55));
        botao.setBackground(new Color(27, 45, 79));
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 20));
        botao.setFocusPainted(false);

        botao.addActionListener(this);
        return botao;
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.win.uiScaleX", "1.0");
        System.setProperty("sun.java2d.win.uiScaleY", "1.0");

        TelaLivros menu = new TelaLivros();
        menu.setVisible(true);
    }
}