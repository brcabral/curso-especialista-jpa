package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ExpressoesCondicionaisTest extends EntityManagerTest {
    @Test
    public void usarExpressaoCondicionalLike() {
        String jpql = "select c from Cliente c where c.nome like concat('%', :nome, '%')";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        typedQuery.setParameter("nome", "a");
        List<Object[]> lista = typedQuery.getResultList();

        // Assert.assertTrue(lista.size() == 2);
        Assert.assertFalse(lista.isEmpty());
    }

    @Test
    public void usarIsNull() {
        String jpql = "select p from Produto p where p.foto is null";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();

        // Assert.assertTrue(lista.size() == 2);
        Assert.assertFalse(lista.isEmpty());
    }

    @Test
    public void usarIsEmpty() {
        // Usado para Collections
        String jpql = "select p from Produto p where p.categorias is empty";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();

        // Assert.assertTrue(lista.size() == 1);
        Assert.assertFalse(lista.isEmpty());
    }

    @Test
    public void usarMaiorMenor() {
        String jpql = "select p from Produto p where p.preco >= :precoInicial and p.preco <= :precoFinal";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        typedQuery.setParameter("precoInicial", new BigDecimal(400));
        typedQuery.setParameter("precoFinal", new BigDecimal(1500));
        List<Produto> lista = typedQuery.getResultList();

        // Assert.assertTrue(lista.size() == 2);
        Assert.assertFalse(lista.isEmpty());
    }

    @Test
    public void usarMaiorMenorComDatas() {
        String jpql = "select p from Pedido p where p.dataCriacao >= :dataPedido";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        typedQuery.setParameter("dataPedido", LocalDateTime.now().minusDays(2));
        List<Pedido> lista = typedQuery.getResultList();

        // Assert.assertTrue(lista.size() == 1);
        Assert.assertFalse(lista.isEmpty());
    }

    @Test
    public void usarBetween() {
        String jpql = "select p from Produto p where p.preco between :precoInicial and :precoFinal";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        typedQuery.setParameter("precoInicial", new BigDecimal(499));
        typedQuery.setParameter("precoFinal", new BigDecimal(1400));
        List<Produto> lista = typedQuery.getResultList();

        // Assert.assertTrue(lista.size() == 2);
        Assert.assertFalse(lista.isEmpty());
    }

    @Test
    public void usarBetweenComDatas() {
        String jpql = "select p from Pedido p where p.dataCriacao between :dataInicial and :dataFinal";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        typedQuery.setParameter("dataInicial", LocalDateTime.now().minusDays(2));
        typedQuery.setParameter("dataFinal", LocalDateTime.now());
        List<Pedido> lista = typedQuery.getResultList();

        // Assert.assertTrue(lista.size() == 1);
        Assert.assertFalse(lista.isEmpty());
    }

    @Test
    public void usarExpressaoDiferente() {
        String jpql = "select p from Produto p where p.preco <> 100";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        List<Produto> lista = typedQuery.getResultList();

        // Assert.assertTrue(lista.size() == 2);
        Assert.assertFalse(lista.isEmpty());
    }

    @Test
    public void usarExpressaoCase() {
        String jpql = "select p.id, " +
                "case p.status " +
                "   when 'PAGO' then 'Está pago' " +
                "   when 'CANCELADO' then 'Foi cancelado' " +
                "   else 'Está aguardando' " +
                "end " +
                "from Pedido p";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    public void usarExpressaoCaseComType() {
        // Utiliza o tipo da classe
        String jpql = "select p.id, " +
                "case type(p.pagamento) " +
                "   when PagamentoBoleto then 'Pago com boleto' " +
                "   when PagamentoCartao then 'Pago com cartão' " +
                "   else 'Ainda não foi pago' " +
                "end " +
                "from Pedido p";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }
}
