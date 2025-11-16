package br.com.ticket.dao;

import br.com.ticket.config.Conexao;
import br.com.ticket.model.TicketEntrega;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketDAO {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    /*
    * Função que insere dentro do banco de dados um novo ticket, com base nas informações fornecidas pelo objeto previamente criado
    *e atualiza o ID do objeto conforme o retorno do banco
    * */
    public void inserir(TicketEntrega t) throws SQLException {
        String sql = "INSERT INTO ticket_entrega (funcionario_id, quantidade, situacao, data_entrega) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, t.getFuncionarioId());
            ps.setInt(2, t.getQuantidade());
            ps.setString(3, t.getSituacao());
            ps.setString(4, t.getDataEntrega().format(fmt));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    t.setId(rs.getInt(1));
                }
            }
        }
    }

    /*Adiciona todos os tickets dentro do banco de dados numa lista de objetos JAVA de ticket,
     mapeando as informações retornadas pelo banco de dados através de uma função auxiliar de mapeamento
    * */
    public List<TicketEntrega> listarTodos() throws SQLException {
        String sql = "SELECT * FROM ticket_entrega";
        List<TicketEntrega> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapTicket(rs));
            }
        }

        return lista;
    }

    /*Função que recebe um set de resultados do banco de dados como parãmetro e
     retorna um novo objeto de ticket com base nas informações recebidas
    * */
    private TicketEntrega mapTicket(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        Integer id_funcionario = rs.getInt("funcionario_id");
        Integer quantidade = rs.getInt("quantidade");
        String situacao = rs.getString("situacao");
        LocalDateTime data = LocalDateTime.parse(rs.getString("data_entrega"), fmt);
        return new TicketEntrega(id, id_funcionario, quantidade, situacao, data);
    }

    /*Método que recebe um ID como parãmetro e busca dentro do banco de dados por esse ID,
     retornando o que acha em forma de objeto JAVA
    * */
    public TicketEntrega buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM ticket_entrega WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTicket(rs);
                }
            }
        }
        return null;
    }

    /*Método que recebe um objeto de ticket, e altera as informações dentro do banco de acordo com o
    ID dentro do objeto
    * */
    public void atualizar(TicketEntrega t) throws SQLException {
        String sql = "UPDATE ticket_entrega SET funcionario_id = ?, quantidade = ?, situacao = ?, data_entrega = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getFuncionarioId());
            ps.setInt(2, t.getQuantidade());
            ps.setString(3, t.getSituacao());
            ps.setString(4, t.getDataEntrega().format(fmt));
            ps.setInt(5, t.getId());

            ps.executeUpdate();
        }
    }

    /*Método que busca dentro do banco de dados por todos os tickets dentro do período passado como parâmetro,
     retornando uma lista desses tickets
    * */
    public List<TicketEntrega> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) throws SQLException {
        String sql = "SELECT * FROM ticket_entrega WHERE data_entrega BETWEEN ? AND ?";
        List<TicketEntrega> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inicio.format(fmt));
            ps.setString(2, fim.format(fmt));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapTicket(rs));
                }
            }
        }
        return lista;
    }

    /*Método que soma todos os tickets que cada funcionário recebeu dentro um período,
     retornando um mapa de funcionário e a soma de quantos tickets ele pediu no período
    * */
    public Map<Integer, Integer> somarPorFuncionario(LocalDateTime inicio, LocalDateTime fim) throws SQLException {
        String sql = """
        SELECT funcionario_id, SUM(quantidade) AS total
        FROM ticket_entrega
        WHERE data_entrega BETWEEN ? AND ?
        GROUP BY funcionario_id
    """;

        Map<Integer, Integer> mapa = new HashMap<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inicio.format(fmt));
            ps.setString(2, fim.format(fmt));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int funcionarioId = rs.getInt("funcionario_id");
                int total = rs.getInt("total");

                mapa.put(funcionarioId, total);
            }
        }

        return mapa;
    }
}
