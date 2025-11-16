package br.com.ticket.model;

import java.time.LocalDateTime;

/*Classe base para poder criar os objetos de Funcionário
* */
public class Funcionario {
    private Integer id;
    private String nome;
    private String cpf;
    private String situacao;
    private LocalDateTime dataAlteracao;

    public Funcionario() {}

    public Funcionario(Integer id, String nome, String cpf, String situacao, LocalDateTime dataAlteracao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.situacao = situacao;
        this.dataAlteracao = dataAlteracao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }
}
