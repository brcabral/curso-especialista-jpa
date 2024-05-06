package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FlushTest extends EntityManagerTest {
    /**
     * Flush: Obriga o JPA e pegar tudo que está na memória do JPA e sincronizar com o banco de dados.
     * Depois é necessário executar um commit para confirmar
     *   ou rollback para desfazer as alterações no banco de dados.
     *
     * Uma consulta obriga o JPA a sincronizar o que ele tem na memória (sem usar o flush explicitamente).
     */
    @Test
    public void chamarFlush() {
        Assertions.assertThrows(Exception.class, () -> erroAoChamarFlush());
    }

    private void erroAoChamarFlush() {
        try {
            entityManager.getTransaction().begin();

            Pedido pedido = entityManager.find(Pedido.class, 1);
            pedido.setStatus(StatusPedido.PAGO);

            entityManager.flush();

            if (pedido.getPagamento() == null) {
                throw new RuntimeException("Pedido ainda não foi pago!");
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
}
