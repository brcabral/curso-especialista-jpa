package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.List;

public class GroupByCriteriaTest extends EntityManagerTest {
    @Test
    public void quantidadeProdutoPorCategoria() {
        // Quantidade de produtos por categoria.
        // String jpql = "select c.nome, count(p.id) from Categoria c join c.produtos p group by c.id";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Categoria> root = criteriaQuery.from(Categoria.class);
        Join<Categoria, Produto> joinProduto = root.join(Categoria_.produtos, JoinType.LEFT);

        criteriaQuery.multiselect(
                root.get(Categoria_.nome),
                criteriaBuilder.count(joinProduto.get(Produto_.id))
        );

        criteriaQuery.groupBy(root.get(Categoria_.id));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();
        lista.forEach(arr -> System.out.println("Nome: " + arr[0] + ", Count: " + arr[1]));
    }

    @Test
    public void totalVendasPorCategoria() {
        // Total de vendas por categoria.
        // String jpql = "select c.nome, sum(ip.precoProduto * ip.quantidade) from ItemPedido ip " +
        //         "join ip.produto pro join pro.categorias c " +
        //         "group by c.id";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        Join<ItemPedido, Produto> joinProduto = root.join(ItemPedido_.produto);
        Join<Produto, Categoria> joinProdutoCategoria = joinProduto.join(Produto_.categorias);


        criteriaQuery.multiselect(
                joinProdutoCategoria.get(Categoria_.nome),
                criteriaBuilder.sum(root.get(ItemPedido_.precoProduto))
        );

        criteriaQuery.groupBy(joinProdutoCategoria.get(Categoria_.id));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();
        lista.forEach(arr -> System.out.println("Nome categoria: " + arr[0] + ", Sum: " + arr[1]));
    }

    @Test
    public void totalVendasPorCliente() {
        // Total de vendas por Cliente
        // String jpql = "select c.nome, sum(ip.precoProduto * ip.quantidade) from Pedido p " +
        //        "join p.cliente c join p.itensPedido ip " +
        //        "group by c.id";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        Join<Pedido, Cliente> joinCliente = root.join(Pedido_.cliente);
        Join<Pedido, ItemPedido> joinItemPedido = root.join(Pedido_.itensPedido);

        criteriaQuery.multiselect(
                joinCliente.get(Cliente_.nome),
                criteriaBuilder.sum(criteriaBuilder.prod(joinItemPedido.get(
                        ItemPedido_.precoProduto), joinItemPedido.get(ItemPedido_.quantidade)))
        );

        criteriaQuery.groupBy(joinCliente.get(Cliente_.id));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();
        lista.forEach(arr -> System.out.println("Nome: " + arr[0] + ", Sum: " + arr[1]));
    }

    @Test
    public void agruparResultadoComFuncoes() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        Expression<Integer> anoCriacaoPedido = criteriaBuilder
                .function("year", Integer.class, root.get(Pedido_.dataCriacao));
        Expression<Integer> mesCriacaoPedido = criteriaBuilder
                .function("month", Integer.class, root.get(Pedido_.dataCriacao));
        Expression<String> nomeMesCriacaoPedido = criteriaBuilder
                .function("monthname", String.class, root.get(Pedido_.dataCriacao));

        Expression<String> anoMesConcat = criteriaBuilder.concat(
                criteriaBuilder.concat(anoCriacaoPedido.as(String.class), "/"),
                nomeMesCriacaoPedido
        );

        criteriaQuery.multiselect(anoMesConcat, criteriaBuilder.sum(root.get(Pedido_.total)));
        criteriaQuery.groupBy(anoCriacaoPedido, mesCriacaoPedido);

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());
        lista.forEach(arr -> System.out.println("Ano/Mês: " + arr[0] + ", Sum: " + arr[1]));
    }

    @Test
    public void condicionarAgrupamentoComHaving() {
        // Total de vendas dentre as categorias que mais vendem.
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        Join<ItemPedido, Produto> joinProduto = root.join(ItemPedido_.produto);
        Join<Produto, Categoria> joinProdutoCategoria = joinProduto.join(Produto_.categorias);

        criteriaQuery.multiselect(
                joinProdutoCategoria.get(Categoria_.nome),
                criteriaBuilder.sum(root.get(ItemPedido_.precoProduto)),
                criteriaBuilder.avg(root.get(ItemPedido_.precoProduto))
        );

        criteriaQuery.groupBy(joinProdutoCategoria.get(Categoria_.id));

        criteriaQuery.having(criteriaBuilder.greaterThan(
                criteriaBuilder.avg(root.get(ItemPedido_.precoProduto))
                        .as(BigDecimal.class), new BigDecimal(700)));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());
        lista.forEach(arr -> System.out.println(
                "Nome categoria: " + arr[0]
                        + ", SUM: " + arr[1]
                        + ", AVG: " + arr[2]));
    }
}
