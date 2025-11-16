package br.com.ticket.service;

import br.com.ticket.model.TicketEntrega;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class TicketServiceTest {

    private TicketService service;

    @BeforeEach
    void setup() {
        service = new TicketService();
    }

    @Test
    void naoDeveCriarTicketComQuantidadeZero() {
        TicketEntrega t = new TicketEntrega();
        t.setFuncionarioId(1);
        t.setQuantidade(0);
        t.setSituacao("A");

        Exception ex = assertThrows(Exception.class, () -> {
            service.criarTicket(t);
        });

        assertTrue(ex.getMessage().contains("Quantidade deve ser maior que zero"));
    }

    @Test
    void naoDeveCriarTicketComQuantidadeNegativa() {
        TicketEntrega t = new TicketEntrega();
        t.setFuncionarioId(1);
        t.setQuantidade(-5);
        t.setSituacao("A");

        Exception ex = assertThrows(Exception.class, () -> {
            service.criarTicket(t);
        });

        assertTrue(ex.getMessage().contains("Quantidade deve ser maior que zero"));
    }

    @Test
    void naoDeveCriarTicketComSituacaoI() {
        TicketEntrega t = new TicketEntrega();
        t.setFuncionarioId(1);
        t.setQuantidade(5);
        t.setSituacao("I");

        Exception ex = assertThrows(Exception.class, () -> {
            service.criarTicket(t);
        });

        assertTrue(ex.getMessage().contains("situação I"));
    }

    @Test
    void naoDeveGerarRelatorioComDatasNulas() {
        Exception ex = assertThrows(Exception.class, () -> {
            service.relatorioPorPeriodo(null, LocalDateTime.now());
        });

        assertTrue(ex.getMessage().contains("Datas inválidas"));
    }
}
