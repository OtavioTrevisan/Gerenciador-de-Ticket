package br.com.ticket.view;

import br.com.ticket.model.TicketEntrega;
import br.com.ticket.model.Funcionario;
import br.com.ticket.service.FuncionarioService;
import br.com.ticket.service.TicketService;
import br.com.ticket.view.util.FiltroNumerico;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Tela de cadastro de um novo ticket.
 * Ao salvar, os dados são enviados ao serviço e inseridos no banco.
 */
public class TicketForm extends JDialog {

    private JComboBox<Funcionario> cbFuncionario;
    private JTextField txtQuantidade;
    private JComboBox<String> cbSituacao;

    private final FuncionarioService funcionarioService = new FuncionarioService();
    private final TicketService ticketService = new TicketService();

    /**
     * Construtor da tela de cadastro.
     * Inicializa os componentes visuais do formulário.
     */
    public TicketForm(JFrame parent) {
        super(parent, "Cadastro de Ticket", true);

        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        form.add(new JLabel("Funcionário:"));

        cbFuncionario = new JComboBox<>();
        carregarFuncionarios();
        form.add(cbFuncionario);

        form.add(new JLabel("Quantidade:"));

        txtQuantidade = new JTextField(10);
        ((AbstractDocument) txtQuantidade.getDocument()).setDocumentFilter(new FiltroNumerico());
        form.add(txtQuantidade);

        form.add(new JLabel("Situação:"));
        cbSituacao = new JComboBox<>(new String[]{"A", "I"});
        form.add(cbSituacao);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());

        add(form, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);
    }

    /**
     * Carrega todos os funcionários do banco
     * e adiciona ao ComboBox do formulário.
     */
    private void carregarFuncionarios() {
        try {
            List<Funcionario> funcionarios = funcionarioService.listarTodos();
            funcionarios.forEach(cbFuncionario::addItem);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar funcionários: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Executa o processo de validação e criação de um novo ticket.
     * Caso todas as regras sejam atendidas, insere o ticket no banco.
     */
    private void salvar() {
        try {

            Funcionario f = (Funcionario) cbFuncionario.getSelectedItem();
            if (f == null) {
                JOptionPane.showMessageDialog(this, "Selecione um funcionário.");
                return;
            }

            String quantidadeStr = txtQuantidade.getText().trim();

            if (quantidadeStr.isEmpty()) {
                throw new IllegalArgumentException("Quantidade obrigatória.");
            }

            int quantidade = Integer.parseInt(quantidadeStr);

            if (quantidade <= 0) {
                throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
            }

            TicketEntrega t = new TicketEntrega();
            t.setFuncionarioId(f.getId());
            t.setQuantidade(quantidade);
            t.setSituacao((String) cbSituacao.getSelectedItem());
            t.setDataEntrega(LocalDateTime.now());

            ticketService.criarTicket(t);

            JOptionPane.showMessageDialog(this, "Ticket cadastrado com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
