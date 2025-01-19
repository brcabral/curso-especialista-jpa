package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NamedQueryTest extends EntityManagerTest {
    @Test
    public void listarTodosProdutos() {
        TypedQuery<Produto> typedQuery = entityManager.createNamedQuery("Produto.listar", Produto.class);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    public void listarProdutosPorCategoria() {
        TypedQuery<Produto> typedQuery = entityManager.createNamedQuery("Produto.listarPorCategoria", Produto.class);
        typedQuery.setParameter("categoria", 2);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    public void listarPedidosArquivoORM() {
        TypedQuery<Pedido> typedQuery = entityManager.createNamedQuery("Pedido.listar", Pedido.class);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    public void listarPedidosArquivoEspecifico() {
        TypedQuery<Pedido> typedQuery = entityManager.createNamedQuery("Pedido.todos", Pedido.class);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    public void listarProdutosArquivoEspecifico() {
        TypedQuery<Produto> typedQuery = entityManager.createNamedQuery("Produto.todos", Produto.class);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }
}
