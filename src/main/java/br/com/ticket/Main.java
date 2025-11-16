package br.com.ticket;

import br.com.ticket.config.Conexao;
import br.com.ticket.view.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        //Chama o método que inicializa as tabelas do banco de dados
        Conexao.inicializarBanco();

        //Cria um frame com base no mainframe criado anteriormente
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
