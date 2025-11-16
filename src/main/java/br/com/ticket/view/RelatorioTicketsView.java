package br.com.ticket.view;

import br.com.ticket.model.Funcionario;
import br.com.ticket.model.TicketEntrega;
import br.com.ticket.service.FuncionarioService;
import br.com.ticket.service.TicketService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

/**
 * Tela de geração de relatório de tickets num período.
 * Permite filtrar os registros por intervalo de datas.
 */
public class RelatorioTicketsView extends JDialog {

    private JSpinner txtDataInicio;
    private JSpinner txtDataFim;

    private JTable tableDetalhes;
    private JTable tableTotais;

    private DefaultTableModel modelDetalhes;
    private DefaultTableModel modelTotais;

    private JLabel lblTotalGeral;

    private TicketService ticketService = new TicketService();
    private FuncionarioService funcionarioService = new FuncionarioService();

    private Map<Integer, String> funcionarioMap = new HashMap<>();

    /**
     * Construtor da tela de relatório.
     * Monta os componentes visuais, configura as tabelas e eventos de busca.
     */
    public RelatorioTicketsView(JFrame parent) {
        super(parent, "Relatório de Tickets por Período", true);

        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel filtroPanel = new JPanel(new FlowLayout());

        filtroPanel.add(new JLabel("Data início:"));
        txtDataInicio = new JSpinner(new SpinnerDateModel());
        txtDataInicio.setEditor(new JSpinner.DateEditor(txtDataInicio, "yyyy-MM-dd"));
        filtroPanel.add(txtDataInicio);

        filtroPanel.add(new JLabel("Data fim:"));
        txtDataFim = new JSpinner(new SpinnerDateModel());
        txtDataFim.setEditor(new JSpinner.DateEditor(txtDataFim, "yyyy-MM-dd"));
        filtroPanel.add(txtDataFim);

        JButton btnBuscar = new JButton("Buscar");
        filtroPanel.add(btnBuscar);

        add(filtroPanel, BorderLayout.NORTH);

        modelDetalhes = new DefaultTableModel(
                new Object[]{"ID", "Funcionário", "Quantidade", "Situação", "Data"}, 0
        );
        tableDetalhes = new JTable(modelDetalhes);

        modelTotais = new DefaultTableModel(
                new Object[]{"Funcionário", "Total"}, 0
        );
        tableTotais = new JTable(modelTotais);

        JPanel center = new JPanel(new GridLayout(2, 1, 10, 10));
        center.add(new JScrollPane(tableDetalhes));
        center.add(new JScrollPane(tableTotais));

        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalGeral = new JLabel("Total geral: 0");
        bottom.add(lblTotalGeral);

        JButton btnFechar = new JButton("Fechar");
        bottom.add(btnFechar);

        add(bottom, BorderLayout.SOUTH);

        btnFechar.addActionListener(e -> dispose());
        btnBuscar.addActionListener(e -> buscar());

        carregarFuncionarios();
    }

    /**
     * Carrega todos os funcionários para um mapa auxiliar,
     * usado para converter IDs em nomes dentro das tabelas.
     */
    private void carregarFuncionarios() {
        try {
            funcionarioMap.clear();
            List<Funcionario> funcionarios = funcionarioService.listarTodos();
            for (Funcionario f : funcionarios) {
                funcionarioMap.put(f.getId(), f.getNome());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar funcionários.");
        }
    }

    /**
     * Executa a busca de tickets dentro do período informado.
     * Atualiza tanto a tabela detalhada quanto o resumo de totais.
     */
    private void buscar() {
        try {
            LocalDate dInicio = ((java.util.Date) txtDataInicio.getValue()).toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            LocalDate dFim = ((java.util.Date) txtDataFim.getValue()).toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            LocalDateTime inicio = dInicio.atStartOfDay();
            LocalDateTime fim = dFim.atTime(23, 59, 59);

            List<TicketEntrega> tickets =
                    ticketService.relatorioPorPeriodo(inicio, fim);

            modelDetalhes.setRowCount(0);
            int totalGeral = 0;

            for (TicketEntrega t : tickets) {
                modelDetalhes.addRow(new Object[]{
                        t.getId(),
                        funcionarioMap.get(t.getFuncionarioId()),
                        t.getQuantidade(),
                        t.getSituacao(),
                        t.getDataEntrega()
                });

                totalGeral += t.getQuantidade();
            }

            Map<Integer, Integer> mapaTotais =
                    ticketService.somarPorFuncionario(inicio, fim);

            modelTotais.setRowCount(0);

            for (Map.Entry<Integer, Integer> entry : mapaTotais.entrySet()) {
                modelTotais.addRow(new Object[]{
                        funcionarioMap.get(entry.getKey()),
                        entry.getValue()
                });
            }

            lblTotalGeral.setText("Total geral: " + totalGeral);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao gerar relatório: " + e.getMessage());
        }
    }
}
