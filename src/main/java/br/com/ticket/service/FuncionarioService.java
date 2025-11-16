package br.com.ticket.service;

import br.com.ticket.dao.FuncionarioDAO;
import br.com.ticket.model.Funcionario;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/*Classe que faz a validação das regras de negócio dos funcionários
* */
public class FuncionarioService {
    private final FuncionarioDAO dao = new FuncionarioDAO();

    /*Método que faz as validações de criação do funcionário, checando o nome, CPF e situação,
      Caso seja válido, chama os métodos de inserção do objeto de DAO
     * */
    public void criarFuncionario(Funcionario f) throws Exception {
        if (f.getNome() == null || f.getNome().isBlank())
            throw new IllegalArgumentException("Nome obrigatório");

        if (f.getCpf() == null || f.getCpf().isBlank())
            throw new IllegalArgumentException("CPF obrigatório");

        if (!cpfValido(f.getCpf()))
            throw new IllegalArgumentException("CPF inválido.");

        if ("I".equalsIgnoreCase(f.getSituacao()))
            throw new IllegalArgumentException("Não é permitido criar funcionário com situação I");

        try {
            if (dao.buscarPorCpf(f.getCpf()) != null)
                throw new IllegalArgumentException("CPF já cadastrado");

            f.setDataAlteracao(LocalDateTime.now());
            dao.inserir(f);

        } catch (SQLException e) {

            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("cpf")) {
                throw new Exception("Já existe um funcionário cadastrado com este CPF.");
            }

            throw new RuntimeException(e);
        }
    }

    /*Método que faz as validações de criação do funcionário, checando o ID e CPF,
      Caso seja válido, chama os métodos de atualização do objeto de DAO
     * */
    public void atualizarFuncionario(Funcionario f) throws Exception {
        if (f.getId() == null)
            throw new IllegalArgumentException("Id obrigatório para atualizar");

        f.setDataAlteracao(LocalDateTime.now());

        try {
            Funcionario existente = dao.buscarPorCpf(f.getCpf());
            if (existente != null && !existente.getId().equals(f.getId())) {
                throw new IllegalArgumentException("O CPF informado já está em uso por outro funcionário.");
            }

            if (!cpfValido(f.getCpf()))
                throw new IllegalArgumentException("CPF inválido.");

            dao.atualizar(f);

        } catch (SQLException e) {

            // Caso o banco dispare erro de duplicidade
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("cpf")) {
                throw new Exception("O CPF informado já está em uso por outro funcionário.");
            }

            throw new RuntimeException(e);
        }
    }

    /*Método que chama o método de listar dentro do DAO, retornando uma lista de objetos de Funcionário
    * */
    public List<Funcionario> listarTodos() throws Exception {
        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*Método que chama o método de buscar por ID dentro do DAO, retornando um objeto de Funcionário
     * */
    public Funcionario buscarPorId(int id) throws Exception {
        try {
            return dao.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*Método que checa se um CPF é valido, com base no calculo fornecido pelo governo*/
    private boolean cpfValido(String cpf) {

        if (cpf == null || cpf.length() != 11) return false;

        if (cpf.chars().distinct().count() == 1) return false;

        try {
            int soma = 0;
            int peso = 10;

            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }

            int resto = soma % 11;
            int dv1 = (resto < 2) ? 0 : 11 - resto;

            soma = 0;
            peso = 11;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }
            soma += dv1 * peso;

            resto = soma % 11;
            int dv2 = (resto < 2) ? 0 : 11 - resto;

            return (cpf.charAt(9) - '0') == dv1 &&
                    (cpf.charAt(10) - '0') == dv2;

        } catch (Exception e) {
            return false;
        }
    }

}
