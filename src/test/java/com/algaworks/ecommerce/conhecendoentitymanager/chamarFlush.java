package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;
import org.junit.Assert;
import org.junit.Test;

public class chamarFlush extends EntityManagerTest {
    @Test(expected = Exception.class)
    public void chamarFlush() {
        try {
            entityManager.getTransaction().begin();

            Pedido pedido = entityManager.find(Pedido.class, 1);
            pedido.setStatus(StatusPedido.PAGO);

            // Força o JPA sincronizar o entityManager com o banco de dados (realizar as transações em aberto)
            entityManager.flush();

            if (pedido.getPagamento() == null) {
                throw new RuntimeException("Pedido ainda não foi pago.");
            }

            // Uma consulta com JPQL obriga o JPA a sincronizar o que ele tem na memória automaticamente (sem usar o flush)
            // Pedido pedidoPago = entityManager
            //      .createQuery("select p from Pedido p where p.id = 1", Pedido.class)
            //      .getSingleResult();
            //Assert.assertEquals(pedido.getStatus(), pedidoPago.getStatus());

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().commit();
            throw e;
        }
    }
}
