package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class BasicoCriteriaTest extends EntityManagerTest {
    @Test
    public void buscarPorIdentificador() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        Pedido pedido = typedQuery.getSingleResult();
        Assertions.assertNotNull(pedido);
    }

    @Test
    public void selecionarUmAtributoClienteParaRetorno() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root.get("cliente"));
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        Cliente cliente = typedQuery.getSingleResult();
        Assertions.assertEquals("Fernando Medeiros", cliente.getNome());
    }

    @Test
    public void selecionarUmAtributoBigDecimalParaRetorno() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> criteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root.get("total"));
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

        TypedQuery<BigDecimal> typedQuery = entityManager.createQuery(criteriaQuery);
        BigDecimal total = typedQuery.getSingleResult();
        Assertions.assertEquals(new BigDecimal("2398.00"), total);
    }

    @Test
    public void retornarTodosOsProdutos() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    public void projetarResultado() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.multiselect(root.get("id"), root.get("nome"));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println("ID: " + arr[0] + ", nome: " + arr[1]));
    }

    @Test
    public void projetarResultadoTuple() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Produto> root = criteriaQuery.from(Produto.class);

        // criteriaQuery.multiselect(root.get("id"), root.get("nome"));
        criteriaQuery.select(criteriaBuilder
                .tuple(root.get("id").alias("id"), root.get("nome").alias("nome")));

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Tuple> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        // lista.forEach(t -> System.out.println("ID: " + t.get(0) + ", nome: " + t.get(1)));
        lista.forEach(t -> System.out.println("ID: " + t.get("id") + ", nome: " + t.get("nome")));
    }
}
