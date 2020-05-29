package com.algaworks.ecommerce.mapeamentoavancao;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.time.LocalDateTime;

// >>>> Forçar a ordem de execução <<<<

// Essa estratégia faz uma comparação de hashcodes.
//@FixMethodOrder(MethodSorters.DEFAULT)

// Aqui a JVM decide a ordem, e pode ser diferente a cada vez que o teste é executado.
//@FixMethodOrder(MethodSorters.JVM)

// Essa estratégia utiliza ordem lexicográfica. Ou seja, ordena pelos nomes dos métodos.
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChaveCompostaTest extends EntityManagerTest {
    @Test
    public void salvarItem() {
        entityManager.getTransaction().begin();

        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO);
        pedido.setTotal(produto.getPreco());

        entityManager.persist(pedido);
        entityManager.flush();

        ItemPedido itemPedido = new ItemPedido();
        // Modelo idClass
        // itemPedido.setPedidoId(pedido.getId());
        // itemPedido.setProdutoId(produto.getId());

        //Modelo EmbeddedId
        itemPedido.setId(new ItemPedidoId(pedido.getId(), produto.getId()));
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setPrecoProduto(produto.getPreco());
        itemPedido.setQuantidade(1);

        entityManager.persist(itemPedido);
        entityManager.getTransaction().commit();
        entityManager.clear();

        Pedido pedidoInserido = entityManager.find(Pedido.class, pedido.getId());
        Assert.assertNotNull(pedidoInserido);
        Assert.assertFalse(pedidoInserido.getItensPedido().isEmpty());
    }

    @Test
    public void buscarItem() {
        ItemPedido itemPedido = entityManager.find(ItemPedido.class,
                new ItemPedidoId(1, 1));
        Assert.assertNotNull(itemPedido);
    }
}
