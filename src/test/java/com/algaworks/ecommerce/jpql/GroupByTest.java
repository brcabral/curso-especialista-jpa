package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.List;

public class GroupByTest extends EntityManagerTest {
    @Test
    public void quantidadeProdutoPorCategoria() {
        String jpql = "select c.nome, count(p.id) from Categoria c join c.produtos p group by c.id";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    public void totalVendasPorMes() {
        String jpql = "select year(p.dataCriacao), function('monthname', p.dataCriacao), sum(p.total) " +
                "from Pedido p " +
                "group by year(p.dataCriacao), month(p.dataCriacao)";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1] + ", " + arr[2]));
    }

    @Test
    public void totalVendasPorCategoria() {
        String jpql = "select c.nome, sum(ip.precoProduto * ip.quantidade) from ItemPedido ip " +
                "join ip.produto pro join pro.categorias c " +
                "group by c.id";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    public void totalVendasPorCliente() {
        String jpql = "select c.nome, sum(ip.precoProduto * ip.quantidade) from Pedido p " +
                "join p.cliente c join p.itensPedido ip " +
                "group by c.id";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    public void totalVendasPorDiaPorCategoria() {
        String jpql = "select concat(day(p.dataCriacao), '/', month(p.dataCriacao), '/', year(p.dataCriacao)), " +
                "c.nome, sum(ip.precoProduto * ip.quantidade) " +
                "from ItemPedido ip " +
                "join ip.produto pro " +
                "join pro.categorias c " +
                "join ip.pedido p " +
                "group by p.dataCriacao, c.id";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1] + ": " + arr[2]));
    }

    @Test
    public void totalVendasPorMesAnoAtual() {
        String jpql = "select year(p.dataCriacao), function('monthname', p.dataCriacao), sum(p.total) " +
                "from Pedido p " +
                "where year(p.dataCriacao) = year(current_date) " +
                "group by year(p.dataCriacao), month(p.dataCriacao)";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1] + ", " + arr[2]));
    }

    @Test
    public void totalVendasPorCategoriaMesAtual() {
        String jpql = "select c.nome, sum(ip.precoProduto * ip.quantidade) from ItemPedido ip " +
                "join ip.produto pro join pro.categorias c join ip.pedido p " +
                "where year(p.dataCriacao) = year(current_date) and month(p.dataCriacao) = month(current_date) " +
                "group by c.id";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    public void totalVendasPorClienteUltimosTresMeses() {
        String jpql = "select c.nome, sum(ip.precoProduto * ip.quantidade) from Pedido p " +
                "join p.cliente c join p.itensPedido ip " +
                "where year(p.dataCriacao) = year(current_date) and month(p.dataCriacao) >= (month(current_date) - 3) " +
                "group by c.id";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }
}
