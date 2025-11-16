package br.com.ticket.model;

import java.time.LocalDateTime;

/*Classe base para poder criar os objetos de Ticket
 * */
public class TicketEntrega {
    private Integer id;
    private Integer funcionarioId;
    private Integer quantidade;
    private String situacao;
    private LocalDateTime dataEntrega;

    public TicketEntrega() {}

    public TicketEntrega(Integer id, Integer funcionarioId, Integer quantidade, String situacao, LocalDateTime dataEntrega) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.quantidade = quantidade;
        this.situacao = situacao;
        this.dataEntrega = dataEntrega;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Integer funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
}
