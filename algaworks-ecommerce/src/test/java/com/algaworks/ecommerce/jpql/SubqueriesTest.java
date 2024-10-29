package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SubqueriesTest extends EntityManagerTest {
    @Test
    public void produtosMaisCaros() {
        String jpql = "select p from Produto p " +
                "where p.preco = (select max(preco) from Produto)";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", preço: " + p.getPreco()));
    }

    @Test
    public void pedidoAcimaDaMedia() {
        String jpql = "select p from Pedido p " +
                "where p.total > (select avg(total) from Pedido)";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", total: " + p.getTotal()));
    }

    @Test
    public void bonsClientes1() {
        String jpql = "select c from Cliente c " +
                "where 500 < (select sum(p.total) from Pedido p where p.cliente = c)";

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);
        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId() + ", Nome: " + obj.getNome()));
    }

    @Test
    public void bonsClientes2() {
        String jpql = "select c from Cliente c " +
                "where 500 < (select sum(p.total) from c.pedidos p)";

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);
        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId() + ", Nome: " + obj.getNome()));
    }

    @Test
    public void pesquisarComIN() {
        String jpql = "select p from Pedido p " +
                "where p.id in (select p2.id from ItemPedido i2 " +
                "                   join i2.pedido p2 join i2.produto pro2 " +
                "               where pro2.preco > 100)";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", total: " + p.getTotal()));
    }

    @Test
    public void pesquisarComExists() {
        String jpql = "select p from Produto p where exists " +
                "(select 1 from ItemPedido ip2 join ip2.produto p2 " +
                "where p2 = p)";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", preço: " + p.getPreco()));
    }

    @Test
    public void pesquisarProtudosPorCategoria() {
        // pesquisar todos os pedidos com produto da categoria 2
        String jpql = "select p from Pedido p where p.id in " +
                "(select p2.id from ItemPedido i2 " +
                "   join i2.pedido p2 join i2.produto pro2 join pro2.categorias c2 where c2.id = 2)";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId()));
    }

    @Test
    public void clientesComDoisOuMaisPedidos() {
        // Todos os clientes que fizeram dois ou mais pedidos
        String jpql = "select c from Cliente c where " +
                "(select count(cliente) from Pedido where cliente = c) >= 2";

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);
        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId() + ", Nome: " + obj.getNome()));
    }

    @Test
    public void produtosNaoVendidosComPrecoAtual() {
        // Todos os produtos que ainda não foram vendidos com o preço atual
        String jpql = "select p from Produto p " +
                "where exists (select 1 from ItemPedido " +
                "              where produto = p and precoProduto <> p.preco)";
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", preço: " + p.getPreco()));
    }

    @Test
    public void produtosSempreVendidosPeloPrecoAtual() {
        // Todos os produtos que sempre foram vendidos pelo preco atual
        String jpql = "select p from Produto p " +
                "where p.preco = ALL (select precoProduto from ItemPedido where produto = p)";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", preço: " + p.getPreco()));
    }

    @Test
    public void produtosNaoVendidosDepoisDoAumentoPreco() {
        // Todos os produtos não foram vendidos mais depois que encareceram
        String jpql = "select p from Produto p " +
                "where p.preco > ALL (select precoProduto from ItemPedido where produto = p)";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", preço: " + p.getPreco()));
    }
}
