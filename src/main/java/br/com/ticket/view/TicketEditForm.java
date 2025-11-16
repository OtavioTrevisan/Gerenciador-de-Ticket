package br.com.ticket.view;

import br.com.ticket.model.Funcionario;
import br.com.ticket.model.TicketEntrega;
import br.com.ticket.service.FuncionarioService;
import br.com.ticket.service.TicketService;
import br.com.ticket.view.util.FiltroNumerico;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Tela de edição de um ticket já existente.
 * Após a edição, o ticket é atualizado no banco de dados.
 */
public class TicketEditForm extends JDialog {

    private JComboBox<Funcionario> cbFuncionario;
    private JTextField txtQuantidade;
    private JComboBox<String> cbSituacao;

    private TicketEntrega ticket;

    private TicketService ticketService = new TicketService();
    private FuncionarioService funcionarioService = new FuncionarioService();

    /**
     * Construtor da tela de edição.
     * Carrega dados do ticket informado e configura o formulário visual.
     */
    public TicketEditForm(JFrame parent, TicketEntrega ticket) {
        super(parent, "Editar Ticket", true);
        this.ticket = ticket;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        form.add(new JLabel("Funcionário:"));
        cbFuncionario = new JComboBox<>();
        carregarFuncionarios();
        cbFuncionario.setSelectedItem(buscarFuncionario(ticket.getFuncionarioId()));
        form.add(cbFuncionario);

        form.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField(String.valueOf(ticket.getQuantidade()), 10);
        ((AbstractDocument) txtQuantidade.getDocument()).setDocumentFilter(new FiltroNumerico());
        form.add(txtQuantidade);

        form.add(new JLabel("Situação:"));
        cbSituacao = new JComboBox<>(new String[]{"A", "I"});
        cbSituacao.setSelectedItem(ticket.getSituacao());
        form.add(cbSituacao);

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.addActionListener(e -> salvar());

        add(form, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);
    }

    /**
     * Carrega todos os funcionários do banco
     * e adiciona ao ComboBox.
     */
    private void carregarFuncionarios() {
        try {
            List<Funcionario> funcionarios = funcionarioService.listarTodos();
            for (Funcionario f : funcionarios) {
                cbFuncionario.addItem(f);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar funcionários: " + e.getMessage());
        }
    }

    /**
     * Busca um funcionário pelo ID.
     * Usado para selecionar corretamente o funcionário atual no ComboBox.
     */
    private Funcionario buscarFuncionario(Integer id) {
        try {
            return funcionarioService.buscarPorId(id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Salva as alterações no ticket.
     * Coleta os dados do formulário, valida e envia ao serviço.
     */
    private void salvar() {
        try {
            Funcionario f = (Funcionario) cbFuncionario.getSelectedItem();
            if (f == null) {
                JOptionPane.showMessageDialog(this, "Selecione um funcionário.");
                return;
            }

            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());

            ticket.setFuncionarioId(f.getId());
            ticket.setQuantidade(quantidade);
            ticket.setSituacao((String) cbSituacao.getSelectedItem());
            ticket.setDataEntrega(LocalDateTime.now());

            ticketService.atualizarTicket(ticket);

            JOptionPane.showMessageDialog(this,
                    "Ticket atualizado com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
