package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaEmprestimos extends JFrame implements ActionListener {

    public TelaEmprestimos(int largura, int altura, Point posicao, int estado) {
        Utilidades.configurarTela("Empréstimos", this, largura, altura, posicao, estado);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
