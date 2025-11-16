package br.com.ticket.service;

import br.com.ticket.model.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioServiceTest {

    private FuncionarioService service;

    @BeforeEach
    void setup() {
        service = new FuncionarioService();
    }

    @Test
    void naoDeveCriarFuncionarioSemNome() {
        Funcionario f = new Funcionario();
        f.setCpf("12345678901");
        f.setSituacao("A");

        Exception ex = assertThrows(Exception.class, () -> {
            service.criarFuncionario(f);
        });

        assertTrue(ex.getMessage().contains("Nome obrigatório"));
    }

    @Test
    void naoDeveCriarFuncionarioComSituacaoI() {
        Funcionario f = new Funcionario();
        f.setNome("Teste");
        f.setCpf("43465430220");
        f.setSituacao("I");

        Exception ex = assertThrows(Exception.class, () -> {
            service.criarFuncionario(f);
        });

        assertTrue(ex.getMessage().toLowerCase().contains("situacao") ||
                ex.getMessage().toLowerCase().contains("situação"));
    }

    @Test
    void naoDeveCriarFuncionarioComCpfDuplicado() throws Exception {
        Funcionario f1 = new Funcionario();
        f1.setNome("Primeiro");
        f1.setCpf("79093670239");
        f1.setSituacao("A");
        service.criarFuncionario(f1);

        Funcionario f2 = new Funcionario();
        f2.setNome("Segundo");
        f2.setCpf("39053344705");
        f2.setSituacao("A");

        Exception ex = assertThrows(Exception.class, () -> {
            service.criarFuncionario(f2);
        });

        assertTrue(ex.getMessage().contains("CPF já cadastrado"));
    }
}
