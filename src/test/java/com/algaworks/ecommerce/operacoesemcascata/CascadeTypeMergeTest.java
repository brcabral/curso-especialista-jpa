package com.algaworks.ecommerce.operacoesemcascata;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CascadeTypeMergeTest extends EntityManagerTest {
    @Test
    public void atualizarPedidoComItens() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.AGUARDANDO);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId());
        itemPedido.getId().setPedidoId(pedido.getId());
        itemPedido.getId().setProdutoId(produto.getId());
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(3);
        itemPedido.setPrecoProduto(produto.getPreco());

        pedido.setItensPedido(Arrays.asList(itemPedido));  // CascadeType.MERGE

        entityManager.getTransaction().begin();
        entityManager.merge(pedido);
        entityManager.getTransaction().commit();
        entityManager.clear();

        ItemPedido itemPedidoAtualizado = entityManager.find(ItemPedido.class, itemPedido.getId());
        Assert.assertTrue(itemPedidoAtualizado.getQuantidade().equals(3));
    }

    @Test
    public void atualizarItemPedidoComPedido() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.PAGO);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId());
        itemPedido.getId().setPedidoId(pedido.getId());
        itemPedido.getId().setProdutoId(produto.getId());
        itemPedido.setPedido(pedido);  // CascadeType.MERGE
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(5);
        itemPedido.setPrecoProduto(produto.getPreco());

        entityManager.getTransaction().begin();
        entityManager.merge(pedido);
        entityManager.getTransaction().commit();
        entityManager.clear();

        ItemPedido itemPedidoAtualizado = entityManager.find(ItemPedido.class, itemPedido.getId());
        Assert.assertTrue(StatusPedido.PAGO.equals(itemPedidoAtualizado.getPedido().getStatus()));
    }
}
