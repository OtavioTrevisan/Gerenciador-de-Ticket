package br.com.ticket.view;

import br.com.ticket.model.Funcionario;
import br.com.ticket.service.FuncionarioService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * Formulário de cadastro de funcionário.
 * Permite criar um novo registro solicitando nome, CPF e situação.
 * Aplica máscara de CPF, valida dados e envia para o service.
 */
public class FuncionarioForm extends JDialog {

    private final JTextField txtNome = new JTextField(20);
    private JFormattedTextField txtCpf;
    private final JComboBox<String> cbSituacao = new JComboBox<>(new String[]{"A", "I"});
    private final FuncionarioService service = new FuncionarioService();

    /**
     * Construtor do formulário.
     * Configura a interface e inicializa os componentes.
     */
    public FuncionarioForm(JFrame parent) {
        super(parent, "Cadastro de Funcionário", true);
        setLayout(new BorderLayout());

        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            txtCpf = new JFormattedTextField(cpfMask);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao aplicar máscara de CPF", e);
        }

        JPanel form = new JPanel(new GridLayout(0,2,5,5));

        form.add(new JLabel("Nome:"));
        form.add(txtNome);

        form.add(new JLabel("CPF:"));
        form.add(txtCpf);

        form.add(new JLabel("Situação:"));
        form.add(cbSituacao);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());

        add(form, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Valida os campos, monta o objeto Funcionario
     * e envia para criação através do service.
     */
    private void salvar() {
        try {
            Funcionario f = new Funcionario();
            f.setNome(txtNome.getText().trim());

            String cpf = txtCpf.getText()
                    .replace(".", "")
                    .replace("-", "")
                    .replace("_", "");

            if (cpf.length() != 11)
                throw new IllegalArgumentException("CPF incompleto.");

            f.setCpf(cpf);
            f.setSituacao((String) cbSituacao.getSelectedItem());
            f.setDataAlteracao(LocalDateTime.now());

            service.criarFuncionario(f);

            JOptionPane.showMessageDialog(this, "Funcionário salvo com sucesso!");
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
}
