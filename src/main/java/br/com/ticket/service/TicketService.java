package br.com.ticket.service;

import br.com.ticket.dao.TicketDAO;
import br.com.ticket.model.TicketEntrega;

import java.time.LocalDateTime;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/*Classe que faz a validação das regras de negócio dos tickets
 * */
public class TicketService {

    private TicketDAO dao = new TicketDAO();

    /*Método que faz as validações de criação de um ticket, checando a existência de um funcionário ligado a esse ticket,
      Quantidade e situação, caso seja válido, chama os métodos de inserção do objeto de DAO
     * */
    public void criarTicket(TicketEntrega t) throws Exception {
        if (t.getFuncionarioId() == null)
            throw new IllegalArgumentException("Funcionário obrigatório.");

        if (t.getQuantidade() == null || t.getQuantidade() <= 0)
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");

        if ("I".equalsIgnoreCase(t.getSituacao()))
            throw new IllegalArgumentException("Não é permitido criar ticket com situação I.");

        t.setDataEntrega(LocalDateTime.now());

        try {
            dao.inserir(t);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*Método que chama o método de listar dentro do DAO, retornando uma lista de objetos de tickets
     * */
    public List<TicketEntrega> listarTodos() throws Exception {
        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*Método que chama o método de buscar por ID dentro do DAO, retornando um objeto de ticket
     * */
    public TicketEntrega buscarPorId(int id) throws Exception {
        try {
            return dao.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*Método que faz as validações de criação do funcionário, checando o ID,
    Caso seja válido, chama os métodos de atualização do objeto de DAO
    * */
    public void atualizarTicket(TicketEntrega t) throws Exception {
        if (t.getId() == null)
            throw new IllegalArgumentException("ID obrigatório para atualizar.");

        t.setDataEntrega(LocalDateTime.now()); // registra alteração

        try {
            dao.atualizar(t);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*Método que recebe um período de onde o relatório deve ser gerado, caso as datas sejam válidas,
      chama o método de listar por período do DAO, retornando uma lista de tickets que são daquele período
    * */
    public List<TicketEntrega> relatorioPorPeriodo(LocalDateTime inicio, LocalDateTime fim) throws Exception {
        if (inicio == null || fim == null)
            throw new IllegalArgumentException("Datas inválidas.");

        try {
            return dao.listarPorPeriodo(inicio, fim);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*Método que recebe um período de onde o relatório deve ser gerado, caso as datas sejam válidas,
      chama o método de somar por funcionários, retornando um mapa de funcionário e a quantidade de
      tickets que o funcionário pegou no período
    * */
    public Map<Integer, Integer> somarPorFuncionario(LocalDateTime inicio, LocalDateTime fim) throws Exception {
        try {
            return dao.somarPorFuncionario(inicio, fim);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
