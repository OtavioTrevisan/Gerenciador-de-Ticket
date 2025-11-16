package br.com.ticket.view;

import br.com.ticket.model.Funcionario;
import br.com.ticket.service.FuncionarioService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * Janela de edição de funcionário.
 * Permite alterar nome, CPF e situação, aplicando validações
 * e salvando a atualização por meio do FuncionarioService.
 */
public class FuncionarioEditForm extends JDialog {

    private JTextField txtNome;
    private JFormattedTextField txtCpf;
    private JComboBox<String> cbSituacao;

    private Funcionario funcionario;
    private FuncionarioService service = new FuncionarioService();

    /**
     * Construtor do formulário de edição.
     * Recebe o funcionário que será editado e preenche os campos com os valores atuais.
     */
    public FuncionarioEditForm(JFrame parent, Funcionario funcionario) {
        super(parent, "Editar Funcionário", true);
        this.funcionario = funcionario;

        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        form.add(new JLabel("Nome:"));
        txtNome = new JTextField(funcionario.getNome(), 20);
        form.add(txtNome);

        form.add(new JLabel("CPF:"));
        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            txtCpf = new JFormattedTextField(cpfMask);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao aplicar máscara de CPF", e);
        }

        txtCpf.setText(formatarCpf(funcionario.getCpf()));
        form.add(txtCpf);

        form.add(new JLabel("Situação:"));
        cbSituacao = new JComboBox<>(new String[]{"A", "I"});
        cbSituacao.setSelectedItem(funcionario.getSituacao());
        form.add(cbSituacao);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());

        add(form, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);
    }

    /**
     * Realiza a validação dos dados e envia a atualização ao service.
     */
    private void salvar() {
        try {
            funcionario.setNome(txtNome.getText().trim());

            String cpf = txtCpf.getText()
                    .replace(".", "")
                    .replace("-", "")
                    .replace("_", "");

            if (cpf.length() != 11)
                throw new IllegalArgumentException("CPF incompleto.");

            funcionario.setCpf(cpf);

            funcionario.setSituacao((String) cbSituacao.getSelectedItem());
            funcionario.setDataAlteracao(LocalDateTime.now());

            service.atualizarFuncionario(funcionario);

            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Converte um CPF sem formatação (ex: 12345678901)
     * para o formato 123.456.789-01.
     */
    private String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return "";

        return cpf.substring(0, 3) + "." +
                cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" +
                cpf.substring(9, 11);
    }
}
