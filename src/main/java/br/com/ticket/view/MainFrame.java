package br.com.ticket.view;

import javax.swing.*;
import java.awt.*;

/**
 * Tela principal do sistema.
 */
public class MainFrame extends JFrame {

    /**
     * Construtor da janela principal.
     * Configura o layout, o menu lateral e os eventos dos botões.
     */
    public MainFrame() {
        setTitle("Ticket Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(240, 240, 240));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JButton btnFuncionarios = criarBotaoMenu("Cadastrar Funcionário");
        JButton btnListarFuncionarios = criarBotaoMenu("Listar Funcionários");
        JButton btnCadastrarTicket = criarBotaoMenu("Cadastrar Ticket");
        JButton btnListarTickets = criarBotaoMenu("Listar Tickets");
        JButton btnRelatorio = criarBotaoMenu("Relatório de Tickets");

        menu.add(btnFuncionarios);
        menu.add(btnListarFuncionarios);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnCadastrarTicket);
        menu.add(btnListarTickets);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnRelatorio);

        add(menu, BorderLayout.WEST);

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel mensagemBoasVindas = new JLabel(
                "<html><h1>Bem-vindo ao gerenciador de tickets</h1>"
                        + "<p>Selecione uma opção no menu à esquerda para começar.</p></html>"
        );
        mensagemBoasVindas.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(mensagemBoasVindas, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);


        btnFuncionarios.addActionListener(e -> {
            FuncionarioForm form = new FuncionarioForm(this);
            form.setVisible(true);
        });

        btnListarFuncionarios.addActionListener(e -> {
            FuncionarioListView listView = new FuncionarioListView(this);
            listView.setVisible(true);
        });

        btnCadastrarTicket.addActionListener(e -> {
            TicketForm form = new TicketForm(this);
            form.setVisible(true);
        });

        btnListarTickets.addActionListener(e -> {
            TicketListView view = new TicketListView(this);
            view.setVisible(true);
        });

        btnRelatorio.addActionListener(e -> {
            RelatorioTicketsView rel = new RelatorioTicketsView(this);
            rel.setVisible(true);
        });
    }

    /**
     * Cria um botão estilizado para o menu lateral.
     */
    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 35));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(220, 220, 220));
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return btn;
    }
}
