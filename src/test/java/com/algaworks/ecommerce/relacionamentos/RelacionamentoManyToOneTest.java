package com.algaworks.ecommerce.relacionamentos;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RelacionamentoManyToOneTest extends EntityManagerTest {
    @Test
    public void inserirPedido() {
        Cliente cliente = entityManager.find(Cliente.class, 1);

        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.AGUARDANDO);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setTotal(BigDecimal.TEN);
        pedido.setCliente(cliente);

        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoInserido = entityManager.find(Pedido.class, pedido.getId());
        Assert.assertNotNull(pedidoInserido.getCliente());
    }

    @Test
    public void inserirItemPedido() {
//        Cliente cliente = entityManager.find(Cliente.class, 1);
//        Produto produto = entityManager.find(Produto.class, 1);
//
//        Pedido pedido = new Pedido();
//        pedido.setStatus(StatusPedido.AGUARDANDO);
//        pedido.setDataCriacao(LocalDateTime.now());
//        pedido.setTotal(BigDecimal.TEN);
//        pedido.setCliente(cliente);
//
//        ItemPedido itemPedido = new ItemPedido();
//        itemPedido.setProduto(produto);
//        itemPedido.setPrecoProduto(produto.getPreco());
//        itemPedido.setQuantidade(2);
//        itemPedido.setPedido(pedido);
//
//        entityManager.getTransaction().begin();
//        entityManager.persist(pedido);
//        entityManager.persist(itemPedido);
//        entityManager.getTransaction().commit();
//
//        entityManager.clear();
//
//        ItemPedido itemPedidoInserido = entityManager.find(ItemPedido.class, itemPedido.getId());
//        Assert.assertNotNull(itemPedidoInserido.getPedido());
//        Assert.assertNotNull(itemPedidoInserido.getProduto());
    }
}
