package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.List;

public class PathExpressionTest extends EntityManagerTest {
    @Test
    public void usarPathExpressions() {
        String jpql = "select p.cliente.nome from Pedido p";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();

        // Assert.assertTrue(lista.size() == 2);
        Assert.assertFalse(lista.isEmpty());
    }

    @Test
    public void buscarPedidoComProdutoEspecifico() {
        String jpql = "select p from Pedido p join p.itensPedido i where i.produto.id = 1";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        List<Pedido> lista = typedQuery.getResultList();

        // Assert.assertTrue(lista.size() == 2);
        Assert.assertFalse(lista.isEmpty());
    }
}
