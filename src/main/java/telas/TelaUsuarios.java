package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaUsuarios extends JFrame implements ActionListener {

    public TelaUsuarios(int largura, int altura, Point posicao, int estado) {
        Utilidades.configurarTela("Usuários", this, largura, altura, posicao, estado);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
