package io.github.eduardoluiz.libraryapi.repository;

import io.github.eduardoluiz.libraryapi.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransacoesTest {

    @Autowired
    TransactionService transactionService;

    /**
     * Commit -> confirmar as alterações
     * Roollback -> desfazer as alterações
     */

    @Test
    void TransacaoSimplesTest() {
        // Salvar livro
        // Salvar autor
        // Alugar o livro
        // Enviar email pro locatário
        // Notificar que o livro saiu da livraria
        transactionService.execute();
    }

    @Test
    void transacaoEstadoManaged() {
        transactionService.updateWithoutSaving();
    }
}
