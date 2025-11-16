package br.com.ticket.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class Conexao {
    //URL aqui serve como o caminho do banco de dados, sendo ele, onde vamos tentar realizar a conexão para poder realizar as reais alterações posteriormente
    private static final String URL = "jdbc:sqlite:ticket.db";

    //Função de conexão, ela que utiliza do JDBC para tentar realizar a conexão com o banco, quando chamada, lembrar de utilizar exceção, pois o método joga para cima dentro da stack de execução o erro gerado pelo SQLite
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    //Função que usamos para poder executar os comandos que criamos dentro do arquivo de criação das tabelas do banco de dados, primeiro criamos um objeto de Statement, usado para poder executar comandos SQL
    public static void inicializarBanco() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
            Path path = Path.of("database.sql");
            if (Files.exists(path)) {
                String sql = Files.readString(path, StandardCharsets.UTF_8);
                for (String s : sql.split(";")) {
                    String trimmed = s.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inicializando banco: " + e.getMessage(), e);
        }
    }
}
