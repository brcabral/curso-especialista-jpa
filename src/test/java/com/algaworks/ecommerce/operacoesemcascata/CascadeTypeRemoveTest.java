package com.algaworks.ecommerce.operacoesemcascata;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.ItemPedidoId;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import org.junit.Assert;

public class CascadeTypeRemoveTest extends EntityManagerTest {
    // @Test
    public void removerPedidoEItensPedido() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        entityManager.getTransaction().begin();
        entityManager.remove(pedido); // CascateType.REMOVE
        entityManager.getTransaction().commit();
        entityManager.clear();

        Pedido pedidoRemovido = entityManager.find(Pedido.class, pedido.getId());
        Assert.assertNull(pedidoRemovido);
    }

    // @Test
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

    // @Test
    public void removerRelacaoProdutoCategoria() {
        Produto produto = entityManager.find(Produto.class, 1);

        Assert.assertFalse(produto.getCategorias().isEmpty());

        entityManager.getTransaction().begin();
        produto.getCategorias().clear();
        entityManager.getTransaction().commit();
        entityManager.clear();

        Produto produtoInserido = entityManager.find(Produto.class, produto.getId());
        Assert.assertTrue(produtoInserido.getCategorias().isEmpty());
    }

    // @Test
    public void removerItensOrfaos() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        Assert.assertFalse(pedido.getItensPedido().isEmpty());

        entityManager.getTransaction().begin();
        pedido.getItensPedido().clear();
        entityManager.getTransaction().commit();
        entityManager.clear();

        Pedido pedidoRemovido = entityManager.find(Pedido.class, pedido.getId());
        Assert.assertTrue(pedidoRemovido.getItensPedido().isEmpty());
    }

}
