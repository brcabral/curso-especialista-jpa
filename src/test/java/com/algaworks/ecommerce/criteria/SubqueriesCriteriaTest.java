package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.List;

public class SubqueriesCriteriaTest extends EntityManagerTest {
    @Test
    public void produtosMaisCaros() {
        // Produto(s) mais caro(s) da base
        // String jpql = "select p from Produto p " +
        //        "where p.preco = (select max(preco) from Produto)";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<Produto> subqueryRoot = subquery.from(Produto.class);
        subquery.select(criteriaBuilder.max(subqueryRoot.get(Produto_.preco)));

        criteriaQuery.where(criteriaBuilder.equal(root.get(Produto_.preco), subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(prod -> System.out.println(
                "ID: " + prod.getId() + ", Nome: " + prod.getNome() + ", Preço: " + prod.getPreco()));
    }

    @Test
    public void pedidoAcimaDaMedia() {
        // Todos os pedidos acima da média de venda
        // String jpql = "select p from Pedido p " +
        //      "where p.total > (select avg(total) from Pedido)";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
        subquery.select(criteriaBuilder.avg(subqueryRoot.get(Pedido_.total)).as(BigDecimal.class));

        criteriaQuery.where(criteriaBuilder.greaterThan(root.get(Pedido_.total), subquery));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Total: " + p.getTotal()));
    }

    @Test
    public void bonsClientes1() {
        // String jpql = "select c from Cliente c " +
        //        "where 500 < (select sum(p.total) from Pedido p where p.cliente = c)";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        criteriaQuery.select(root);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
        subquery.select(criteriaBuilder.sum(subqueryRoot.get(Pedido_.total)));
        subquery.where(criteriaBuilder.equal(root, subqueryRoot.get(Pedido_.cliente)));

        criteriaQuery.where(criteriaBuilder.greaterThan(subquery, new BigDecimal(1300)));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Cliente> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(cli -> System.out.println("ID: " + cli.getId() + ", Nome: " + cli.getNome()));
    }

    @Test
    public void pesquisarComIn() {
        // String jpql = "select p from Pedido p " +
        //        "where p.id in (select p2.id from ItemPedido i2 " +
        //        "                   join i2.pedido p2 join i2.produto pro2 " +
        //        "               where pro2.preco > 100)";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        Join<ItemPedido, Pedido> subqueryJoinPedido = subqueryRoot.join(ItemPedido_.pedido);
        Join<ItemPedido, Produto> subqueryJoinProduto = subqueryRoot.join(ItemPedido_.produto);
        subquery.select(subqueryJoinPedido.get(Pedido_.id));
        subquery.where(criteriaBuilder.greaterThan(
                subqueryJoinProduto.get(Produto_.preco), new BigDecimal(100)));

        criteriaQuery.where(root.get(Pedido_.id).in(subquery));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }

    @Test
    public void pesquisarComExists() {
        // String jpql = "select p from Produto p " +
        //        "where exists (select 1 from ItemPedido ip2 join ip2.produto p2 " +
        //        "               where p2 = p)";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(criteriaBuilder.literal(1));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteriaQuery.where(criteriaBuilder.exists(subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }

    @Test
    public void clientesComMaisDeDoisPedidos() {
        // Todos os clientes que fizeram mais de dois pedidos
        // String jpql = "select c from Cliente c " +
        //         "where (select count(p) from c.pedidos p) > 2";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        criteriaQuery.select(root);

        Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
        Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
        subquery.select(criteriaBuilder.count(subqueryRoot.get(Pedido_.id)));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get(Pedido_.cliente), root));

        criteriaQuery.where(criteriaBuilder.greaterThan(subquery, 2L));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Cliente> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println("ID: " + c.getId() + ", Nome: " + c.getNome()));
    }

    @Test
    public void pesquisarPedidosPorProdutosDaCategoria() {
        // pesquisar todos os pedidos com produto da categoria 2
        // String jpql = "select p from Pedido p join p.itensPedido ip " +
        //        "where ip.produto in (select p2 from Produto p2 join p2.categorias c " +
        //        "                     where c.id = 2)";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        Join<Pedido, ItemPedido> joinItensPedido = root.join("itensPedido");

        criteriaQuery.select(root);

        Subquery<Produto> subquery = criteriaQuery.subquery(Produto.class);
        Root<Produto> subqueryRoot = subquery.from(Produto.class);
        subquery.select(subqueryRoot);
        Join<Produto, Categoria> subqueryJoinCategoria = subqueryRoot.join(Produto_.categorias);
        subquery.where(criteriaBuilder.equal(subqueryJoinCategoria.get(Categoria_.id), 2));

        criteriaQuery.where(joinItensPedido.get(ItemPedido_.produto).in(subquery));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", total: " + p.getTotal()));
    }

    @Test
    public void produtosVendidosPeloPrecoDiferenteDoAtualComExists() {
        // Todos os produtos que já foram vendidos por um preco diferente do atual
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(criteriaBuilder.literal(1));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root),
                criteriaBuilder.notEqual(
                        subqueryRoot.get(ItemPedido_.precoProduto), root.get(Produto_.preco)));

        criteriaQuery.where(criteriaBuilder.exists(subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", nome: " + p.getNome()));
    }

    @Test
    public void produtosSempreVendidosPeloPrecoAtual() {
        // Todos os produtos que sempre foram vendidos pelo preco atual
        // String jpql = "select p from Produto p " +
        //        "where p.preco = ALL (select precoProduto from ItemPedido where produto = p)";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteriaQuery.where(criteriaBuilder.equal(
                root.get(Produto_.preco), criteriaBuilder.all(subquery)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", nome: " + p.getNome()));
    }

    @Test
    public void produtosNaoVendidosDepoisDoAumentoPreco() {
        // Todos os produtos não foram vendidos mais depois que encareceram
        // String jpql = "select p from Produto p " +
        //        "where p.preco > ALL (select precoProduto from ItemPedido where produto = p " +
        //        "and exists (select 1 from ItemPedido where produto = p))";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteriaQuery.where(
                criteriaBuilder.greaterThan(
                        root.get(Produto_.preco), criteriaBuilder.all(subquery)),
                criteriaBuilder.exists(subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", nome: " + p.getNome()));
    }

    @Test
    public void produtosVendidosPeloPrecoAtual() {
        // Todos os produtos que já foram vendidos, pelo menos, uma vez pelo preço atual.
        // String jpql = "select p from Produto p " +
        //        "where p.preco = ANY (select precoProduto from ItemPedido where produto = p)";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteriaQuery.where(criteriaBuilder.equal(
                root.get(Produto_.preco), criteriaBuilder.any(subquery)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", nome: " + p.getNome()));
    }

    @Test
    public void produtosVendidosPeloPrecoDiferenteDoAtual() {
        // Todos os produtos que já foram vendidos por um preco diferente do atual
        // String jpql = "select p from Produto p " +
        //        "where p.preco <> ANY (select precoProduto from ItemPedido where produto = p)";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteriaQuery.where(criteriaBuilder.notEqual(
                root.get(Produto_.preco), criteriaBuilder.any(subquery)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", nome: " + p.getNome()));
    }

    @Test
    public void produtosSempreVendidosPeloMesmoPreco() {
        // Todos os produtos que sempre foram vendidos pelo mesmo preco
        String jpql = "select distinct ip.produto from ItemPedido ip " +
                "where ip.precoProduto = ALL (select ip2.precoProduto from ItemPedido ip2 " +
                "                             where ip2.produto = ip.produto) ";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);

        criteriaQuery.select(root.get(ItemPedido_.produto)).distinct(true);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(criteriaBuilder.equal(
                subqueryRoot.get(ItemPedido_.produto), root.get(ItemPedido_.produto)));

        criteriaQuery.where(criteriaBuilder.equal(
                root.get(ItemPedido_.precoProduto), criteriaBuilder.all(subquery)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", nome: " + p.getNome()));
    }
}
