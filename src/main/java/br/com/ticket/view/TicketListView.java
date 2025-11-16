package br.com.ticket.view;

import br.com.ticket.model.Funcionario;
import br.com.ticket.model.TicketEntrega;
import br.com.ticket.service.FuncionarioService;
import br.com.ticket.service.TicketService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tela de listagem de tickets cadastrados, essa janela funciona como um painel administrativo
 * para consultar rapidamente os registros existentes.
 */
public class TicketListView extends JDialog {

    private JTable table;
    private DefaultTableModel tableModel;

    private TicketService ticketService = new TicketService();
    private FuncionarioService funcionarioService = new FuncionarioService();

    private Map<Integer, String> funcionarioMap = new HashMap<>();

    /**
     * Construtor da tela de listagem de tickets.
     * Inicializa tabelas, botões e eventos.
     */
    public TicketListView(JFrame parent) {
        super(parent, "Lista de Tickets", true);

        setSize(800, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Funcionário", "Quantidade", "Situação", "Data Entrega"}, 0
        );
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnEditar = new JButton("Editar Selecionado");
        JButton btnFechar = new JButton("Fechar");

        bottomPanel.add(btnAtualizar);
        bottomPanel.add(btnEditar);
        bottomPanel.add(btnFechar);

        add(bottomPanel, BorderLayout.SOUTH);

        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um ticket para editar.");
                return;
            }

            Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);

            try {
                TicketEntrega t = ticketService.buscarPorId(id);
                if (t == null) {
                    JOptionPane.showMessageDialog(this, "Ticket não encontrado!");
                    return;
                }

                TicketEditForm form = new TicketEditForm((JFrame) getParent(), t);
                form.setVisible(true);

                carregarTickets();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        // Ação: atualizar tabela
        btnAtualizar.addActionListener(e -> carregarTickets());

        // Ação: fechar janela
        btnFechar.addActionListener(e -> dispose());

        carregarTickets();
    }

    /**
     * Carrega tickets do banco e popula a tabela.
     * Também reconstrói o mapa de funcionários ID -> Nome.
     */
    private void carregarTickets() {
        try {
            funcionarioMap.clear();
            List<Funcionario> listaFunc = funcionarioService.listarTodos();
            for (Funcionario f : listaFunc) {
                funcionarioMap.put(f.getId(), f.getNome());
            }

            List<TicketEntrega> tickets = ticketService.listarTodos();

            tableModel.setRowCount(0);

            for (TicketEntrega t : tickets) {
                tableModel.addRow(new Object[]{
                        t.getId(),
                        funcionarioMap.get(t.getFuncionarioId()),
                        t.getQuantidade(),
                        t.getSituacao(),
                        t.getDataEntrega().toString()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar tickets: " + e.getMessage());
        }
    }
}
