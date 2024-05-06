package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GerenciamentoTransacoesTest extends EntityManagerTest {
    /**
     * Transação: período de tempo que é delimitado pelo begin e pelo commit,
     * que a gente pode fazer mudanças no banco de dados com consistência
     */
    @Test
    public void abrirFecharCancelarTransacao() {
        Assertions.assertThrows(Exception.class, () -> erroEsperadoMetodoDeNegocio());
    }

    private void erroEsperadoMetodoDeNegocio() {
        try {
            entityManager.getTransaction().begin();
            metodoDeNegocio();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    private void metodoDeNegocio() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        pedido.setStatus(StatusPedido.PAGO);

        if (pedido.getPagamento() == null) {
            throw new RuntimeException("Pedido ainda não foi pago.");
        }
    }
}
