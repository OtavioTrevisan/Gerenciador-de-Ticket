package br.com.ticket.view;

import br.com.ticket.model.Funcionario;
import br.com.ticket.service.FuncionarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela responsável por exibir todos os funcionários cadastrados.
 * Permite atualizar a lista, editar um funcionário existente ou fechar a janela.
 */
public class FuncionarioListView extends JDialog {

    private JTable table;
    private DefaultTableModel tableModel;
    private FuncionarioService service = new FuncionarioService();

    /**
     * Construtor da janela de listagem de funcionários.
     * Configura os componentes visuais e inicia o carregamento da tabela.
     */
    public FuncionarioListView(JFrame parent) {
        super(parent, "Lista de Funcionários", true);
        setSize(700, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nome", "CPF", "Situação", "Última Alteração"}, 0
        );
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnEditar = new JButton("Editar Selecionado");
        JButton btnFechar = new JButton("Fechar");

        buttonsPanel.add(btnAtualizar);
        buttonsPanel.add(btnEditar);
        buttonsPanel.add(btnFechar);

        add(buttonsPanel, BorderLayout.SOUTH);

        btnAtualizar.addActionListener(e -> carregarFuncionarios());

        btnFechar.addActionListener(e -> dispose());

        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um funcionário.");
                return;
            }

            Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);

            try {
                Funcionario f = service.buscarPorId(id);

                if (f == null) {
                    JOptionPane.showMessageDialog(this, "Funcionário não encontrado!");
                    return;
                }

                FuncionarioEditForm form = new FuncionarioEditForm((JFrame) getParent(), f);
                form.setVisible(true);

                carregarFuncionarios();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        carregarFuncionarios();
    }

    /**
     * Carrega a lista de funcionários do banco e atualiza a tabela.
     */
    private void carregarFuncionarios() {
        try {
            List<Funcionario> funcionarios = service.listarTodos();

            tableModel.setRowCount(0);

            for (Funcionario f : funcionarios) {
                tableModel.addRow(new Object[]{
                        f.getId(),
                        f.getNome(),
                        f.getCpf(),
                        f.getSituacao(),
                        f.getDataAlteracao().toString()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar funcionários: " + e.getMessage());
        }
    }
}
