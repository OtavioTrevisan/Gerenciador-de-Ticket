package br.com.ticket.dao;

import br.com.ticket.config.Conexao;
import br.com.ticket.model.Funcionario;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    //Formatador de data usado para poder transformar num formato válido para o SQLite
    private static final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /*Método que recebe um objeto de um funcionário que tem as suas informações usadas para poder
    inserir dentro do banco de dados, e retorna a chave gerada pelo banco para poder atualizar o objeto
    mandado pelo parâmetro
    * */
    public void inserir(Funcionario f) throws SQLException {
        String sql = "INSERT INTO funcionario (nome, cpf, situacao, data_alteracao) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Passa como parâmetro de criação o comando base e diz que é necessário retornar o ID quando criado
            ps.setString(1, f.getNome());
            ps.setString(2, f.getCpf());
            ps.setString(3, f.getSituacao());
            ps.setString(4, f.getDataAlteracao().format(fmt));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    f.setId(rs.getInt(1));
                }
            }
        }
    }

    /*Método que recebe um objeto de um funcionário e atualiza dentro do banco de dados com base
     nas informações dentro do objeto
    * */
    public void atualizar(Funcionario f) throws SQLException {
        String sql = "UPDATE funcionario SET nome = ?, cpf = ?, situacao = ?, data_alteracao = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getNome());
            ps.setString(2, f.getCpf());
            ps.setString(3, f.getSituacao());
            ps.setString(4, f.getDataAlteracao().format(fmt));
            ps.setInt(5, f.getId());
            ps.executeUpdate();
        }
    }

    /*Método que recebe um ID e retorna um objeto JAVA do funcionário que tem o ID igual ao informado
    * */
    public Funcionario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM funcionario WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapFuncionario(rs);
            }
        }
        return null;
    }

    /*Método que recebe um CPF e retorna um objeto JAVA do funcionário que tem o CPF igual ao informado
    * */
    public Funcionario buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM funcionario WHERE cpf = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapFuncionario(rs);
            }
        }
        return null;
    }

    /*Método que retorna uma lista de todos os funcionários presentes dentro do banco de dados
    * */
    public List<Funcionario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM funcionario";
        List<Funcionario> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapFuncionario(rs));
            }
        }
        return lista;
    }

    /*Método que recebe um set de informações do banco e retorna um novo objeto JAVA de funcionário
    * */
    private Funcionario mapFuncionario(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String nome = rs.getString("nome");
        String cpf = rs.getString("cpf");
        String situacao = rs.getString("situacao");
        String dt = rs.getString("data_alteracao");
        LocalDateTime dataAlteracao = LocalDateTime.parse(dt, fmt);
        return new Funcionario(id, nome, cpf, situacao, dataAlteracao);
    }

}
