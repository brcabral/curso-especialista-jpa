package com.algaworks.ecommerce.operacoesemcascata;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.ItemPedidoId;
import com.algaworks.ecommerce.model.Pedido;
import org.junit.Assert;
import org.junit.Test;

public class CascadeTypeRemoveTest extends EntityManagerTest {
    @Test
    public void removerPedidoEItensPedido() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        entityManager.getTransaction().begin();
        entityManager.remove(pedido); // CascateType.REMOVE
        entityManager.getTransaction().commit();
        entityManager.clear();

        Pedido pedidoRemovido = entityManager.find(Pedido.class, pedido.getId());
        Assert.assertNull(pedidoRemovido);
    }

    @Test
    public void removerItemPedidoEPedido() {
        ItemPedido itemPedido = entityManager.find(ItemPedido.class,
                new ItemPedidoId(1, 1));

        entityManager.getTransaction().begin();
        entityManager.remove(itemPedido); // CascateType.REMOVE
        entityManager.getTransaction().commit();
        entityManager.clear();

        Pedido pedidoRemovido = entityManager.find(Pedido.class, itemPedido.getPedido().getId());
        Assert.assertNull(pedidoRemovido);
    }
}
