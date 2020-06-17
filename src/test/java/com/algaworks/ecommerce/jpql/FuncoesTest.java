package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.List;

public class FuncoesTest extends EntityManagerTest {
    @Test
    public void aplicarFuncaoString() {
        // concat, length, locate, substring, lower, upper, trim
        // locate é similar ao indexOf do Java
        String jpql = "select c.nome, length(c.nome) from Categoria c " +
                "where substring(c.nome, 1, 1) = 'N'";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }

    @Test
    public void aplicarFuncaoData() {
        // current_date, current_time, current_timestamp
        // year(p.dataCriacao), month(p.dataCriacao), day(p.dataCriacao)
        String jpql = "select hour(p.dataCriacao), minute(p.dataCriacao), second(p.dataCriacao) " +
                "from Pedido p where hour(p.dataCriacao) < 6";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }
}
